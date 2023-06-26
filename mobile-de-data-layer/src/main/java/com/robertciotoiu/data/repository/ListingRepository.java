package com.robertciotoiu.data.repository;

import com.robertciotoiu.data.model.raw.Listing;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends PagingAndSortingRepository<Listing, String>, MongoRepository<Listing, String> {
    // Avoids the non-indexed count done by Spring Jpa when performing findAll(Pageable)
    Slice<Listing> findBy(Pageable page);
}
