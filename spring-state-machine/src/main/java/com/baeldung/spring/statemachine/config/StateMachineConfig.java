package com.baeldung.spring.statemachine.config;

import com.baeldung.spring.statemachine.applicationreview.OrderEvents;
import com.baeldung.spring.statemachine.applicationreview.OrderStates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStates.UNPAID)
                .state(OrderStates.UNPAID)
                .state(OrderStates.PAID)
                .state(OrderStates.FULFILLED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.UNPAID).target(OrderStates.PAID).event(OrderEvents.PAY)
                .and()
                .withExternal()
                .source(OrderStates.PAID).target(OrderStates.FULFILLED).event(OrderEvents.FULFILL);
    }

    @Bean
    public StateMachineListener<OrderStates, OrderEvents> stateMachineListener() {
        return new StateMachineListenerAdapter<OrderStates, OrderEvents>() {
            @Override
            public void stateChanged(State<OrderStates, OrderEvents> from, State<OrderStates, OrderEvents> to) {
                System.out.println("State changed from " + (from != null ? from.getId() : "none") + " to " + to.getId());
            }
        };
    }
}

