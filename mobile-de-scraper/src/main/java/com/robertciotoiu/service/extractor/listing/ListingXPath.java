package com.robertciotoiu.service.extractor.listing;

public class ListingXPath {
    public static final String LISTING_ID_XPATH = "a";
    public static final String TITLE_XPATH = "a/div/div[2]/div[1]/div[1]/div/span[contains(@class,'h3')]";
    public static final String IS_NEW_XPATH = "a/div/div[2]/div[1]/div[1]/div/span[contains(@class,'new-headline-label')]";
    public static final String POSTED_DATE_XPATH = "a/div/div[2]/div[1]/div[1]/div/span[contains(@class,'u-block u-pad-top-9')]";
    public static final String POSTED_DATE_2ND_XPATH = "a/div/div[2]/div[1]/div[1]/div";
    public static final String POSTED_DATE_ADS_XPATH = "a/div/div[2]/div[1]/div[1]/div/span[3]";
    ////a/div/div[2]/div[1]/div/div/span[2]
    public static final String URL_XPATH = "a";
    public static final String IMG_URL_XPATH = "a/div/div[1]/div/img";
    public static final String IS_ELECTRIC_XPATH = "a";
    public static final String REG_MIL_POW_XPATH = "a/div/div[2]/div[2]/div[1]/div/div[1]";
    // When Vehicle status is not present, vehicle type, fuel, gearbox, hu and doors are all under a div tag
    public static final String TYPE_FUEL_GEARBOX_HU_DOORS_XPATH = "a/div/div[2]/div[2]/div[1]/div/div[2]";
    public static final String CONSUMPTION_EMISSIONS_XPATH = "a/div/div[2]/div[2]/div[1]/div";
    // ATM there are always 3 attributes or 0
    public static final String VEHICLE_EXTRAS_1_XPATH = "a/div/div[2]/div[2]/div[1]/div[2]/div[1]/p";
    public static final String VEHICLE_EXTRAS_2_XPATH = "a/div/div[2]/div[2]/div[1]/div[2]/div[2]/p";
    public static final String VEHICLE_EXTRAS_3_XPATH = "a/div/div[2]/div[2]/div[1]/div[2]/div[3]/p";
    public static final String SELLER_XPATH = "a/div/div[2]/div[2]/div[2]/div";
    //100% always the same
    public static final String PRICE_XPATH = "a/div/div[2]/div[1]/div[2]/div[1]/span[1]/.";
    ///html/body/div[4]/div[1]/div[3]/div[4]/div[2]/div[2]/div[7]/div/div[1]/div/div/div[2]/a/div/div[2]/div[2]/div/div[1]/span[1]
    public static final String PRICE_RATING_XPATH = "a/div/div[2]/div[1]/div[2]/div[2]/div/.";
    public static final String DEALER_NAME_XPATH = "div[1]/span[1]";
    public static final String DEALER_RATING_PARENT_XPATH = "div[1]/span[2]";
    public static final String DEALER_RATING_XPATH = "div/span[1]";
    public static final String DEALER_WITH_LOGO_NAME_XPATH = "div[2]/span[1]";
    public static final String DEALER_WITH_LOGO_RATING_PARENT_XPATH = "div[2]/span[2]";
    public static final String DEALER_WITH_LOGO_RATING_XPATH = "div/span[1]";


    private ListingXPath(){
        throw new IllegalStateException("Static class");
    }
}
