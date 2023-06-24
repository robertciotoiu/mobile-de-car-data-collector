package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Document(collection = "listings")
public class Listing {
    @Id
    private String listingId;
    private String title;
    private String category;
    private LocalDateTime scrapeTime;
    private String url;
    private String imgUrl;
    private boolean isNew;
    private boolean isElectric;
    private List<String> vehicleExtras;
    private LocalDateTime postedDate;
    private RegMilPow regMilPow;
    private TypeStatusFuelGearboxHuDoors typeStatusFuelGearboxHuDoors;
    private ConsumptionEmissions consumptionEmissions;
    private Seller seller;
    private PriceData priceData;
}
