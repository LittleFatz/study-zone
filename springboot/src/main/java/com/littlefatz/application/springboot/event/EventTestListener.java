package com.littlefatz.application.springboot.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventTestListener {

    @EventListener(NotifyEvent.class)
    public void processNotifyEvent(NotifyEvent event) {

        System.out.println("start trigger event~~~~~~~");
        System.out.println(event.getMessage());
        System.out.println(event.getMessageId());
        System.out.println("end trigger event~~~~~~~");
    }
}
