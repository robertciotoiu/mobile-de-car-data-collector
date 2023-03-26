package com.robertciotoiu;

import com.rabbitmq.client.Channel;
import com.robertciotoiu.exception.ListingExtractionException;
import com.robertciotoiu.service.ScraperService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class RabbitMQConsumer {
    private static final Logger logger = LogManager.getLogger(RabbitMQConsumer.class);
    @Autowired
    ScraperService scraperService;

    @RabbitListener(queues = "${rabbitmq.queue}", concurrency = "1")
    public void handleMessage(String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel) throws IOException {
        try {
            logger.info("Received message: {}", message);
            scraperService.scrape(message);
        } catch (
                Exception e) {
            // handle any exceptions
            logger.error("Exception caught while processing message: {}", e.getMessage());
            // reject and requeue the message
            channel.basicReject(deliveryTag, true);
            throw new ListingExtractionException("Stopping program execution... Check and solve the parsing exception.");
        } finally {
            // Acknowledge the message to remove it from the queue
            channel.basicAck(deliveryTag, false);
        }
    }

}