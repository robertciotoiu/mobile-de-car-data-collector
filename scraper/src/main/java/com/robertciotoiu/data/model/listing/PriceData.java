package com.robertciotoiu.data.model.listing;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceData {
    private int price;
    private String priceRating;
}