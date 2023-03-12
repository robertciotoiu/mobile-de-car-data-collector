package com.robertciotoiu.operational.service;

import com.robertciotoiu.operational.data.CarCategoryCooldown;
import com.robertciotoiu.operational.data.CarCategoryCooldownRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class CategoryCooldownHandler {
    private static final Logger logger = LogManager.getLogger(CategoryCooldownHandler.class);
    @Autowired
    private CarCategoryCooldownRepository repository;
    private static final int MAX_PAGES = 50;

    /**
     * Check if there is a cooldown or if it elapsed
     * @param firstPageUrl
     * @return
     */
    public boolean hasCooldown(String firstPageUrl) {
        var carCategoryCooldownOptional = repository.findById(firstPageUrl);
        if(carCategoryCooldownOptional.isEmpty()) {
            logger.info("No cooldown for URL: {}", firstPageUrl);
            return false;
        } else {
            var carCategoryCooldown = carCategoryCooldownOptional.get();
            var expectedCooldownEndTime = carCategoryCooldown.getCrawlTime().plusMinutes(carCategoryCooldown.getCooldownMinutes());
            var currentTime = LocalDateTime.now(ZoneOffset.UTC);
            var minutesLeft = Duration.between(currentTime, expectedCooldownEndTime).toMinutes();
            if(minutesLeft > 0){
                logger.info("Cooldown left: {} for URL: {}", minutesLeft, firstPageUrl);
                return true;
            } else{
                logger.info("Cooldown elapsed {} minutes ago for URL: {}", minutesLeft, firstPageUrl);
                return false;
            }
        }
    }

    /**
     * Set a *smart* cooldown time in MongoDB for this car category URL
     * @param parsableUrls
     */
    public void calculateAndSetCooldown(List<String> parsableUrls) {
        var carCategoryCooldownBuilder = CarCategoryCooldown.builder()
                .carCategoryUrl(parsableUrls.get(0))
                .crawlTime(LocalDateTime.now(ZoneOffset.UTC));
        if(parsableUrls.size() >= MAX_PAGES){
            carCategoryCooldownBuilder.cooldownMinutes(720);
            logger.info("Set 720 minutes cooldown for URL: {}", parsableUrls.get(0));
        } else {
            carCategoryCooldownBuilder.cooldownMinutes(60);
            logger.info("Set 60 minutes cooldown for URL: {}", parsableUrls.get(0));
        }
        repository.save(carCategoryCooldownBuilder.build());
    }
}
