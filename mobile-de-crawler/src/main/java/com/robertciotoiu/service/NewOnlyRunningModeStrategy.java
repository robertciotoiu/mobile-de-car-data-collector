package com.robertciotoiu.service;

import com.robertciotoiu.RabbitMQProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class NewOnlyRunningModeStrategy implements RunningModeStrategy {
    private static final Logger logger = LogManager.getLogger(NewOnlyRunningModeStrategy.class);
    @Autowired
    private CarCategoryBaseUrlExtractor carCategoryBaseUrlExtractor;
    @Autowired
    private CarCategoryParsableUrlExtractor carCategoryParsableUrlExtractor;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Override
    public void execute(String url) {
        try {
            var carCategoryBaseUrls = carCategoryBaseUrlExtractor.extract(url);
            rabbitMQProducer.publishMessagesToRabbitMQ(carCategoryBaseUrls);
        } catch (IOException e) {
            logger.error("Cannot connect to mobile.de sitemap", e);
        }
    }
}
