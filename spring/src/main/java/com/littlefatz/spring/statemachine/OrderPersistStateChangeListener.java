package com.littlefatz.spring.statemachine;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class OrderPersistStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {
    @Override
    public void onPersist(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine) {

//        if (stateMachine.hasStateMachineError()) {
//            System.out.println("got error in action");
//            throw new RuntimeException("rollback");
//        }


        if (message != null && message.getHeaders().containsKey(Constant.entityHeader)) {
            Order entity = message.getHeaders().get(Constant.entityHeader, Order.class);
            System.out.println(entity.getValue());
        }

        System.out.println("~~~~~~~~~~~~~ Persist state successful:" + state.getId());
    }
}
