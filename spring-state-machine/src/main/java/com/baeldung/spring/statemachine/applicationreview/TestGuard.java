package com.baeldung.spring.statemachine.applicationreview;

import org.springframework.core.annotation.Order;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

@Component
public class TestGuard implements Guard<OrderStates, OrderEvents> {

    @Override
    public boolean evaluate(StateContext<OrderStates, OrderEvents> stateContext) {

        OrderEvents event = stateContext.getEvent();
        State<OrderStates, OrderEvents> source = stateContext.getSource();
        State<OrderStates, OrderEvents> target = stateContext.getTarget();
        Transition<OrderStates, OrderEvents> transition = stateContext.getTransition();
        if (event == OrderEvents.PAY) {
            System.out.println("heres-----");
            return true;
        }

        return false;
    }
}
