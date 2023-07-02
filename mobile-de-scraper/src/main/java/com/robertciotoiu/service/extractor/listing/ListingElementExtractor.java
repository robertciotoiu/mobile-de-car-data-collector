package com.robertciotoiu.service.extractor.listing;

import com.robertciotoiu.data.model.raw.*;
import com.robertciotoiu.exception.ListingIdNotFoundError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.robertciotoiu.service.extractor.listing.ListingXPath.*;

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
        extractAndSetConsumptionEmissionsData(listingElement, listingId, listingBuilder);
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
            var imgUrl = listingElement.selectXpath(IMG_URL_XPATH).attr("data-src");
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
            extractAndSetPostedDate2ndOption(listingElement, listingId, listingBuilder);
        }
    }

    private void extractAndSetPostedDate2ndOption(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var postedDate = listingElement.selectXpath(POSTED_DATE_2ND_XPATH).text();
            var extractedDate = extractDate(postedDate);
            var postedDateTime = getPostedDateTime(extractedDate);
            listingBuilder.postedDate(postedDateTime);
        } catch (Exception e) {
            extractAndSetPostedDateGermanPage(listingElement, listingId, listingBuilder);
        }
    }

    private void extractAndSetPostedDateGermanPage(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder){
        try {
            var postedDate = listingElement.selectXpath(POSTED_DATE_2ND_XPATH).text();
            listingBuilder.postedDate(extractGermanPageDateTime(postedDate));
        } catch (Exception e) {
            extractAndSetPostedDateFromAds(listingElement, listingId, listingBuilder);
        }
    }

    public static LocalDateTime extractGermanPageDateTime(String input) {
        String dateTimeString = input.substring(input.indexOf("seit ") + 5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm", Locale.ENGLISH);
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    private void extractAndSetPostedDateFromAds(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var postedDate = listingElement.selectXpath(POSTED_DATE_ADS_XPATH).text();
            var extractedDate = extractDate(postedDate);
            var postedDateTime = getPostedDateTime(extractedDate);
            listingBuilder.postedDate(postedDateTime);
        } catch (Exception e) {
            logger.warn("Failed to extract postedDate for listing: {} using the 3th option(Ads). Element: {}", listingId, listingElement);
        }
    }

    private void extractAndSetRegMilPow(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var regMilPow = listingElement.selectXpath(REG_MIL_POW_XPATH).text();
            listingBuilder.regMilPow(extractRegMilPow(regMilPow, listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract regMilPow for listing: {}", listingId);
        }
    }

    private void extractAndSetTypeStatusFuelGearboxHuDoors(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var typeStatusFuelGearboxHuDoors = listingElement.selectXpath(TYPE_FUEL_GEARBOX_HU_DOORS_XPATH);
            listingBuilder.typeStatusFuelGearboxHuDoors(typeStatusFuelGearboxHuDoorsExtractor.extractTypeStatusFuelGearboxHuDoors(typeStatusFuelGearboxHuDoors, listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract typeStatusFuelGearboxHuDoors for listing: {}", listingId);
        }
    }

    private void extractAndSetConsumptionEmissionsData(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var consumptionEmissionsElements = listingElement.selectXpath(CONSUMPTION_EMISSIONS_XPATH);
            listingBuilder.consumptionEmissions(extractConsumptionEmissions(consumptionEmissionsElements));
        } catch (Exception e) {
            extractAndSetConsumptionEmissionsData2ndOption(listingElement, listingId, listingBuilder);
        }
    }

    private ConsumptionEmissions extractConsumptionEmissions(Elements consumptionEmissionsElements) {
        var consumptionEmissionElement = consumptionEmissionsElements.last();
        var consumptionEmissionsString = ((TextNode) consumptionEmissionElement.childNode(consumptionEmissionElement.childNodeSize() - 1)).text();
        var sanitizedConsumptionEmissionString = consumptionEmissionsString.replaceAll(",(?=\\d)", ".");
        var consumptionEmissionsArray = sanitizedConsumptionEmissionString.split(",");
        var consumption = consumptionEmissionsArray[0].trim();
        var emissions = consumptionEmissionsArray[1].trim();

        return ConsumptionEmissions.builder().consumption(consumption).emissions(emissions).build();
    }

    private void extractAndSetConsumptionEmissionsData2ndOption(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var consumptionEmissionsString = listingElement.selectXpath(CONSUMPTION_EMISSIONS_XPATH).text();
            var consumptionEmissions = extractConsumptionEmissions2ndOption(consumptionEmissionsString);
            listingBuilder.consumptionEmissions(consumptionEmissions);

            if (consumptionEmissions.getConsumption() == null || consumptionEmissions.getConsumption().equals("")) {
                logger.debug("Failed to extract consumption for listing: {} using 2nd option.", listingId);
            }

            if (consumptionEmissions.getEmissions() == null || consumptionEmissions.getEmissions().equals("")) {
                logger.debug("Failed to extract emissions for listing: {} using 2nd option.", listingId);
            }
        } catch (Exception e) {
            logger.info("Failed to extract consumptionEmissions for listing: {} using 2nd option. Exception: ", listingId, e);
        }
    }

    private ConsumptionEmissions extractConsumptionEmissions2ndOption(String consumptionEmissionsString) {
        var consumptionEmissionsBuilder = ConsumptionEmissions.builder();
        String consumptionRegex = "\\d+\\.\\d+\\s+l/100km\\s+\\(comb\\.\\)\\*?";
        Pattern consumptionPattern = Pattern.compile(consumptionRegex);
        Matcher consumptionMatcher = consumptionPattern.matcher(consumptionEmissionsString);

        if (consumptionMatcher.find()) {
            var consumption = consumptionMatcher.group();
            consumptionEmissionsBuilder.consumption(consumption);
        }

        String emissionsRegex = "\\d+\u2009g CO₂/km\\s+\\(comb\\.\\)\\*?";
        Pattern emissionsPattern = Pattern.compile(emissionsRegex);
        Matcher emissionMatcher = emissionsPattern.matcher(consumptionEmissionsString);

        if (emissionMatcher.find()) {
            var emissions = emissionMatcher.group();
            consumptionEmissionsBuilder.emissions(emissions);
        }

        return consumptionEmissionsBuilder.build();
    }

    private void extractAndSetVehicleExtras(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var vehicleExtras1 = listingElement.selectXpath(VEHICLE_EXTRAS_1_XPATH);
            var vehicleExtras2 = listingElement.selectXpath(VEHICLE_EXTRAS_2_XPATH);
            var vehicleExtras3 = listingElement.selectXpath(VEHICLE_EXTRAS_3_XPATH);
            listingBuilder.vehicleExtras(extractAndSetVehicleExtras(vehicleExtras1, vehicleExtras2, vehicleExtras3));
        } catch (Exception e) {
            logger.warn("Failed to extract vehicleExtras for listing: {}", listingId);
        }
    }

    private void extractAndSetSeller(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        try {
            var seller = listingElement.selectXpath(SELLER_XPATH);
            listingBuilder.seller(extractAndSetSeller(seller, listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract seller for listing: {}", listingId);
        }
    }

    private void extractAndSetPrice(Element listingElement, String listingId, Listing.ListingBuilder listingBuilder) {
        var price = listingElement.selectXpath(PRICE_XPATH);
        var priceRating = listingElement.selectXpath(PRICE_RATING_XPATH);
        var priceDataBuilder = PriceData.builder();
        setPrice(price, listingId, priceDataBuilder);
        setPriceRating(priceRating, listingId, priceDataBuilder);
        listingBuilder.priceData(priceDataBuilder.build());
    }

    private static void setPrice(Elements price, String listingId, PriceData.PriceDataBuilder listingBuilder) {
        try {
            listingBuilder.price(Integer.parseInt(price.text().replace("€", "").replace(",", "")));
        } catch (Exception e) {
            logger.debug("Failed to extract price for listing: {}. Price element: {}", listingId, price);
            setPriceHandlePointAndNBSP(price, listingId, listingBuilder);
        }
    }

    private static void setPriceHandlePointAndNBSP(Elements price, String listingId, PriceData.PriceDataBuilder listingBuilder) {
        try {
            listingBuilder.price(extractPrice(price.text()));
        } catch (Exception e) {
            logger.warn("Failed to extract price for listing: {} using setPriceHandlePointAndNBSP.", listingId);
            listingBuilder.rawPrice(price.text());
        }
    }

    public static Integer extractPrice(String stringPrice) {
        String numberString = stringPrice.replaceAll("[^\\d,]", "") // remove all non-digit and non-comma characters
                .replace(",", "."); // replace comma with decimal point
        return Integer.parseInt(numberString);
    }

    private static void setPriceRating(Elements priceRating, String listingId, PriceData.PriceDataBuilder listingBuilder) {
        try {
            listingBuilder.priceRating(priceRating.text());
        } catch (Exception e) {
            logger.warn("Failed to extract priceRating for listing: {}", listingId);
        }
    }

    private Seller extractAndSetSeller(Elements sellerElements, String listingId) {
        Element sellerElement;

        try {
            sellerElement = sellerElements.first();
        } catch (Exception e) {
            logger.warn("Failed to extract sellerElement");
            return Seller.builder().build();
        }

        if (sellerElement == null)
            return Seller.builder().build();

        Seller seller;
        if (sellerElement.childNodeSize() == 1) {
            // Seller
            seller = extractPrivateSeller(sellerElement, listingId);
        } else {
            // Dealer
            seller = extractDealer(sellerElement, listingId);
        }

        return seller;
    }

    private static Seller extractPrivateSeller(Element sellerElement, String listingId) {
        var sellerBuilder = Seller.builder();
        String[] locationSellerTypeArray;

        try {
            locationSellerTypeArray = sellerElement.text().split(",");
        } catch (Exception e) {
            logger.warn("Failed to extract private seller locationSellerTypeArray for listing: {}", listingId);
            return null;
        }

        try {
            var privateSellerLocation = locationSellerTypeArray[0].trim();
            sellerBuilder.location(privateSellerLocation);
        } catch (Exception e) {
            logger.warn("Failed to extract privateSellerLocation for listing: {}", listingId);
        }

        try {
            var privateSellerType = locationSellerTypeArray[1].trim();
            sellerBuilder.sellerType(privateSellerType);
        } catch (Exception e) {
            logger.warn("Failed to extract privateSellerType for listing: {}", listingId);
        }
        return sellerBuilder.build();
    }

    private static Seller extractDealer(Element sellerElement, String listingId) {
        Seller dealer;
        if (hasLogoElement(sellerElement)) {
            dealer = extractDealer(sellerElement, listingId, DEALER_WITH_LOGO_NAME_XPATH, DEALER_WITH_LOGO_RATING_PARENT_XPATH, DEALER_WITH_LOGO_RATING_XPATH);
        } else {
            dealer = extractDealer(sellerElement, listingId, DEALER_NAME_XPATH, DEALER_RATING_PARENT_XPATH, DEALER_RATING_XPATH);
        }
        return dealer;
    }

    private static Seller extractDealer(Element sellerElement, String listingId, String dealerNameXpath, String dealerRatingParentXpath, String dealerRatingXpath) {
        var dealerBuilder = Seller.builder();

        try {
            var dealerName = sellerElement.selectXpath(dealerNameXpath).text();
            dealerBuilder.dealerName(dealerName);
        } catch (Exception e) {
            logger.warn("Failed to extract dealerName for listing: {}", listingId);
        }
        extractDealerRating(sellerElement, listingId, dealerRatingParentXpath, dealerRatingXpath, dealerBuilder);

        try {
            var dealerLocationTypeArray = sellerElement.lastElementChild().text().split(",");
            extractAndSetDealerLocation(listingId, dealerBuilder, dealerLocationTypeArray);
            extractAndSetDealerType(listingId, dealerBuilder, dealerLocationTypeArray);
        } catch (Exception e) {
            logger.warn("Failed to extract dealerLocationTypeArray for listing: {}", listingId);
        }

        return dealerBuilder.build();
    }

    private static void extractAndSetDealerType(String listingId, Seller.SellerBuilder dealerBuilder, String[] dealerLocationTypeArray) {
        try {
            var dealerType = dealerLocationTypeArray[1].trim();
            dealerBuilder.sellerType(dealerType);
        } catch (Exception e) {
            logger.warn("Failed to extract sellerType for listing: {}", listingId);
        }
    }

    private static void extractAndSetDealerLocation(String listingId, Seller.SellerBuilder dealerBuilder, String[] dealerLocationTypeArray) {
        try {
            var dealerLocation = dealerLocationTypeArray[0].trim();
            dealerBuilder.location(dealerLocation);
        } catch (Exception e) {
            logger.warn("Failed to extract dealerLocation for listing: {}", listingId);
        }
    }

    private static void extractDealerRating(Element sellerElement, String listingId, String dealerRatingParentXpath, String dealerRatingXpath, Seller.SellerBuilder dealerBuilder) {
        var dealerRatingElements = sellerElement.selectXpath(dealerRatingParentXpath);
        if (!dealerRatingElements.isEmpty()) {
            try {
                var dealerRating = Double.parseDouble(
                        dealerRatingElements
                                .first()
                                .selectXpath(dealerRatingXpath)
                                .attr("data-rating"));
                dealerBuilder.dealerRating(dealerRating);
            } catch (Exception e) {
                logger.warn("Failed to extract dealerRating for listing: {}", listingId);
            }

            try {
                var ratings = Integer.parseInt(dealerRatingElements.text().split(" ")[0]);
                dealerBuilder.ratings(ratings);
            } catch (Exception e) {
                logger.warn("Failed to extract ratings for listing: {}", listingId);
            }
        }
    }

    private static boolean hasLogoElement(Element sellerElement) {
        return !sellerElement.selectXpath("//div[contains(@class,'image-block-dealerLogo')]").html().equals("");
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

    private RegMilPow extractRegMilPow(String regMilPowString, String listingId) {
        var regMilPowBuilder = RegMilPow.builder();

        var removedThousandSeparator = regMilPowString.replaceAll(",(?=\\d)", "");
        var regMilPowArray = removedThousandSeparator.split(",");

        try {
            regMilPowBuilder.registrationDate(getRegistrationDate(regMilPowArray[0], listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract registrationDate for listing: {}", listingId);
        }

        try {
            regMilPowBuilder.mileage(getMileage(regMilPowArray[1], listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract mileage for listing: {}", listingId);
        }

        try {
            regMilPowBuilder.kw(getKw(regMilPowArray[2], listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract kw for listing: {}", listingId);
        }

        try {
            regMilPowBuilder.hp(getHp(regMilPowArray[2], listingId));
        } catch (Exception e) {
            logger.warn("Failed to extract hp for listing: {}", listingId);
        }

        return regMilPowBuilder.build();
    }

    private Integer getHp(String regMilPow, String listingId) {
        try {
            return Integer.parseInt(regMilPow
                    .substring(regMilPow.indexOf("(") + 1, regMilPow.indexOf(" ", regMilPow.indexOf("(")))
                    .trim());
        } catch (Exception e) {
            logger.warn("Failed to extract hp for listing: {}", listingId);
            return null;
        }
    }

    private Integer getKw(String regMilPow, String listingId) {
        try {
            return Integer.parseInt(regMilPow
                    .substring(0, regMilPow.indexOf(" ", 1))
                    .trim());
        } catch (Exception e) {
            logger.warn("Failed to extract kw for listing: {}", listingId);
            return null;
        }
    }

    private Integer getMileage(String regMilPow, String listingId) {
        try {
            String mileageString = regMilPow
                    .substring(0, regMilPow.lastIndexOf(" "))
                    .replace(".", "")
                    .trim();
            return Integer.parseInt(mileageString);
        } catch (Exception e) {
            logger.warn("Failed to extract mileage for listing: {}", listingId);
            return null;
        }
    }

    public String extractDate(String input) {
        Pattern pattern = Pattern.compile("Ad online since (.+? [AP]M)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "";
        }
    }

    public LocalDateTime getPostedDateTime(String postedDate) {
        var formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a");
        var postedDateTimeString = postedDate.substring(postedDate.indexOf("since") + 6).trim();
        return LocalDateTime.parse(postedDateTimeString, formatter);
    }

    public String getRegistrationDate(String registrationDate, String listingId) {
        try {
            if (registrationDate.equals("New car") || registrationDate.equals("Pre-Registration"))
                return YearMonth.now().toString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
            return YearMonth.parse(registrationDate.substring(registrationDate.indexOf("FR") + 3).trim(), formatter).toString();
        } catch (Exception e) {
            logger.warn("Failed to extract registration date for listing: {}. Saving registration date string found: {}", listingId, registrationDate);
            return registrationDate;
        }
    }
}
