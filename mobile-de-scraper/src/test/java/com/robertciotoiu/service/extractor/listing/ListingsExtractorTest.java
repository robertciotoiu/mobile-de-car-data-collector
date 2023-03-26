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
import java.time.LocalDateTime;

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
    void testExtractRegMilPow() throws IOException {
        File in = new File("src/test/resources/special-listings/regMilPow-few-fields.html");
        var doc = Jsoup.parse(in, null);

        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");
        var extractedMileage = listings.stream().filter(listing -> listing.getListingId().equals("363980464")).findFirst().get().getRegMilPow().getMileage();
        var extractedRegistrationDate = listings.stream().filter(listing -> listing.getListingId().equals("363980464")).findFirst().get().getRegMilPow().getRegistrationDate();

        Assertions.assertEquals(999999, extractedMileage);
        Assertions.assertEquals("1979-05", extractedRegistrationDate);
    }

    @Test
    void testMissingFuelTypeOrGearboxType() throws IOException{
        File in = new File("src/test/resources/special-listings/fuelTypeOrGearboxType.html");
        var doc = Jsoup.parse(in, null);
        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");
        var rawTBPString = listings.stream().filter(listing -> listing.getListingId().equals("363996834")).findFirst().get().getTypeStatusFuelGearboxHuDoors().getTypeFuelGearboxHuDoors().getRawTBPString();
        Assertions.assertEquals("Small Car, Damaged, Automatic transmission, HU 03/2023",rawTBPString);
    }

    @Test
    void testAdsPostedDate() throws IOException{
        File in = new File("src/test/resources/special-listings/adsPostedDate.html");
        var doc = Jsoup.parse(in, null);
        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");
        var adPostedDate = listings.stream().filter(listing -> listing.getListingId().equals("363995931")).findFirst().get().getPostedDate();
        Assertions.assertEquals(LocalDateTime.parse("2023-03-26T14:53"), adPostedDate);
    }

    @Test
    void testPrice() throws IOException{
        File in = new File("src/test/resources/special-listings/price.html");
        var doc = Jsoup.parse(in, null);
        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");
        var adPostedDate = listings.stream().filter(listing -> listing.getListingId().equals("363981116")).findFirst().get().getPostedDate();
        Assertions.assertEquals(LocalDateTime.parse("2023-03-26T10:49"), adPostedDate);
    }

    @Test
    @Disabled("Used only for manual review of extracted listings")
    void testListingExtraction() throws IOException {
        File in = new File("src/test/resources/listings-03202023.html");
        var doc = Jsoup.parse(in, null);
        var listings = listingsExtractor.extract(doc, "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en");
        listings.forEach(System.out::println);
        Assertions.assertEquals(21, listings.size());
    }
}
