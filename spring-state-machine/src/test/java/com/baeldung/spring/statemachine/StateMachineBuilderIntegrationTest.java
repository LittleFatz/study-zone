//package com.baeldung.spring.statemachine;
//
//import org.junit.Test;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.StateMachineBuilder;
//
//import static org.junit.Assert.assertEquals;
//
//public class StateMachineBuilderIntegrationTest {
//
//    @Test
//
//            builder.configureTransitions()
//            .
//
//    withExternal()
//                .
//
//    source("SI").tar
//
//    public void whenUseStateMachineBuilder_thenBuildSuccessAndMachineWorks() throws Exception {
//        StateMachineBuilder.Builder<String, String> builder = StateMachineBuilder.builder();
//        builder.configureStates().withStates()
//                .initial("SI")
//                .state("S1")
//                .end("SF");
//        get("S1").event("E1")
//                .and().withExternal()
//                .source("S1").target("SF").event("E2");
//
//        StateMachine machine = builder.build();
//
//        machine.start();
//
//        machine.sendEvent("E1");
//        assertEquals("S1", machine.getState().getId());
//
//        machine.sendEvent("E2");
//        assertEquals("SF", machine.getState().getId());
//    }
//}
