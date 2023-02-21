package com.robertciotoiu.service;

import com.robertciotoiu.exception.ListingIdNotFoundError;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ListingsExtractorTest {

    @Test
    void extractListingsWithMultipleAdsTest() throws IOException {
        File in = new File("src/test/resources/listings-with-multiple-ads.html");
        Document doc = Jsoup.parse(in, null);

        var listings = ListingsExtractor.extract(doc);

        Assertions.assertNotNull(listings);
        Assertions.assertEquals(21, listings.size());
    }

    @Test
    void extractListingWithMissingId_ShouldThrowError() throws IOException {
        File in = new File("src/test/resources/listings-missing-listingId.html");
        Document doc = Jsoup.parse(in, null);

        Assertions.assertThrows(ListingIdNotFoundError.class, () -> ListingsExtractor.extract(doc));
    }
}
