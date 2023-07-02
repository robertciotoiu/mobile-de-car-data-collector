package com.robertciotoiu.service.extractor.listing;

import com.robertciotoiu.data.model.raw.Listing;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListingsExtractor {
    private static final String LISTING_CLASS_NAME_XPATH = "//div[contains(@class,'cBox-body--resultitem')]";
    private static final String TOP_LISTING_CLASS_NAME = "//div[contains(@class,'cBox-body--topResultitem')]";
    private static final String AD_LISTING_CLASS_NAME = "//div[contains(@class,'cBox-body--eyeCatcher')]";
    @Autowired
    ListingElementExtractor listingElementExtractor;

    public List<Listing> extract(Document carSpecPage, String carSpecPageUrl) {
        var listings = new ArrayList<Listing>();

        var normalListings = extractListings(carSpecPage, carSpecPageUrl, LISTING_CLASS_NAME_XPATH);
        var topListings = extractListings(carSpecPage, carSpecPageUrl, TOP_LISTING_CLASS_NAME);
        var adListings = extractListings(carSpecPage, carSpecPageUrl, AD_LISTING_CLASS_NAME);

        listings.addAll(normalListings);
        listings.addAll(topListings);
        listings.addAll(adListings);

        return listings;
    }

    private List<Listing> extractListings(Document carSpecPage, String carSpecPageUrl, String xpath) {
        var listings = new ArrayList<Listing>();
        var listingsElements = carSpecPage.selectXpath(xpath);
        var carCategory = extractCarCategory(carSpecPageUrl);

        for (var listingElement : listingsElements) {
            var listing = extractListing(listingElement);
            listing.setCategory(carCategory);
            listing.setScrapeTime(LocalDateTime.now(ZoneOffset.UTC));
            listings.add(listing);
        }

        return listings;
    }

    private String extractCarCategory(String carSpecPageUrl) {
        var beginIndex = carSpecPageUrl.lastIndexOf("/") + 1;
        var endIndex = carSpecPageUrl.indexOf(".html?");
        return carSpecPageUrl.substring(beginIndex, endIndex);
    }

    private Listing extractListing(Element listingElement) {
        return listingElementExtractor.extract(listingElement);
    }
}
