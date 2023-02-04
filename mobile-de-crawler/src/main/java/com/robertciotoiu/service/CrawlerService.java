package com.robertciotoiu.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Scheduled(fixedDelayString = "${scheduler.fixed-delay}", timeUnit = TimeUnit.SECONDS)
    private static void crawl() {
        logger.info("Start new crawling");
        var carCategoryBaseUrl = getCarCategoryBaseUrl();
        var parsableUrls = extractParsableUrls(carCategoryBaseUrl);
        postUrlsToMQ(parsableUrls);
    }

    private static void postUrlsToMQ(List<String> parsableUrls) {
        //TODO: implement
        logger.info(parsableUrls);
    }

    private static List<String> extractParsableUrls(List<String> carCategoryBaseUrls) {
        var parsableUrls = new ArrayList<String>();
        for (var carCategoryBaseUrl : carCategoryBaseUrls) {
            var carCategoryParsableUrls = CarCategoryParsableUrlExtractor.getUrls(carCategoryBaseUrl);
            parsableUrls.addAll(carCategoryParsableUrls);
        }
        return parsableUrls;
    }

    private static List<String> getCarCategoryBaseUrl() {
        try {
            return CarCategoryBaseUrlExtractor.extract(URL);
        } catch (IOException e) {
            logger.error("Cannot connect to mobile.de sitemap", e);
        }
        return new ArrayList<>();
    }
}
