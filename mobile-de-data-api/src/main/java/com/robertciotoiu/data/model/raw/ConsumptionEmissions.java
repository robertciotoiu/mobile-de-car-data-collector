package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumptionEmissions {
    private String consumption;
    private String emissions;
}
