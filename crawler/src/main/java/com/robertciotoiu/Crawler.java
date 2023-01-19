package com.robertciotoiu;

import com.robertciotoiu.service.CarSpecSitemapUrlExtractor;
import com.robertciotoiu.service.CarSpecUrlsExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Crawler {
    private static final Logger logger = LogManager.getLogger(Crawler.class);


    public static void main(String[] args) {
        var carSpecUrls = getCarSpecificationUrls();
        scrapeAndIngestListings(carSpecUrls);
    }

    private static void scrapeAndIngestListings(List<String> carSpecUrls) {
        for (var carSpecFirstPageUrl : carSpecUrls) {
            var carSpecPageUrls = CarSpecUrlsExtractor.getUrls(carSpecFirstPageUrl);

            //TODO: put all of them in RabbitMQ
        }
    }

    private static List<String> getCarSpecificationUrls() {
        try {
            return CarSpecSitemapUrlExtractor.extract();
        } catch (IOException e) {
            logger.error("Cannot connect to mobile.de sitemap", e);
        }
        return new ArrayList<>();
    }
}
