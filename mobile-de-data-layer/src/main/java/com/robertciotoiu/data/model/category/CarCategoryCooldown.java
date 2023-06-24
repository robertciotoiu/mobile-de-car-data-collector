package com.robertciotoiu.data.model.category;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "operational_car_categories_cooldown")
public class CarCategoryCooldown {
    @Id
    String carCategoryUrl;
    LocalDateTime crawlTime;
    int cooldownMinutes;
}
