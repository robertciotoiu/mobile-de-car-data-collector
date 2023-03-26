package com.robertciotoiu.service.extractor.listing;

import com.robertciotoiu.exception.ListingIdNotFoundError;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class ListingsExtractorTest {

    @Autowired
    ListingsExtractor listingsExtractor;

    @Test
    void extractListingsWithMultipleAdsTest() throws IOException {
        File in = new File("src/test/resources/listings-with-multiple-ads.html");
        Document doc = Jsoup.parse(in, null);

        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");

        Assertions.assertNotNull(listings);
        Assertions.assertEquals(21, listings.size());
    }

    @Test
    void extractListingWithMissingId_ShouldThrowError() throws IOException {
        File in = new File("src/test/resources/listings-missing-listingId.html");
        Document doc = Jsoup.parse(in, null);

        Assertions.assertThrows(ListingIdNotFoundError.class, () -> listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en"));
    }


    @Test
    @Disabled("Used only for manual review of extracted listings")
    void testListingExtraction() throws IOException {
        File in = new File("src/test/resources/listings-03202023.html");
        var doc = Jsoup.parse(in, null);

        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");

        listings.forEach(System.out::println);
    }
}
