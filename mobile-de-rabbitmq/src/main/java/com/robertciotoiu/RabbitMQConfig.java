package com.robertciotoiu;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {
    @Value("${SPRING_RABBITMQ_HOST}")
    private String rabbitmqHost;
    @Value("${SPRING_RABBITMQ_PORT}")
    private int rabbitmqPort;
    @Value("${SPRING_RABBITMQ_USERNAME}")
    private String rabbitmqUsername;
    @Value("${SPRING_RABBITMQ_PASSWORD}")
    private String rabbitmqPassword;
    @Value("${rabbitmq.queue}")
    private String rabbitmqQueueName;
    @Value("${rabbitmq.exchange}")
    private String rabbitmqExchange;
    @Value("${rabbitmq.routingkey}")
    private String rabbitmqRoutingkey;
    @Value("${rabbitmq.concurrent.consumers}")
    private Integer concurrentConsumers;
    @Value("${rabbitmq.max.concurrent.consumers}")
    private Integer maxConcurrentConsumers;
    @Value("${rabbitmq.prefetch.count}")
    private Integer prefetchCount;

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitmqRoutingkey);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(rabbitmqExchange);
    }

    @Bean
    public Queue queue() {
        return new Queue(rabbitmqQueueName);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        connectionFactory.setUsername(rabbitmqUsername);
        connectionFactory.setPassword(rabbitmqPassword);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setExchange(rabbitmqExchange);
        rabbitTemplate.setRoutingKey(rabbitmqRoutingkey);
        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        // number of consumer threads
        factory.setConcurrentConsumers(concurrentConsumers);
        // max number of consumer threads
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        // number of unacknowledged messages per consumer
        factory.setPrefetchCount(prefetchCount);
        // use manual acknowledgement
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public MessagePostProcessor messagePostProcessor() {
        return message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        };
    }
}