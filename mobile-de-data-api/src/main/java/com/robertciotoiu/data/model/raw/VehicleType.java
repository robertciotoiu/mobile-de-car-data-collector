package com.robertciotoiu.data.model.raw;

public enum VehicleType {
    CABRIO_ROADSTER,
    ESTATE,
    SALOON,
    SMALL,
    SPORTS_COUPE,
    SUV_OFFROAD_PICKUP,
    VAN_MINIBUS,
    OTHER;

    public static VehicleType fromString(String vehicleType) {
        vehicleType = vehicleType.toLowerCase();
        if (vehicleType.contains("cabrio") || vehicleType.contains("roadster")) return CABRIO_ROADSTER;
        if (vehicleType.contains("estate")) return ESTATE;
        if (vehicleType.contains("saloon")) return SALOON;
        if (vehicleType.contains("small")) return SMALL;
        if (vehicleType.contains("sports") || vehicleType.contains("coupe")) return SPORTS_COUPE;
        if (vehicleType.contains("suv") || vehicleType.contains("offroad") || vehicleType.contains("pickup"))
            return SUV_OFFROAD_PICKUP;
        if (vehicleType.contains("van") || vehicleType.contains("minibus")) return VAN_MINIBUS;
        return OTHER;
    }
}
