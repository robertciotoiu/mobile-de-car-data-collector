package com.robertciotoiu.service.extractor;

public class ListingXPath {
    public static final String LISTING_ID_XPATH = "/div/a";
    public static final String TITLE_XPATH = "//div/a/div/div[2]/div[1]/div[1]/div/span[contains(@class,'h3')]";
    public static final String IS_NEW_XPATH = "//div/a/div/div[2]/div[1]/div[1]/div/span[contains(@class,'new-headline-label')]";
    public static final String URL_XPATH = "/div/a";
    public static final String IMG_URL_XPATH = "/div/a/div/div[1]/div/img";
    public static final String IS_ELECTRIC_XPATH = "/div/a";
    public static final String POSTED_DATE_XPATH = "/div/a/div/div[2]/div[1]/div[1]/div/span[2]";
    public static final String REG_MIL_POW_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div/div[1]";
    // When Vehicle status is not present, vehicle type, fuel, gearbox, hu and doors are all under a div tag
    public static final String TYPE_FUEL_GEARBOX_HU_DOORS_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div/div[2]";
    public static final String CONSUMPTION_EMISSIONS_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div";
    // ATM there are always 3 attributes or 0
    public static final String VEHICLE_EXTRAS_1_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div[2]/div[1]/p";
    public static final String VEHICLE_EXTRAS_2_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div[2]/div[2]/p";
    public static final String VEHICLE_EXTRAS_3_XPATH = "/div/a/div/div[2]/div[2]/div[1]/div[2]/div[3]/p";
    public static final String SELLER_XPATH = "/div/a/div/div[2]/div[2]/div[2]/div";
    //100% always the same
    public static final String PRICE_XPATH = "/div/a/div/div[2]/div[1]/div[2]/div[1]/span[1]/.";
    public static final String PRICE_RATING_XPATH = "/div/a/div/div[2]/div[1]/div[2]/div[2]/div/.";

    private ListingXPath(){
        throw new IllegalStateException("Static class");
    }
}
