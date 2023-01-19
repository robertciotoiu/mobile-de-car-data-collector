package com.robertciotoiu;

import com.robertciotoiu.data.ListingPersistor;
import com.robertciotoiu.service.ListingsExtractor;

import java.util.ArrayList;
import java.util.List;

public class Scraper {

    public static void main(String[] args){
        //TODO: take them from RabbitMQ
        var carSpecPageUrls = new ArrayList<String>();

        scrapeAndIngestListings(carSpecPageUrls);
    }

    private static void scrapeAndIngestListings(List<String> carSpecPageUrls) {
        for (var carSpecPageUrl : carSpecPageUrls) {
            var listings = ListingsExtractor.extract(carSpecPageUrl);
            ListingPersistor.persist(listings);
        }
    }
}
