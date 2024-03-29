package com.robertciotoiu.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceData {
    private Integer price;
    private String priceRating;
    private String rawPrice;
}
