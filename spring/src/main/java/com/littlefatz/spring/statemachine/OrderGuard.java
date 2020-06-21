package com.littlefatz.spring.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class OrderGuard implements Guard<String, String> {
    @Override
    public boolean evaluate(StateContext<String, String> stateContext) {
        Order order = (Order) stateContext.getMessageHeader(Constant.entityHeader);
//        stateContext.get
        System.out.println("~~~~~~~~~~guard:" + order.getName());
        if (order.getValue() < 100){
            System.out.println("too cheap");
//            throw new RuntimeException("guard");
//            stateContext.getStateMachine().sendEvent("error");

            return false;
        } else {
            return true;
        }


    }
}
