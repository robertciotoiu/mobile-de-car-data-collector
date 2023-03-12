package com.robertciotoiu.service.extractor;

import com.robertciotoiu.data.model.*;
import com.robertciotoiu.exception.ListingIdNotFoundError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.robertciotoiu.service.extractor.ListingXPath.*;

@Component
public class ListingElementExtractor {
    private static final Logger logger = LogManager.getLogger(ListingElementExtractor.class);

    @Autowired
    TypeStatusFuelGearboxHuDoorsExtractor typeStatusFuelGearboxHuDoorsExtractor;

    /**
     * <p>Tries to extract from a listing element as many Listing fields as possibles.<p>
     * <p>Never throws an Exception.<p>
     * <p>Could throw a ListingIdNotFoundError when listing Id is not found</p>
     *
     * @param listingElement Jsoup Element containing only the listing html element
     * @return Listing object.
     */
    public Listing extract(Element listingElement) {
        var listingBuilder = Listing.builder();

        try {
            String listingId = extractAndSetListingId(listingElement, listingBuilder);
            extractAndSetListingInformation(listingElement, listingId, listingBuilder);
        } catch (Exception e) {
            logger.error("Failed to extract listing: {}", listingElement);
        }

        return listingBuilder.build();
    }

    private String extractAndSetListingId(Element listingElement, Listing.ListingBuilder listingBuilder) {
        var listingId = listingElement.selectXpath(ListingXPath.LISTING_ID_XPATH).attr("data-listing-id");
        if (listingId.equals("")) {
            logger.error("Failed to extract listingId! Listing element: {}", listingElement);
            throw new ListingIdNotFoundError("ListingId not found! Either a change in website structure, either a serious bug. Stopping program execution...");
        }

        listingBuilder.listingId(listingId);
        return listingId;
    }

    private void extractAndSetListingInformation(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        extractAndSetTitle(listingElement, listingId, listingBuilder);
        extractAndSetIsNew(listingElement, listingId, listingBuilder);
        extractAndSetUrl(listingElement, listingId, listingBuilder);
        extractAndSetImgUrl(listingElement, listingId, listingBuilder);
        extractAndSetIsElectric(listingElement, listingId, listingBuilder);
        extractAndSetPostedDate(listingElement, listingId, listingBuilder);
        extractAndSetRegMilPow(listingElement, listingId, listingBuilder);
        extractAndSetTypeStatusFuelGearboxHuDoors(listingElement, listingId, listingBuilder);
        extractAndSetVehicleData(listingElement, listingId, listingBuilder);
        extractAndSetVehicleExtras(listingElement, listingId, listingBuilder);
        extractAndSetSeller(listingElement, listingId, listingBuilder);
        extractAndSetPrice(listingElement, listingId, listingBuilder);

    }

    private void extractAndSetTitle(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var title = listingElement.selectXpath(TITLE_XPATH).text();
            listingBuilder.title(title);
        } catch (Exception e) {
            logger.warn("Failed to extract title for listing: {}", listingId);
        }
    }

    private void extractAndSetIsNew(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var isNewText = listingElement.selectXpath(IS_NEW_XPATH).text();
            listingBuilder.isNew(isNewText.equals("New"));
        } catch (Exception e) {
            logger.warn("Failed to extract isNewText for listing: {}", listingId);
        }
    }

    private void extractAndSetUrl(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var url = listingElement.selectXpath(URL_XPATH).attr("href");
            listingBuilder.url(url);
        } catch (Exception e) {
            logger.warn("Failed to extract url for listing: {}", listingId);
        }
    }

    private void extractAndSetImgUrl(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var imgUrl = listingElement.selectXpath(IMG_URL_XPATH).attr("src");
            listingBuilder.imgUrl(imgUrl);
        } catch (Exception e) {
            logger.warn("Failed to extract imgUrl for listing: {}", listingId);
        }
    }

    private void extractAndSetIsElectric(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var isElectric = listingElement.selectXpath(IS_ELECTRIC_XPATH).attr("data-has-electric-engine");
            listingBuilder.isElectric(isElectric.equals("true"));
        } catch (Exception e) {
            logger.warn("Failed to extract isElectric for listing: {}", listingId);
        }
    }

    private void extractAndSetPostedDate(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var postedDate = listingElement.selectXpath(POSTED_DATE_XPATH).text();
            listingBuilder.postedDate(getPostedDateTime(postedDate));
        } catch (Exception e) {
            logger.warn("Failed to extract postedDate for listing: {}", listingId);
        }
    }

    private void extractAndSetRegMilPow(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var regMilPow = listingElement.selectXpath(REG_MIL_POW_XPATH).text();
            listingBuilder.regMilPow(extractRegMilPow(regMilPow));
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract regMilPow for listing: {}", listingId);
        }
    }

    private void extractAndSetTypeStatusFuelGearboxHuDoors(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var typeStatusFuelGearboxHuDoors = listingElement.selectXpath(TYPE_FUEL_GEARBOX_HU_DOORS_XPATH);
            listingBuilder.typeStatusFuelGearboxHuDoors(typeStatusFuelGearboxHuDoorsExtractor.extractTypeStatusFuelGearboxHuDoors(typeStatusFuelGearboxHuDoors, listingId));
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract typeStatusFuelGearboxHuDoors for listing: {}", listingId);
        }
    }

    private void extractAndSetVehicleData(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var vehicleDataString = listingElement.selectXpath(CONSUMPTION_EMISSIONS_XPATH).text();
            listingBuilder.consumptionEmissions(extractConsumptionEmissions(vehicleDataString));
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract vehicleDataString for listing: {}", listingId);
        }
    }

    private void extractAndSetVehicleExtras(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var vehicleExtras1 = listingElement.selectXpath(VEHICLE_EXTRAS_1_XPATH);
            var vehicleExtras2 = listingElement.selectXpath(VEHICLE_EXTRAS_2_XPATH);
            var vehicleExtras3 = listingElement.selectXpath(VEHICLE_EXTRAS_3_XPATH);
            listingBuilder.vehicleExtras(extractAndSetVehicleExtras(vehicleExtras1, vehicleExtras2, vehicleExtras3));
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract vehicleExtras for listing: {}", listingId);
        }
    }

    private void extractAndSetSeller(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {

            var seller = listingElement.selectXpath(SELLER_XPATH);
            listingBuilder.seller(extractAndSetSeller(seller));
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract seller for listing: {}", listingId);
        }
    }

    private void extractAndSetPrice(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var price = listingElement.selectXpath(PRICE_XPATH);
            var priceRating = listingElement.selectXpath(PRICE_RATING_XPATH);
            listingBuilder.priceData(PriceData.builder()
                    .price(Integer.parseInt(price.text().replace("€", "").replace(",", "")))
                    .priceRating(priceRating.text())
                    .build()
            );
        } catch (Exception e) {
            //TODO make sure inside the method there is a logging for every failed field
            logger.warn("Failed to extract priceData for listing: {}", listingId);
        }
    }

    // TODO: wrap as many important operations as possible with clear indicative logs(usefull for debugging)
    private Seller extractAndSetSeller(Elements sellerElements) {
        Element sellerElement;

        try {
            sellerElement = sellerElements.first();
        } catch (Exception e) {
            logger.warn("Failed to extract sellerElement");
            return Seller.builder().build();
        }

        if (sellerElement == null)
            return Seller.builder().build();

        var seller = Seller.builder();
        if (sellerElement.childNodeSize() == 1) {
            // Private seller
            var locationSellerTypeArray = sellerElement.text().split(",");
            var privateSellerLocation = locationSellerTypeArray[0].trim();
            var privateSellerType = locationSellerTypeArray[1].trim();
            seller.location(privateSellerLocation);
            seller.sellerType(privateSellerType);
        } else {
            // Dealer
            var dealerName = sellerElement.selectXpath(DEALER_NAME_XPATH).text();
            var dealerRatingElements = sellerElement.selectXpath(DEALER_RATING_PARENT_XPATH);
            if (!dealerRatingElements.isEmpty()) {
                var dealerRating = Double.parseDouble(
                        dealerRatingElements
                                .first()
                                .selectXpath(DEALER_RATING_XPATH)
                                .attr("data-rating"));
                seller.dealerRating(dealerRating);

                var ratings = Integer.parseInt(dealerRatingElements.text().split(" ")[0]);
                seller.ratings(ratings);
            }
            seller.dealerName(dealerName);

            var dealerLocationTypeArray = sellerElement.child(1).text().split(",");
            var dealerLocation = dealerLocationTypeArray[0].trim();
            var dealerType = dealerLocationTypeArray[1].trim();
            seller.location(dealerLocation)
                    .sellerType(dealerType);
        }

        return seller.build();
    }

    private List<String> extractAndSetVehicleExtras(Elements vehicleExtras1, Elements vehicleExtras2, Elements vehicleExtras3) {
        if (vehicleExtras1.isEmpty())
            return Collections.emptyList();

        var vehicleExtras = new ArrayList<String>();

        vehicleExtras.add(vehicleExtras1.text());
        vehicleExtras.add(vehicleExtras2.text());
        vehicleExtras.add(vehicleExtras3.text());

        return vehicleExtras;
    }

    private ConsumptionEmissions extractConsumptionEmissions(String vehicleDataString) {
        var consumptionEmissions = vehicleDataString.substring(vehicleDataString.indexOf("ca."));
        var consumptionEmissionsArray = consumptionEmissions.split("\\),");
        var consumption = consumptionEmissionsArray[0].trim() + ")";
        var emissions = consumptionEmissionsArray[1].trim();

        return ConsumptionEmissions.builder().consumption(consumption).emissions(emissions).build();
    }

    private RegMilPow extractRegMilPow(String regMilPowString) {
        var regMilPowArray = regMilPowString.split(",");

        return RegMilPow.builder()
                .registrationDate(getRegistrationDate(regMilPowArray[0]))
                .mileage(getMileage(regMilPowArray[1]))
                .kw(getKw(regMilPowArray[2]))
                .hp(getHp(regMilPowArray[2]))
                .build();
    }

    private int getHp(String regMilPow) {
        return Integer.parseInt(regMilPow
                .substring(regMilPow.indexOf("(") + 1, regMilPow.indexOf(" ", regMilPow.indexOf("(")))
                .trim());
    }

    private int getKw(String regMilPow) {
        return Integer.parseInt(regMilPow
                .substring(0, regMilPow.indexOf(" ", 1))
                .trim());
    }

    private int getMileage(String regMilPow) {
        String mileageString = regMilPow
                .substring(0, regMilPow.lastIndexOf(" "))
                .replace(".", "")
                .trim();
        return Integer.parseInt(mileageString);
    }

    public LocalDateTime getPostedDateTime(String postedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a");
        return LocalDateTime.parse(postedDate.substring(postedDate.indexOf("since") + 6).trim(), formatter);
    }

    public YearMonth getRegistrationDate(String registrationDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        return YearMonth.parse(registrationDate.substring(registrationDate.indexOf("FR") + 3).trim(), formatter);
    }
}
