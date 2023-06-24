package com.robertciotoiu.data.model.category;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "car_category_meta_data")
public class CarCategoryMetadata {
    @Id
    String url;
    Integer totalListings;
    Integer totalPages;
    LocalDateTime scrapedTime;
}
