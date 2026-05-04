# 上游 Kafka 消息升级改造设计文档

**日期**：2026-05-04
**作者**：littlefatz
**状态**：Draft，待 review

---

## 1. 背景

我方系统是一个 Spring Boot 应用，消费上游系统发送的 Kafka 消息。每条消息进入后，先落入 `corporate_action_message` 表（用于失败时的重放补偿），再调用多个下游 wapi API 构造业务对象，写入 `corporate_action` 表（前端展示数据源）。

上游系统即将进行升级，对消息体的两个字段做了以下改造：

1. **`templateId` 类型细化**：原本只有 `initial` 一种类型，且语义上兼顾"首次创建"和"后续更新"。升级后细化为三种类型：
   - `initial`：首次创建（仅 create）
   - `update`：业务变更（仅 update）
   - `reminder`：触发提醒通知（不动业务数据）

2. **`portfolioNumber` 字段格式变更**：
   - 旧格式：纯数字 `3477234`，本质是上游系统数据库中 portfolio 记录的主键 ID（以下称 `portfolioId`）
   - 新格式：`<字母前缀>-<数字>-<数字>`，例如 `S-12313-0`、`P-2347-0` 等。字母前缀代表业务分类，**不固定为 S**，可能出现多种字母
   - 旧流程中，我方系统需要拿 `portfolioId` 调一次 wapi API 才能换出新格式编号；上游升级的目的就是让我方省掉这次额外调用

由于我方上线时间早于上游切换时间，**我方代码必须同时兼容新老两种格式的消息**，并在上游切换稳定后逐步下线兼容代码。

## 2. 目标和非目标

### 2.1 目标

- 在上游切换前后均能正确消费 Kafka 消息，业务数据不丢失、不错乱
- 在保留补偿能力（消息可重放）的前提下，平滑过渡到新格式
- 抓住上游升级的红利：上游切换稳定后，主流程不再需要为"换号"调 wapi
- 留下可观测的指标，用数据驱动"何时下线兼容代码"的决策

### 2.2 非目标

- 不在本次变更中做大规模 schema 重构（不变更唯一键、不新增列）
- 不在上线时跑一次性数据迁移脚本（上线流程复杂，无窗口）
- 不改造下游 wapi API 本身

## 3. 关键约束和已确认事实

| # | 事实 | 影响 |
|---|---|---|
| 1 | 上游一刀切，但我方先发布 | 兼容期 = 我方上线 → 上游切换之间，可能持续数日 |
| 2 | wapi 换号 API 永远可用，不会下线 | 老格式分支可以一直依赖它 |
| 3 | `corporate_action_message` 唯一键 = `(orderNumber, portfolioNumber)`，但列名 `portfolioNumber` 在老流程下实际存的是 `portfolioId` | 同列双值是历史既定事实，本方案沿用 |
| 4 | `corporate_action` 唯一键 = `(orderNumber, portfolioNumber)`，存真正的字母前缀格式（如 `S-12313-0` / `P-2347-0`） | 业务表语义不变 |
| 5 | 一个 `(orderNumber, cin)` 下面 ≤ 3 个 portfolio | 懒迁移 fallback 时 wapi 反查最多 3 次 |
| 6 | `cin` 必然存在于 message body 中 | 是 fallback 路径的前置条件 |
| 7 | 补偿任务通过重放消息（content JSON）触发完整业务流水线 | content 必须保留得能重放 |
| 8 | `templateId` 字段已经存在于 message JSON 中 | 派发逻辑可从 content 解析，无需新增列 |

## 4. 总体架构

在 Kafka consumer 入口前增加一层 **MessageAdapter（消息适配器）**，作为整个改造的核心防腐层：

```
┌──────────────────────┐
│ Kafka Consumer       │
└──────────┬───────────┘
           ↓ raw KafkaMessage
┌──────────────────────────────────────────────┐
│ MessageAdapter                               │
│ ─ 检测 portfolioNumber 字段格式             │
│ ─ 老格式：调 wapi 补齐真实 portfolioNumber   │
│ ─ 新格式：直接透传                          │
│ ─ 输出：UnifiedMessage（含 era 标签）       │
└──────────┬───────────────────────────────────┘
           ↓ UnifiedMessage(era=LEGACY|NEW, templateId, ...)
┌──────────────────────────────────────────────┐
│ MessageProcessor                             │
│ ─ corporate_action_message 落库（reminder 跳过） │
│ ─ 按 templateId 派发到三种 handler          │
│   • InitialHandler                          │
│   • UpdateHandler                           │
│   • ReminderHandler                         │
└──────────────────────────────────────────────┘
```

业务层（MessageProcessor 及其下游）只面对 `UnifiedMessage` 对象，不感知"老格式 vs 新格式"的差异。这样上游切换稳定后，下线兼容代码 = 删除 MessageAdapter 的老格式分支，业务代码无需改动。

## 5. 详细设计

### 5.1 格式检测与归一化（MessageAdapter）

**检测规则**：

```
正则：^\d+$              → era = LEGACY（纯数字 portfolioId）
正则：^[A-Z]-\d+-\d+$    → era = NEW（字母前缀格式 portfolioNumber，如 S-12313-0、P-2347-0）
其他                     → 抛异常并入死信队列
```

⚠️ 字母前缀的具体取值范围（是否仅大写、是否单字符等）需要和上游系统确认。如果未来上游引入小写字母或多字符前缀，需要同步调整正则。建议把正则提取为可配置项（application.yml），便于无需发版调整。

**归一化输出**（`UnifiedMessage`）：

| 字段 | 老格式来源 | 新格式来源 |
|---|---|---|
| `orderNumber` | message | message |
| `portfolioNumber`（字母前缀格式） | 调 wapi 用 portfolioId 换 | message 直取 |
| `portfolioId`（纯数字，可选） | message 直取 | 不填（默认 null） |
| `cin` | message | message |
| `templateId` | message（必为 `initial`） | message |
| `era` | `LEGACY` | `NEW` |
| `rawContent` | 原始 JSON | 原始 JSON |

**注意**：`portfolioId` 字段在新格式下不填，新流程主流程也不依赖它。仅在懒迁移 fallback 路径中，会把 `corporate_action_message` 表里某行的旧 portfolioId 通过 wapi 校验后替换成新 portfolioNumber。

### 5.2 `corporate_action_message` 落库逻辑

主流程（`MessageProcessor`）按以下顺序处理：

**前置异常拦截**：在落库前先校验 `(era, templateId)` 是否合法，详见 5.4 表。非法组合（如 `LEGACY + update`、`LEGACY + reminder`）直接 reject，不落 `corporate_action_message`，进死信队列 + 业务告警。

```
if (era, templateId) is illegal combination:
    reject + alarm + dead-letter
    return

if (templateId == reminder):
    # 5.5 节，不落 corporate_action_message
    skip 落库

elif era == LEGACY:
    # 老格式：照搬旧逻辑，列值是 portfolioId
    upsert corporate_action_message
        key   = (orderNumber, portfolioId)        # 即 message 里的 portfolioNumber 字段（数字格式）
        content = rawContent
    继续业务流水线（用 wapi 已换出的 NEW-format portfolioNumber 调下游）

else:  # era == NEW
    # 新格式：先尝试直查
    row = SELECT * FROM corporate_action_message
          WHERE orderNumber=? AND portfolioNumber=?(NEW-format)

    if row exists:
        # 切换后产生的数据，直接覆盖 content
        UPDATE corporate_action_message SET content=rawContent WHERE id=row.id
    else:
        # 5.3 节：懒迁移 fallback
        懒迁移流程

    继续业务流水线
```

### 5.3 新格式 fallback：懒迁移流程

当新格式消息进来、按 `(orderNumber, NEW-format)` 直查未命中时：

```
candidates = SELECT * FROM corporate_action_message
             WHERE orderNumber=? AND cin=?

if candidates is empty:
    # 这个 portfolio 我方系统从未见过
    INSERT new row
        portfolioNumber = NEW-format
        content = rawContent
    return  # 是合法的"首次"，由后续 templateId 派发决定怎么处理 corporate_action

else:
    # 候选行 ≤ 3 个，逐一比对
    matched = null
    for c in candidates:
        # c.portfolioNumber 列值此时仍是 portfolioId（纯数字）
        # 否则上一步直查就命中了
        derived = wapi.lookupPortfolioNumber(c.portfolioNumber)  # portfolioId → NEW-format
        if derived == message.portfolioNumber:
            matched = c
            break

    if matched != null:
        # 命中老行 → 懒迁移：更新该行的列值为 NEW-format，并覆盖 content
        UPDATE corporate_action_message
            SET portfolioNumber = ?(NEW-format), content = ?(rawContent)
            WHERE id = matched.id
    else:
        # cin 下都不匹配 → 也是首次见过的 portfolio
        INSERT new row
            portfolioNumber = NEW-format
            content = rawContent
```

**唯一键冲突分析**：UPDATE 时新值 `(orderNumber, NEW-format)` 必定不冲突——若它存在，第一步直查就命中了，不会走到 fallback。

**最坏情况成本**：3 次 wapi 调用 + 1 次 DB 查询 + 1 次 DB 更新。仅发生在"上游切换后第一次为某老 portfolio 收到新消息"的瞬间，整体可控。

### 5.4 templateId 派发逻辑

`corporate_action_message` 落库（或跳过）成功后，进入 `corporate_action` 表的写入逻辑。派发依据 `(era, templateId)` 二元组：

| era | templateId | 行为 | 异常处理 |
|---|---|---|---|
| LEGACY | initial | 走旧 create-or-update 逻辑（兼顾两种语义）| 不视为异常 |
| LEGACY | update | 不应出现 | 抛异常 + 报警（上游不可能给老格式消息打 update 标签）|
| LEGACY | reminder | 不应出现 | 抛异常 + 报警 |
| NEW | initial | 仅 create；若 corp_action 已存在则报错 | 打印 ERROR 日志 + 业务监控告警 |
| NEW | update | 仅 update；若 corp_action 不存在则报错 | 打印 ERROR 日志 + 业务监控告警 |
| NEW | reminder | 不写 corp_action，调通知服务发送提醒 | 通知失败仅打印 WARN 日志（接受 reminder 丢失）|

**关键点**：`era` 不依赖任何持久化列，由解析 message JSON 的 `portfolioNumber` 格式即时推导得到。补偿任务也用同样的方式从 `content` 解析。

### 5.5 reminder 特殊路径

`reminder` 消息**不写 `corporate_action_message`**，原因：

- `corporate_action_message` 的存在意义是补偿能重放
- `reminder` 不动业务数据，没有可补偿的对象
- 让 `reminder` 写表反而会污染 content（覆盖未处理的 initial/update），导致补偿读到 reminder 时丢失关键步骤

具体处理：

```
if templateId == reminder:
    notificationService.send(orderNumber, portfolioNumber, cin, ...)
    log.info("reminder dispatched: order={} portfolio={}", ...)
    return  # 流水线结束
```

如果业务侧需要"reminder 何时发给了哪些客户"的审计：另建 `notification_audit_log` 表或用应用日志 + ELK，**不要塞进 `corporate_action_message`**。

### 5.6 补偿任务的派发逻辑

补偿任务的本职工作不变（扫待补偿的行 → 重放完整流水线 → 标记成功/失败），唯一新增的是从 content 解析 era 和 templateId：

```
for row in SELECT * FROM corporate_action_message WHERE status='PENDING':
    json = parse(row.content)
    era = detectEra(json.portfolioNumber)
    templateId = json.templateId

    dispatch(era, templateId, json)  # 同 5.4 节表格
```

由于 reminder 永远不会落到这张表，补偿任务实际只需处理 `(LEGACY, initial)` / `(NEW, initial)` / `(NEW, update)` 三种组合。

### 5.7 数据库变更

**无 schema 变更**。

- 不新增列（`templateId` 从 content JSON 解析）
- 不变更唯一键
- 不做存量数据迁移

## 6. 兼容期与下线策略

### 6.1 时间线

```
T0  我方上线（含 MessageAdapter）
    │ 兼容期 ── 老格式消息正常处理（走 LEGACY 分支）
T1  上游切换（一刀切）
    │ 过渡观察期 ── 新消息全部走 NEW 分支；老存量行通过懒迁移逐步收敛
T2  legacy_count 指标曲线变平 + 业务确认稳定
    │
T3  下线兼容代码（删除 MessageAdapter 的 LEGACY 分支、删除补偿任务的 LEGACY 派发分支）
```

### 6.2 下线条件

下线兼容代码的判断依据：

1. 上游切换后已稳定运行 ≥ N 周（建议 4 周起，由业务方确定）
2. `corporate_action_message_legacy_count` 指标连续 ≥ 7 天不再下降（曲线变平）
3. 业务确认残留 legacy 行属于不会再产生新消息的冷数据

**下线后处理**：残留 legacy 行保留在表中，万一被补偿任务读到，由保留的 LEGACY 兜底分支处理（建议保留兜底分支多一个版本周期再彻底删除）。

## 7. 监控与可观测

新增以下监控项：

| 指标 / 日志 | 类型 | 用途 |
|---|---|---|
| `kafka_message_consumed{era}` | Counter | 兼容期内观察新老格式消息比例 |
| `corporate_action_message_legacy_count` | Gauge（每日采样）| 下线决策依据 |
| `wapi_call_count{purpose=lazy_migration}` | Counter | 监控 fallback 路径 wapi 调用频率 |
| `lazy_migration_executed` | Counter | 每次成功的懒迁移更新计 +1 |
| `lazy_migration_failed_no_match` | Counter | cin 下逐一比对都不匹配（首次见过的 portfolio）的次数 |
| `template_anomaly{type}` | Counter | NEW initial 已存在 / NEW update 不存在 / LEGACY 收到 update or reminder 等异常 |
| `notification_send_failed` | Counter | reminder 通知失败 |

**告警建议**：

- `template_anomaly` 任意类型 > 0 且持续 → 业务告警（说明上游或我方逻辑有问题）
- `wapi_call_count{purpose=lazy_migration}` 异常飙高 → 可能存在格式判断 bug
- 上游切换后，`kafka_message_consumed{era=LEGACY}` 应快速归零，否则告警

## 8. 幂等性与顺序性

### 8.1 幂等性

Kafka 至少一次投递，重复消息可能出现。各路径的幂等保证：

- `corporate_action_message`：upsert 语义，重复消息覆盖 content，无害
- `corporate_action` initial：现有逻辑已保证 create 失败时检测唯一约束
- `corporate_action` update：UPDATE 操作天然幂等（同样数据多次写入结果一致）
- reminder：通知服务需自行去重（如使用 messageId 做幂等键），或接受偶发重复通知

### 8.2 顺序性

**现状**：partition key 由上游系统决定，我方无从得知；因此我们**不能假设同一业务对象的消息一定按发出顺序被处理**。乱序可能在以下场景出现：
- 兼容期内同一业务对象切换前后 key 不同，可能落到不同 partition
- 即便同一格式期内，如果上游 key 选取不当，同业务消息也可能跨 partition
- 多消费者并发处理不同 partition 时，没有强顺序保证

**业务可接受性**：上游流量不高，乱序不是硬伤。设计上要做的是**不让乱序导致数据错乱**，而不是**强制有序消费**。

**乱序场景与处理**：

| 场景 | 表现 | 处理 |
|---|---|---|
| update 先于 initial 到达 | NEW update 进来时 corporate_action 表中无记录 | 按 5.4 表抛异常 + 报警 + 留 corporate_action_message 行待补偿。补偿任务重试时 initial 大概率已经被处理，update 重放成功 |
| reminder 先于 initial/update 到达 | 通知发出时业务数据可能尚未就绪 | 接受 reminder 失效（通知可能不准确），不影响业务表 |
| 切换瞬时老 partition 积压 + 新 partition 已有新消息 | 同一业务的 LEGACY initial 与 NEW update 并发处理 | 同上"update 先于 initial"的处理；额外建议切换前协调上游等我方消费 lag 归零再切换 |
| 同业务两条 update 乱序 | 后发的 update 比先发的 update 先到 | 因为补偿任务重放是从 wapi 取最新数据（不是用 message body 内容），无论哪条先处理，最终 corporate_action 都会同步到上游最新状态，**幂等收敛** |

**缓解措施总结**：

1. 切换前协调上游"等我方消费 lag 归零再切"，把切换瞬时乱序窗口压到最小
2. 监控 `template_anomaly{type=NEW_update_no_record}`、`template_anomaly{type=NEW_initial_existing_record}` 指标，乱序导致的异常会显式呈现
3. 切换后初期让补偿任务跑得更频繁（缩短重试间隔）— 因 race 失败的消息能更快被重放修复
4. 长期依赖"补偿任务从 wapi 取最新数据"的幂等收敛特性兜底

## 9. 测试方案

### 9.1 单元测试

- MessageAdapter：覆盖 LEGACY / NEW / 非法格式三种输入
- 派发逻辑：`(era, templateId)` 6 种组合的正反路径

### 9.2 集成测试

涵盖以下场景，按时间线编排：

1. 兼容期：连续 N 条老格式消息（initial 的 create / update 两种语义）
2. 切换瞬时：发完老格式消息后立即发新格式消息（同一业务对象）
3. 切换后纯新格式：initial → update → reminder 完整生命周期
4. 懒迁移触发：老格式 initial 落库 → 切换 → 新格式 update 进来
5. 异常场景：NEW initial 撞已有记录、NEW update 找不到记录、LEGACY 收到 update（非法）
6. fallback 边界：`(orderNumber, cin)` 下 0 / 1 / 3 个候选 portfolio 的情况
7. 补偿任务：故意让某行处理失败，验证补偿任务能从 content 正确派发

### 9.3 性能测试

- 兼容期内整体 TPS（含 wapi 调用）应不低于改造前
- 切换后整体 TPS 应**优于**改造前（少一次 wapi 调用）
- 懒迁移 fallback 单次最长耗时（含 ≤ 3 次 wapi 反查）控制在 SLA 内

## 10. 回滚策略

本次改造**前向兼容老逻辑**——MessageAdapter 在 LEGACY 分支下的行为等价于旧代码。因此：

- 上线后发现新代码逻辑有 bug，可直接回滚到上一版本
- 上游切换前回滚不影响业务（消息全是老格式，老代码本来就处理）
- 上游切换后回滚有风险（老代码不认识 NEW 格式），建议切换后回滚需上游配合"暂时降回老格式"或者紧急修复前向

## 11. 风险与未决问题

| 风险 | 缓解措施 |
|---|---|
| MessageAdapter 格式判断正则的边界 case（如未来 portfolioNumber 出现新变种）| 严格用既定正则；不匹配 → 死信队列 + 人工介入；监控 `kafka_message_consumed{era=UNKNOWN}` |
| 懒迁移 fallback 路径在高并发下重复 wapi 调用 | 加进程内 LRU 缓存 portfolioId → portfolioNumber 映射（短 TTL） |
| 上游切换时间不确定，兼容期可能比预期长 | 监控 LEGACY 流量比例，业务可接受 |
| `legacy_count` 长期不归零 | 用监控数据决策是否做后置批量迁移 |
| Kafka 消息乱序（partition key 由上游掌控、我方无法假设有序）| 业务流量低、乱序非硬伤；依赖补偿任务"从 wapi 取最新数据"幂等收敛兜底；切换前协调上游等我方 lag 归零（详见 8.2）|

待和团队对齐：

- [ ] 上游 portfolioNumber 字母前缀的具体取值范围（确认是否仅大写、是否单字符）
- [ ] 通知服务当前的幂等保证强度
- [ ] 兼容代码下线的具体时间（需业务方拍板"上游切换后 N 周"的 N 值）
- [ ] 切换时点是否能与上游协调到"我方消费 lag 归零"再切换

## 12. 实施步骤概览（给 writing-plans 阶段参考）

1. 加监控埋点（先于功能改动，便于上线后立刻观察）
2. 抽 MessageAdapter 和 UnifiedMessage（仅做 NEW 格式透传，LEGACY 分支保留旧路径调用）
3. 实现懒迁移 fallback 路径（含单测、集成测试）
4. 拆 templateId 派发为三个 handler
5. reminder 路径切换为不落表，仅触发通知
6. 补偿任务改造为从 content 解析派发
7. 全链路集成测试（含切换瞬时编排）
8. 灰度发布 → 监控 → 全量
9. （T2 之后）下线兼容代码

详细的步骤拆解、依赖关系、人天估算由后续 implementation plan 完成。

