package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;

/**
 * rawTBPString: raw to be processed string is storing the whole String found which may contain TypeFuelGearboxHuDoors.
 * if this string is present, it means that the expected structure slightly differs
 * and it will be later processed by another microservice
 */
@Data
@Builder
public class TypeFuelGearboxHuDoors {
    private VehicleType vehicleType;
    private FuelGearboxHuDoors fuelGearboxHuDoors;
    private String rawTBPString;
}
