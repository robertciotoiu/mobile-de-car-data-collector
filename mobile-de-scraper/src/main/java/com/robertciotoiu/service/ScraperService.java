package com.robertciotoiu.service;

import com.robertciotoiu.data.ListingPersistor;
import com.robertciotoiu.service.extractor.ListingsExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScraperService {

    @Autowired
    ListingsExtractor listingsExtractor;

    public void scrapeAndIngestListings(String carSpecPageUrl) {
        var listings = listingsExtractor.extract(carSpecPageUrl);
        ListingPersistor.persist(listings);
    }

    public void scrapeAndIngestListings(List<String> carSpecPageUrls) {
        for (var carSpecPageUrl : carSpecPageUrls) {
            scrapeAndIngestListings(carSpecPageUrl);
        }
    }
}
