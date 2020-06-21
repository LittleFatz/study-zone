package com.littlefatz.spring.statemachine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {OrderStateMachineConfig.class, OrderPersistHandlerConfig.class})
public class OrderStateMachineTest {
//
//    @Autowired
//    private OrderStateMachineConfig stateMachine;

    @Autowired
    private PersistStateMachineHandler persistStateMachineHandler;


    @Autowired
    private StateMachine<String, String> stateMachine;

//    @Before
//    public void setUp() {
//        stateMachine.start();
//    }

    @Test
    public void testHello() {
        System.out.println("hello");
    }

    @Test
    public void testStateMachine() {
        Order order = new Order();
        order.setName("test");
        order.setValue(20);
        order.setStatus(OrderStatus.SUMMITED);


        boolean flag = persistStateMachineHandler.handleEventWithState(
                MessageBuilder.withPayload("initial").setHeader(Constant.entityHeader, order).build(),
                "start");

//        persistStateMachineHandler.getStateMachine();

//        boolean flag = persistStateMachineHandler.handleEventWithState(
//                MessageBuilder.withPayload("initial").setHeader(Constant.entityHeader, order).build(),
//                "start");
        System.out.println(flag);

//        throwError();

        System.out.println("==========================================");
//        boolean flag2 = persistStateMachineHandler.handleEventWithState(
//                MessageBuilder.withPayload("activate").setHeader(Constant.entityHeader, order).build(),
//                "init");
//        System.out.println(flag2);

//        boolean flag = stateMachine.sendEvent(OrderEvent.PAYED.name());
//        System.out.println(flag);
//        boolean flag2 = stateMachine.sendEvent(OrderEvent.DELIVERED.name());
//        System.out.println(flag2);

    }

    @After
    public void tearDown() {
        stateMachine.stop();
    }
}
