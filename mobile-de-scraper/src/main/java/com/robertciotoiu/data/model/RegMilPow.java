package com.robertciotoiu.data.model;

import lombok.Builder;
import lombok.Data;

import java.time.YearMonth;

@Data
@Builder
public class RegMilPow {
    private YearMonth registrationDate;
    private int mileage;
    private int kw;
    private int hp;
}
