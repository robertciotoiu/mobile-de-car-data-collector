package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FuelGearboxHuDoors {
    private String fuelType;
    private String gearboxType;
    private String vehicleHU;
    private String doorsNumber;
}
