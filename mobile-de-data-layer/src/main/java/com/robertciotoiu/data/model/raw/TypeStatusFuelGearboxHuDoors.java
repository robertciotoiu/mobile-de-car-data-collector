package com.robertciotoiu.data.model.raw;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeStatusFuelGearboxHuDoors {
    /**
     * Can contain the following information:
     * Vehicle Condition: "Damaged"; "Accident-free"; "Repaired accident damage"
     * Availability: "Availability: In 2 Weeks after order"
     * This is usually between vehicle type and fuel type in <br> tags
     */
    private String vehicleCondition;
    private TypeFuelGearboxHuDoors typeFuelGearboxHuDoors;
}
