package com.robertciotoiu.data.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeFuelGearboxHuDoors {
    private VehicleType vehicleType;
    private FuelGearboxHuDoors fuelGearboxHuDoors;
}
