package com.robertciotoiu.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegMilPow {
    private String registrationDate;
    private Integer mileage;
    private Integer kw;
    private Integer hp;
}
