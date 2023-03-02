package com.robertciotoiu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMQProducer {
    private static final Logger logger = LogManager.getLogger(RabbitMQProducer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MessagePostProcessor messagePostProcessor;
    @Value("${rabbitmq.exchange}")
    private String rabbitmqExchange;
    @Value("${rabbitmq.routingkey}")
    private String rabbitmqRoutingkey;

    public void publishMessagesToRabbitMQ(List<String> messages) {
        for (String message : messages) {
            publishMessageToRabbitMQ(message);
        }
        logger.info("Published {} messages in RabbitMQ for category", messages.size());
    }

    public void publishMessageToRabbitMQ(String message) {
        rabbitTemplate.convertAndSend(rabbitmqExchange, rabbitmqRoutingkey, message, messagePostProcessor);
        logger.debug("Message {} published to exchange: {} routing key: {}", message, rabbitmqExchange, rabbitmqRoutingkey);
    }
}

