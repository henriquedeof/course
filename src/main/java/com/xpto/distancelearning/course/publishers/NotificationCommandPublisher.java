package com.xpto.distancelearning.course.publishers;

import com.xpto.distancelearning.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value(value = "${dl.broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;

    @Value(value = "${dl.broker.key.notificationCommandKey}")
    private String notificationCommandKey;

    public void publishNotificationCommand(NotificationCommandDto notificationCommandDto) {
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDto);
    }
}