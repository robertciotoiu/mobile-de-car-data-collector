package com.robertciotoiu.service.extractor.listing;

import com.robertciotoiu.data.model.raw.*;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ListingElementExtractorTest {
    private static final String LISTING_XPATH = "/html/body/div";
    @Autowired
    ListingElementExtractor listingElementExtractor;

    @Test
    void testExtractPrivateWithoutVehicleCondition() throws IOException {
        File in = new File("src/test/resources/listing-types/listing_private.html");
        var doc = Jsoup.parse(in, null).selectXpath(LISTING_XPATH).first();

        Assertions.assertNotNull(doc, "Check the input file!");

        var listing = listingElementExtractor.extract(doc);

        Assertions.assertEquals("Mazda Mazda Demio 5 Türer Klima 06/2023 HU/AU Be...", listing.getTitle());
    }

    @Test
    void testExtractPrivateWithVehicleCondition() throws IOException {
        File in = new File("src/test/resources/listing-types/listing_dealer_accident-free_no_extras.html");
        var doc = Jsoup.parse(in, null).selectXpath(LISTING_XPATH).first();

        Assertions.assertNotNull(doc, "Check the input file!");

        var expectedListing = Listing.builder()
                .listingId("358886142")
                .title("Volkswagen Caddy PKW Alltrack AHK+DSG+Xenon+Navi+Kamera")
                .url("https://suchen.mobile.de/fahrzeuge/details.html?id=358886142&cn=DE&damageUnrepaired=ALSO_DAMAGE_UNREPAIRED&isSearchRequest=true&makeModelVariant1.makeId=25200&makeModelVariant1.modelDescription=alltrack&makeModelVariant1.modelId=9&pageNumber=2&scopeId=C&sortOption.sortBy=creationTime&sortOption.sortOrder=DESCENDING&searchId=997548af-ab0f-4a55-da5d-31eb30464532&ref=srp")
                .imgUrl("https://img.classistatic.de/api/v1/mo-prod/images/07/07a7928b-d21d-45b3-8dc5-e250ee862a57?rule=mo-160.jpg")
                .isNew(false)
                .isElectric(false)
                .postedDate(LocalDateTime.of(2023, 1, 8, 8, 22))
                .vehicleExtras(Collections.emptyList())
                .regMilPow(RegMilPow.builder()
                        .registrationDate(YearMonth.of(2018, 4).toString())
                        .mileage(62579)
                        .kw(75)
                        .hp(102).build())
                .typeStatusFuelGearboxHuDoors(
                        TypeStatusFuelGearboxHuDoors.builder()
                                .vehicleCondition("Accident-free")
                                .typeFuelGearboxHuDoors(
                                        TypeFuelGearboxHuDoors.builder()
                                                .vehicleType(VehicleType.VAN_MINIBUS)
                                                .fuelGearboxHuDoors(
                                                        FuelGearboxHuDoors.builder()
                                                                .fuelType("diesel")
                                                                .gearboxType("automatic transmission")
                                                                .vehicleHU("hu 05/2023")
                                                                .doorsNumber("4/5 doors")
                                                                .build()
                                                ).build()
                                ).build()
                )
                .consumptionEmissions(ConsumptionEmissions.builder()
                        .consumption("ca. 5.0 l/100km (comb.)")
                        .emissions("ca. 130 g CO₂/km (comb.)")
                        .build()
                )
                .seller(
                        Seller.builder()
                                .sellerType("Dealer")
                                .dealerName("Automobile Geißler")
                                .dealerRating(4.6)
                                .ratings(124)
                                .location("DE-04347 Leipzig")
                                .build()
                )
                .priceData(
                        PriceData.builder()
                                .price(24990)
                                .priceRating("Good price")
                                .build()
                )
                .build();

        var listing = listingElementExtractor.extract(doc);

        expectedListing.setScrapeTime(listing.getScrapeTime());

        Assertions.assertEquals("Volkswagen Caddy PKW Alltrack AHK+DSG+Xenon+Navi+Kamera", listing.getTitle());
        Assertions.assertEquals(expectedListing, listing);
    }

    @Test
    void testPostedDateParser() {
        var extractedDateTime = listingElementExtractor.getPostedDateTime("Ad online since Jan 14, 2023, 11:48 PM");
        var expectedDateTime = LocalDateTime.of(LocalDate.of(2023, 1, 14), LocalTime.of(23, 48));

        assertEquals(expectedDateTime, extractedDateTime);
    }
}