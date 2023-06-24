package com.robertciotoiu.data.repository;

import com.robertciotoiu.data.model.raw.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends PagingAndSortingRepository<Listing, String>, MongoRepository<Listing, String> {

}
