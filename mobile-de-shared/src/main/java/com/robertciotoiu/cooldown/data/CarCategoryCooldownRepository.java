package com.robertciotoiu.cooldown.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarCategoryCooldownRepository extends MongoRepository<CarCategoryCooldown, String> {
}
