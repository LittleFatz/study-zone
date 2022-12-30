package com.littlefatz.application.spring.statemachine;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

@Configuration
@ComponentScan(basePackages = "com.littlefatz.spring.statemachine")
public class OrderPersistHandlerConfig {

    @Autowired
    private StateMachine<String, String> orderStateMachineConfig;

    @Autowired
    private OrderPersistStateChangeListener orderPersistStateChangeListener;

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        PersistStateMachineHandler handler = new PersistStateMachineHandler(orderStateMachineConfig);
        handler.addPersistStateChangeListener(orderPersistStateChangeListener);
        return handler;
    }

}
