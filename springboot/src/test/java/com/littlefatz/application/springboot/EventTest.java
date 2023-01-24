package com.littlefatz.application.springboot;

import com.littlefatz.application.springboot.event.EventTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EventTest {

    @Autowired
    private EventTestService eventTestService;

    @Test
    public void testSendEvent() {
        eventTestService.sendEvent();
    }
}
