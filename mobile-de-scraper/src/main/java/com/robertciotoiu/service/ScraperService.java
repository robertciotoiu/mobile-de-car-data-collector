package com.robertciotoiu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScraperService {
    @Autowired
    ListingService listingService;
    @Autowired
    CarCategoryMetadataService carCategoryMetadataService;

    public void scrape(String carSpecPageUrl) {
        listingService.scrapeAndIngestListings(carSpecPageUrl);
        carCategoryMetadataService.scrapeAndIngestCarCategoryMetadata(carSpecPageUrl);
    }
}
