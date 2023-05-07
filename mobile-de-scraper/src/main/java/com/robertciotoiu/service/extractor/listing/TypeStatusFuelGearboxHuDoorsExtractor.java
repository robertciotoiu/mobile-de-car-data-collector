package com.robertciotoiu.service.extractor.listing;

import com.robertciotoiu.data.model.raw.FuelGearboxHuDoors;
import com.robertciotoiu.data.model.raw.TypeFuelGearboxHuDoors;
import com.robertciotoiu.data.model.raw.TypeStatusFuelGearboxHuDoors;
import com.robertciotoiu.data.model.raw.VehicleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TypeStatusFuelGearboxHuDoorsExtractor {
    private static final Logger logger = LogManager.getLogger(TypeStatusFuelGearboxHuDoorsExtractor.class);
    private static final String VEHICLE_TYPE = "vehicleType";
    private static final String FUEL_TYPE = "fuelType";
    private static final String GEARBOX_TYPE = "gearboxType";
    private static final String VEHICLE_HU = "vehicleHU";
    private static final String DOORS_NUMBER = "doorsNumber";
    private static final HashMap<String, String> stringToCarFeature = prepareCarFeatures();

    public TypeStatusFuelGearboxHuDoors extractTypeStatusFuelGearboxHuDoors(Elements typeStatusFuelGearboxHuDoorsElements, String listingId) {
        Element typeStatusFuelGearboxHuDoorsElement;
        try {
            typeStatusFuelGearboxHuDoorsElement = typeStatusFuelGearboxHuDoorsElements.first();
        } catch (Exception e) {
            logger.error("Cannot extract typeStatusFuelGearboxHuDoorsElement", e);
            return TypeStatusFuelGearboxHuDoors.builder().build();
        }

        if (typeStatusFuelGearboxHuDoorsElement == null) {
            logger.error("Missing typeStatusFuelGearboxHuDoorsElement");
            return TypeStatusFuelGearboxHuDoors.builder().build();
        }

        TypeFuelGearboxHuDoors typeFuelGearboxHuDoors;
        String vehicleCondition = null;
        var typeFuelGearboxHuDoorsRaw = typeStatusFuelGearboxHuDoorsElement.text();

        if (typeStatusFuelGearboxHuDoorsElement.childNodeSize() == 3) {
            vehicleCondition = extractVehicleCondition(typeStatusFuelGearboxHuDoorsElement, listingId);
            typeFuelGearboxHuDoors = extractTypeFuelGearboxHuDoors(typeFuelGearboxHuDoorsRaw);
        } else if (typeStatusFuelGearboxHuDoorsElement.childNodeSize() == 1) {
            typeFuelGearboxHuDoors = extractTypeFuelGearboxHuDoors(typeFuelGearboxHuDoorsRaw);
        } else {
            // Too many combinations to handle. Save it for a later smart processing microservice.
            logger.warn("Found element that has a different structure from the coded options. Storing whole text for later processing. Listing: {} Element: {}", listingId, typeStatusFuelGearboxHuDoorsElement);
            typeFuelGearboxHuDoors = TypeFuelGearboxHuDoors.builder().rawTBPString(typeFuelGearboxHuDoorsRaw).build();
        }

        // If important specs are missing, try to save the entire description for later post-processing
        if(typeFuelGearboxHuDoors.getRawTBPString() == null && (typeFuelGearboxHuDoors.getFuelGearboxHuDoors().getFuelType() == null || typeFuelGearboxHuDoors.getFuelGearboxHuDoors().getGearboxType() == null)){
            typeFuelGearboxHuDoors.setRawTBPString(typeFuelGearboxHuDoorsRaw);
            logger.warn("Fuel type or gearbox type is missing for listingsId: {}. Saving raw string for later post-processing", listingId);
        }

        return TypeStatusFuelGearboxHuDoors.builder()
                .vehicleCondition(vehicleCondition)
                .typeFuelGearboxHuDoors(typeFuelGearboxHuDoors)
                .build();
    }

    private String extractVehicleCondition(Element typeStatusFuelGearboxHuDoorsElement, String listingId) {
        String vehicleCondition = null;
        try {
            vehicleCondition = ((Element) typeStatusFuelGearboxHuDoorsElement.childNode(1)).text();
        } catch (Exception e) {
            logger.warn("Failed to extract vehicleCondition for listing: {}", listingId);
            logger.debug(typeStatusFuelGearboxHuDoorsElement);
        }
        return vehicleCondition;
    }

    private TypeFuelGearboxHuDoors extractTypeFuelGearboxHuDoors(String typeFuelGearboxHuDoorsString) {
        if(typeFuelGearboxHuDoorsString == null){
            return TypeFuelGearboxHuDoors.builder().build();
        }

        var typeFuelGearboxHuDoors = TypeFuelGearboxHuDoors.builder();
        var fuelGearboxHuDoors = FuelGearboxHuDoors.builder();
        var typeFuelGearboxHuDoorsArray = typeFuelGearboxHuDoorsString.split(",");

        for (String carFeature : typeFuelGearboxHuDoorsArray) {
            carFeature = carFeature.toLowerCase().trim();
            for (var knownCarFeature : stringToCarFeature.entrySet()) {
                if (carFeature.matches(knownCarFeature.getKey()) || carFeature.contains(knownCarFeature.getKey())) {
                    switch (knownCarFeature.getValue()) {
                        case VEHICLE_TYPE -> typeFuelGearboxHuDoors.vehicleType(VehicleType.fromString(carFeature));
                        case FUEL_TYPE -> fuelGearboxHuDoors.fuelType(carFeature);
                        case GEARBOX_TYPE -> fuelGearboxHuDoors.gearboxType(carFeature);
                        case VEHICLE_HU -> fuelGearboxHuDoors.vehicleHU(carFeature);
                        case DOORS_NUMBER -> fuelGearboxHuDoors.doorsNumber(carFeature);
                        default ->
                                logger.error("Car feature unknown. Update the code! typeFuelGearboxHuDoorsString: {}", typeFuelGearboxHuDoorsString);
                    }
                }
            }
        }
        typeFuelGearboxHuDoors.fuelGearboxHuDoors(fuelGearboxHuDoors.build());

        return typeFuelGearboxHuDoors.build();
    }

    private static HashMap<String, String> prepareCarFeatures() {
        HashMap<String, String> stringToCarFeature = new HashMap<>();
        stringToCarFeature.put("cabriolet", VEHICLE_TYPE);
        stringToCarFeature.put("roadster", VEHICLE_TYPE);
        stringToCarFeature.put("estate", VEHICLE_TYPE);
        stringToCarFeature.put("saloon", VEHICLE_TYPE);
        stringToCarFeature.put("small", VEHICLE_TYPE);
        stringToCarFeature.put("sports", VEHICLE_TYPE);
        stringToCarFeature.put("sport", VEHICLE_TYPE);
        stringToCarFeature.put("coupe", VEHICLE_TYPE);
        stringToCarFeature.put("suv", VEHICLE_TYPE);
        stringToCarFeature.put("off-road", VEHICLE_TYPE);
        stringToCarFeature.put("pickup", VEHICLE_TYPE);
        stringToCarFeature.put("van", VEHICLE_TYPE);
        stringToCarFeature.put("minibus", VEHICLE_TYPE);
        stringToCarFeature.put("other", VEHICLE_TYPE);
        stringToCarFeature.put("diesel", FUEL_TYPE);
        stringToCarFeature.put("petrol", FUEL_TYPE);
        stringToCarFeature.put("electric", FUEL_TYPE);
        stringToCarFeature.put("ethanol", FUEL_TYPE);
        stringToCarFeature.put("hybrid", FUEL_TYPE);
        stringToCarFeature.put("lpg", FUEL_TYPE);
        stringToCarFeature.put("natural", FUEL_TYPE);
        stringToCarFeature.put("gas", FUEL_TYPE);
        stringToCarFeature.put("hydrogen", FUEL_TYPE);
        stringToCarFeature.put("gearbox", GEARBOX_TYPE);
        stringToCarFeature.put("transmission", GEARBOX_TYPE);
        stringToCarFeature.put("automatic", GEARBOX_TYPE);
        stringToCarFeature.put("semi-automatic", GEARBOX_TYPE);
        stringToCarFeature.put("hu \\d{2}/\\d{4}", VEHICLE_HU);
        stringToCarFeature.put("new", VEHICLE_HU);
        stringToCarFeature.put("\\d/\\d doors", DOORS_NUMBER);
        return stringToCarFeature;
    }
}
