package com.robertciotoiu.data.repository;

import com.robertciotoiu.data.model.category.CarCategoryCooldown;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryCooldownRepository extends MongoRepository<CarCategoryCooldown, String> {
}
