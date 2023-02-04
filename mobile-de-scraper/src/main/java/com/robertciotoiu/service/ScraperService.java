package com.robertciotoiu.service;

import com.robertciotoiu.data.ListingPersistor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {

    public void scrape() {
        //TODO: take them from RabbitMQ
        var carSpecPageUrls = new ArrayList<String>();

        scrapeAndIngestListings(carSpecPageUrls);
    }

    private void scrapeAndIngestListings(List<String> carSpecPageUrls) {
        for (var carSpecPageUrl : carSpecPageUrls) {
            var listings = ListingsExtractor.extract(carSpecPageUrl);
            ListingPersistor.persist(listings);
        }
    }
}
