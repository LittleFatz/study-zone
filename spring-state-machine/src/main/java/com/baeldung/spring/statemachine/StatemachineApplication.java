package com.baeldung.spring.statemachine;

import com.baeldung.spring.statemachine.applicationreview.OrderEvents;
import com.baeldung.spring.statemachine.applicationreview.OrderStates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

@SpringBootApplication
public class StatemachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatemachineApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(StateMachine<OrderStates, OrderEvents> stateMachine,
                                               StateMachineListenerAdapter<OrderStates, OrderEvents> listener) {
        return args -> {
            stateMachine.addStateListener(listener);  // Ensure the listener is added to the state machine
            stateMachine.start();
            stateMachine.sendEvent(OrderEvents.PAY);
            stateMachine.sendEvent(OrderEvents.FULFILL);
            stateMachine.stop();
        };
    }

}
