package com.littlefatz.application.springboot.event;

import org.springframework.context.ApplicationEvent;


public class NotifyEvent extends ApplicationEvent {

    private String messageId;

    private String message;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public NotifyEvent(Object source, String messageId, String message) {
        super(source);

        this.messageId = messageId;
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
