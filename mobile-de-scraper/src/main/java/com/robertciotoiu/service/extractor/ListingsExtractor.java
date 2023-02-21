package com.robertciotoiu.service.extractor;

import com.robertciotoiu.connection.JsoupWrapper;
import com.robertciotoiu.data.model.listing.Listing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ListingsExtractor {
    private static final Logger logger = LogManager.getLogger(ListingsExtractor.class);

    private static final String LISTING_CLASS_NAME_XPATH = "//div[contains(@class,'cBox-body--resultitem')]";
    private static final String TOP_LISTING_CLASS_NAME = "//div[contains(@class,'cBox-body--topResultitem')]";
    private static final String AD_LISTING_CLASS_NAME = "//div[contains(@class,'cBox-body--eyeCatcher')]";

    @Autowired
    JsoupWrapper jsoupWrapper;
    @Autowired
    ListingElementExtractor listingElementExtractor;

    public List<Listing> extract(String carSpecPageUrl) {
        var listings = new ArrayList<Listing>();

        try {
            var carSpecPage = jsoupWrapper.getHtml(carSpecPageUrl);
            listings.addAll(extract(carSpecPage));
        } catch (IOException e) {
            logger.warn("Error accessing CarSpec URL to extract listings. URL: {}. Exception: {}", carSpecPageUrl, e);
        }

        return listings;
    }

    public List<Listing> extract(Document carSpecPage) {
        var listings = new ArrayList<Listing>();

        var normalListings = extractListings(carSpecPage, LISTING_CLASS_NAME_XPATH);
        var topListings = extractListings(carSpecPage, TOP_LISTING_CLASS_NAME);
        var adListings = extractListings(carSpecPage, AD_LISTING_CLASS_NAME);

        listings.addAll(normalListings);
        listings.addAll(topListings);
        listings.addAll(adListings);

        return listings;
    }

    private List<Listing> extractListings(Document carSpecPage, String xpath) {
        var listings = new ArrayList<Listing>();
        var listingsElements = carSpecPage.selectXpath(xpath);

        for (var listingElement : listingsElements) {
            var listing = extractListing(listingElement);
            listings.add(listing);
        }

        return listings;
    }

    private Listing extractListing(Element listingElement) {
        return listingElementExtractor.extract(listingElement);
    }
}
