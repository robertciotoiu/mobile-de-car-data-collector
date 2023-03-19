package com.robertciotoiu.data.repository;

import com.robertciotoiu.data.model.category.CarCategoryMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryMetadataRepository extends MongoRepository<CarCategoryMetadata, String> {
}