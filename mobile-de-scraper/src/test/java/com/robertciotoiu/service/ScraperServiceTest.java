package com.robertciotoiu.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//This is used for debugging
@Disabled
@SpringBootTest
class ScraperServiceTest {
    @Autowired
    ScraperService scraperService;

    @Test
    void testScrape() {
        var urlToScrape = "https://suchen.mobile.de/fahrzeuge/search.html?cn=DE&isSearchRequest=true&makeModelVariant1.makeId=3500&makeModelVariant1.modelDescription=116i+advantage&makeModelVariant1.modelId=2&pageNumber=13&ref=srpNextPage&scopeId=C&sortOption.sortBy=creationTime&sortOption.sortOrder=DESCENDING&refId=e6d0e676-f00b-d7e9-b5f3-08ad73ea7e1e&lang=en";
        scraperService.scrape(urlToScrape);
    }
}