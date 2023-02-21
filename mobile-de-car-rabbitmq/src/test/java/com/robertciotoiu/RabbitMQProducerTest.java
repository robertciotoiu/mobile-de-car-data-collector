package com.robertciotoiu;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RabbitMQProducerTest {
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private RabbitTemplate rabbitTemplate;

@Value("${rabbitmq.queue}")
private String rabbitmqQueueName;


    @Test
    void testProducerAndConsumeMessage() {
        String message = "testMessage";
        rabbitMQProducer.publishMessageToRabbitMQ(message);

        Message receivedMessage = rabbitTemplate.receive(rabbitmqQueueName);
        assertNotNull(receivedMessage);
        assertEquals(message, new String(receivedMessage.getBody()).replace("\"",""));
    }

    @SpringBootApplication
    static class IntegrationTestConfig {}
}
