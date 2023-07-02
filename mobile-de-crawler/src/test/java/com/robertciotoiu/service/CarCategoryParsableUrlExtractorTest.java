package com.robertciotoiu.service;

import com.robertciotoiu.CarCategoryParsableUrlExtractor;
import com.robertciotoiu.MobileDeCrawlerApplication;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = MobileDeCrawlerApplication.class)
class CarCategoryParsableUrlExtractorTest {
    @Autowired
    CarCategoryParsableUrlExtractor carCategoryParsableUrlExtractor;
    private final static String URL_WITH_50_SUB_URLS = "src/test/resources/car-category-with-50-sub-urls.html";
    private final static String URL_WITH_0_SUB_URLS = "src/test/resources/car-category-with-0-sub-urls.html";
    private final static String URL_WITH_2_SUB_URLS = "src/test/resources/car-category-with-2-sub-urls.html";

    @Test
    void testUrlWith50SubUrls() throws IOException {
        File in = new File(URL_WITH_50_SUB_URLS);
        var doc = Jsoup.parse(in, null);

        Assertions.assertNotNull(doc, "Check the input file!");

        var links = carCategoryParsableUrlExtractor.extractParsableUrls("Dummy" ,doc);

        Assertions.assertNotNull(links);
        Assertions.assertEquals(50, links.size());
    }

    @Test
    void testUrlWith0SubUrls() throws IOException {
        File in = new File(URL_WITH_0_SUB_URLS);
        var doc = Jsoup.parse(in, null);

        Assertions.assertNotNull(doc, "Check the input file!");

        var links = carCategoryParsableUrlExtractor.extractParsableUrls("Dummy" ,doc);

        Assertions.assertNotNull(links);
        Assertions.assertEquals(1, links.size());
    }

    @Test
    void testUrlWith2SubUrls() throws IOException {
        File in = new File(URL_WITH_2_SUB_URLS);
        var doc = Jsoup.parse(in, null);

        Assertions.assertNotNull(doc, "Check the input file!");

        var links = carCategoryParsableUrlExtractor.extractParsableUrls("Dummy" ,doc);

        Assertions.assertNotNull(links);
        Assertions.assertEquals(2, links.size());
    }
}
