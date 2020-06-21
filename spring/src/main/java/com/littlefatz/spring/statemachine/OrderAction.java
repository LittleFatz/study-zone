package com.littlefatz.spring.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class OrderAction implements Action<String, String> {

    @Override
    public void execute(StateContext<String, String> stateContext) {
        Order order = (Order) stateContext.getMessageHeader(Constant.entityHeader);
        System.out.println("~~~~~~~~~~action:" + order.getName());
        stateContext.getStateMachine()
                .setStateMachineError(new RuntimeException("fail to validate"));

//        stateContext.getStateMachine().sendEvent("error");

    }
}
