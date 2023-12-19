package javaClasses;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageSender {

    private final JmsTemplate jmsTemplate;

    public JmsMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendNotification(String message) {
        jmsTemplate.convertAndSend("admin.notifications", message);
    }
}

