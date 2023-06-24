package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Seller {
    private String sellerType;
    private String dealerName;
    private double dealerRating;
    private int ratings;
    private String location;
}
