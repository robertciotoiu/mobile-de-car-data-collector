package com.robertciotoiu.service;

import com.robertciotoiu.data.model.raw.Listing;
import com.robertciotoiu.data.repository.ListingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListingPersistor {
    private static final Logger logger = LogManager.getLogger(ListingPersistor.class);

    @Autowired
    private ListingRepository repository;

    public void persist(List<Listing> listings) {
        int totalListings = listings.size();
        var savedListings = repository.saveAll(listings);

        List<Listing> failedListings = listings.stream()
                .filter(listing -> !savedListings.contains(listing))
                .toList();
        int totalFailedListings = failedListings.size();
        if (totalFailedListings > 0) {
            logger.error("{} out of {} objects failed to persist: {}", totalFailedListings, totalListings, failedListings);
        } else {
            logger.info("All {} listings persisted successfully.", totalListings);
        }
    }
}
