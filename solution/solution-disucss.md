# 项目背景
/superpowers:brainstorming 目前我有一个springboot系统，会消费上游系统发送的kafka
message，如果是数据库中没有找到对应的数据，那么说明是第一次收到mesage，那么我的系统会进行create
record操作，如果是已经存在，那么进行update操作。
由于上游系统升级改造，这个kafka message会有两个影响我们系统的改动：
1. message body已有的一个属性 templateId会新增两种类型：update和reminder。原templateId只有一种类型initial，但是这种类型
兼顾initial和update，也就是前面说的create record和update record。这个改动只是把template类型细化成多种类型
2. message body另一个已有属性portfolioNumber,格式会从“3477234”这种纯数字的格式，转换为"S-12313-0"这种格式。纯数字歌格式
的portfolioNumber实际上是上游系统portfolioNumber在数据库记录中的id，我们暂且称它为portfolioId。以前的工作流程中，我们需
要使用纯数字格式的porfolioId,作为request的一部分调用一个wapi API，来获取"S-12313-0"这种格式的porfolioNumber.上游的这个
改动是为了减少我们额外多调用一次api，但是却会影响我们系统已有的主流程。
请你根据上面提供的改动点信息，分析我们系统在改造的时候需要注意什么？
由于上面讨论的系统方案是我们公司的内部项目，因此你在当前文件夹是找不到对应代码，只是作为方案讨论，如何生成的文件请放置
到 solution文件夹中






# 追加信息
corporate_action_message这张表中，虽然我存的值是portfolioId，但是为了对应上游kafka
  message的portfolioNumber，因此该表中的属性名依然是portfolioNumber。
  由于这张表主要用于补偿任务，我的想法是使用同一个字段保存portfolioId和portfolioNumber.
  这里还需要补充一个细节，这两张corporate_action_message和corporate_action，他们的唯一主键是orderNumber +
  portfolioNumber,其中orderNumber也是kafka message body中一个属性。同一个orderNumber下面，会有多个portfolioNumber，一个
  portfolioNumber关联着一个唯一的cin，cin可以认为是客户唯一的id。
  在消费kafka
  message先根据格式判断是portfolioId还是portfolioNumber,如果是portfolioId，证明上游系统还未切换，直接走旧流程。
  如果是portfolioNumber，那么需要先根据orderNumber+portfolioNumber查询是否已经有数据，如果有证明这条数据是上游系统切换
  后才生成的，那么直接执行后续操作。如果找不到，则有可能是以前是以portfolioId保存在该表中，因此需要先使用orderNumber和c
  in查询所有数据，并且使用portfolioId查询wapi api得到portfolio number，逐一比对找到符合的记录，最后更新数据


  # spec使用
  步骤 2：先让 Claude 做一次"spec ↔ 实际代码"的对齐扫描

  不要直接让它写实施计划，先让它告诉你 spec 里的假设跟代码现状有没有差距。一个有效的 prompt：

  ▎ 请读 docs/specs/2026-05-04-kafka-message-upgrade-design.md 这份 spec，然后扫描代码库，重点看：
  ▎ 1. Kafka consumer 入口在哪里？目前有没有类似 MessageAdapter 的抽象层？
  ▎ 2. corporate_action_message 和 corporate_action 表的实际 entity / mapper 在哪些文件？
  ▎ 3. 补偿任务的具体实现位置和触发方式
  ▎ 4. wapi 调用的封装位置
  ▎ 5. 通知服务的接口在哪
  ▎
  ▎ 列出 spec 里的假设和实际代码的差距，不要写实施计划，只做现状摸底报告。

  这一步能帮你发现 spec 里那些"想当然"的部分（比如 spec 假设有个 MessageProcessor 类，实际代码可能根本不长这样）。

  步骤 3：让 Claude 调用 writing-plans skill 生成实施计划

  如果公司电脑也装了 superpowers plugin：

  ▎ 基于这份 spec 和上一步的现状摸底报告，使用 /superpowers:writing-plans 生成详细实施计划。

  如果没装：

  ▎ 基于这份 spec 和现状摸底报告，生成一份分步实施计划，包含：每步要改哪些文件、依赖关系、验收标准、回滚点。