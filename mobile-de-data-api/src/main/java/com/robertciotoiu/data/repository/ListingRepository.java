package com.robertciotoiu.data.repository;

import com.robertciotoiu.data.model.raw.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {
}
