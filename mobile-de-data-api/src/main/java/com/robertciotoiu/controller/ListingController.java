package com.robertciotoiu.controller;

import com.robertciotoiu.data.model.raw.Listing;
import com.robertciotoiu.data.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    ListingRepository listingRepository;

    @GetMapping
    public Slice<Listing> getListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return listingRepository.findBy(pageable);
    }

    @GetMapping("/{listingId}")
    public Listing getListing(@PathVariable String listingId) {
        return listingRepository.findById(listingId).orElse(null);
    }
}
