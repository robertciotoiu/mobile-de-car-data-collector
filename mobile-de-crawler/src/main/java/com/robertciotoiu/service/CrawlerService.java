package com.robertciotoiu.service;

import com.robertciotoiu.RabbitMQProducer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerService.class);
    private static final String URL = "https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml";
    @Autowired
    private CarCategoryBaseUrlExtractor carCategoryBaseUrlExtractor;
    @Autowired
    private CarCategoryParsableUrlExtractor carCategoryParsableUrlExtractor;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Scheduled(fixedDelayString = "${scheduler.fixed-delay}", timeUnit = TimeUnit.MINUTES)
    private void crawl() {
        logger.info("Start new crawling");
        var carCategoryBaseUrls = getCarCategoryBaseUrl();
        extractAndPublishParsableUrls(carCategoryBaseUrls);
    }

    private void extractAndPublishParsableUrls(List<String> carCategoryBaseUrls) {
        carCategoryBaseUrls.forEach(carCategoryBaseUrl -> {
            var carCategoryParsableUrls = carCategoryParsableUrlExtractor.extractParsableUrls(carCategoryBaseUrl);
            rabbitMQProducer.publishMessagesToRabbitMQ(carCategoryParsableUrls);
        });
    }

    private List<String> getCarCategoryBaseUrl() {
        try {
            return carCategoryBaseUrlExtractor.extract(URL);
        } catch (IOException e) {
            logger.error("Cannot connect to mobile.de sitemap", e);
        }
        return new ArrayList<>();
    }
}
