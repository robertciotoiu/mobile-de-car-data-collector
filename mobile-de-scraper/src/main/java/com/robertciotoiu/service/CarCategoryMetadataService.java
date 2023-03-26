package com.robertciotoiu.service;

import com.robertciotoiu.data.repository.CarCategoryMetadataRepository;
import com.robertciotoiu.service.extractor.category.CarCategoryMetadataExtractor;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CarCategoryMetadataService {
    @Autowired
    CarCategoryMetadataRepository repository;
    @Autowired
    CarCategoryMetadataExtractor extractor;

    public void scrapeAndIngestCarCategoryMetadata(Document carSpecPage, String carSpecPageUrl) {
        if (isFirstPage(carSpecPageUrl)) {
            var carCategoryData = extractor.extract(carSpecPage, carSpecPageUrl);
            repository.save(carCategoryData);
        }
    }

    public static boolean isFirstPage(String carSpecPageUrl) {
        return !carSpecPageUrl.contains("pageNumber");
    }
}
