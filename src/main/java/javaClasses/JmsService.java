package javaClasses;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class JmsService {
    @JmsListener(destination = "admin.notifications")
    public void receiveNotification(String message) {
        System.out.println("Получено JMS сообщение: " + message);
    }
}
