package com.robertciotoiu.service;

import com.robertciotoiu.service.extractor.listing.ListingsExtractor;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ListingService {
    @Autowired
    ListingsExtractor listingsExtractor;

    @Autowired
    ListingPersistor listingPersistor;

    public void scrapeAndIngestListings(Document carSpecPage, String carSpecPageUrl) {
        var listings = listingsExtractor.extract(carSpecPage, carSpecPageUrl);
        listingPersistor.persist(listings);
    }
}
