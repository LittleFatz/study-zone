package com.littlefatz.application.spring.statemachine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.*;

@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    @Autowired
    private OrderAction action;

    @Autowired
    private OrderGuard guard;

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
//        Set<String> stringStates = new HashSet<>();
//        EnumSet.allOf(OrderStatus.class).forEach(entity -> stringStates.add(entity.name()));
//
//        states.withStates().initial(OrderStatus.SUMMITED.name()).end(OrderStatus.COMPLETED.name()).states(stringStates);
        states.withStates().initial("start").states(new HashSet<>(Arrays.asList("start", "init", "active", "inactive")));


    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
//        transitions.withExternal().source(OrderStatus.SUMMITED.name()).target(OrderStatus.PAYED.name()).event(OrderEvent.PAYED.name())
//                .guard(testGuard()).and()
//                .withExternal().source(OrderStatus.PAYED.name()).target(OrderStatus.DELIVERED.name()).event(OrderEvent.DELIVERED.name()).and()
//                .withExternal().source(OrderStatus.DELIVERED.name()).target(OrderStatus.COMPLETED.name()).event(OrderEvent.RECEIVED.name());
//
        transitions.withExternal().source("start").target("init").event("initial").guard(guard).action(action).and()
                .withExternal().source("init").target("active").event("activate").and()
                .withExternal().source("active").target("inactive").event("deactivate").and()
                .withExternal().source("start").target("active").event("activate").guard(guard).and()
                .withExternal().source("inactive").target("init").event("initial").guard(guard).and()
                .withExternal().source("inactive").target("active").event("activate").guard(guard).and()
                .withInternal().source("init").event("initial").and()
                .withInternal().source("active").event("activate").and()
                .withInternal().source("inactive").event("deactivate");

    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration().autoStartup(true).listener(listener());
    }

    @Bean
    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                System.out.println("State change to " + to.getId());
            }

            @Override
            public void eventNotAccepted(Message<String> event) {
                System.out.println("event not accpted");
            }

            @Override
            public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
                System.out.println("found error");


                // do something with error
            }
        };
    }

    private Guard<String, String> testGuard() {
        return context -> {
            System.out.println("~~~~~~~~~~~guard");
            return true;
        };
    }
}
