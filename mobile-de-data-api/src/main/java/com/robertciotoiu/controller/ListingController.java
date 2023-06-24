package com.robertciotoiu.controller;

import com.robertciotoiu.data.model.raw.Listing;
import com.robertciotoiu.data.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/listings")
public class ListingController {

    @Autowired
    ListingRepository listingRepository;

    @GetMapping
    public Page<Listing> getListings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return listingRepository.findAll(pageable);
    }
}
