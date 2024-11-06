package com.robertciotoiu.service;

import com.robertciotoiu.CarCategoryParsableUrlExtractor;
import com.robertciotoiu.connection.JsoupWrapper;
import com.robertciotoiu.cooldown.service.CategoryCooldownHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScraperService {
    private static final Logger logger = LogManager.getLogger(ScraperService.class);
    @Autowired
    JsoupWrapper jsoupWrapper;
    @Autowired
    ListingService listingService;
    @Autowired
    CarCategoryMetadataService carCategoryMetadataService;
    @Autowired
    CategoryCooldownHandler categoryCooldownHandler;
    @Autowired
    CarCategoryParsableUrlExtractor carCategoryParsableUrlExtractor;


    public void scrape(String carSpecPageUrl) {
        try {
            if (isScrapable(carSpecPageUrl)) {
                var carSpecPage = jsoupWrapper.getHtml(carSpecPageUrl);
                scrape(carSpecPage, carSpecPageUrl);
                setCooldown(carSpecPage, carSpecPageUrl);
            }
        } catch (IOException e) {
            logger.warn("Error accessing CarSpec URL to extract listings. URL: {}. Exception: {}", carSpecPageUrl, e);
        }
    }

    private void scrape(Document carSpecPage, String carSpecPageUrl) {
        listingService.scrapeAndIngestListings(carSpecPage, carSpecPageUrl);
        carCategoryMetadataService.scrapeAndIngestCarCategoryMetadata(carSpecPage, carSpecPageUrl);
    }

    private void setCooldown(Document carSpecPage,String carSpecPageUrl) {
        var carCategoryUrls = carCategoryParsableUrlExtractor.extractParsableUrls(carSpecPageUrl, carSpecPage);
        categoryCooldownHandler.calculateAndSetCooldown(carCategoryUrls);
    }

    private boolean isScrapable(String carSpecPageUrl) {
        return !carCategoryMetadataService.isFirstPage(carSpecPageUrl) || !categoryCooldownHandler.hasCooldown(carSpecPageUrl);
    }
}
