package com.littlefatz.application.springboot.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventTestService {

    @Autowired
    private ApplicationEventPublisher publisher;

    public void sendEvent() {
        NotifyEvent event = new NotifyEvent(this, "testID", "test message");

        System.out.println("publish start");
        publisher.publishEvent(event);
        System.out.println("publish end");
    }
}
