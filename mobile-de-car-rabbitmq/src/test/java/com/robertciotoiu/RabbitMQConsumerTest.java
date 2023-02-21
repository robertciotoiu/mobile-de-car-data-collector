package com.robertciotoiu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RabbitMQConsumerTest {
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.queue}")
    private String rabbitmqQueueName;

    @Test
    void testProducerAndConsumeMessage() {
        String message = "testMessage";
        Assertions.assertDoesNotThrow(() -> rabbitMQProducer.publishMessageToRabbitMQ(message));
    }

    @SpringBootApplication
    static class IntegrationTestConfig {
    }
}
