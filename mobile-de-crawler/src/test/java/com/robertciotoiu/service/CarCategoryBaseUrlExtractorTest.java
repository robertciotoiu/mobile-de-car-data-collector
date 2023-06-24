package com.robertciotoiu.service;

import com.robertciotoiu.MobileDeCrawlerApplication;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(classes = MobileDeCrawlerApplication.class)
class CarCategoryBaseUrlExtractorTest {
    @Autowired
    CarCategoryBaseUrlExtractor carCategoryBaseUrlExtractor;

    @Test
    void extractTest() throws IOException {
        File in = new File("src/test/resources/sitemap-pls-carspecification-0.xml");
        var doc = Jsoup.parse(in, null);
        Assertions.assertNotNull(doc, "Check the input file!");

        var extractedUrls = carCategoryBaseUrlExtractor.extract(doc);
        Assertions.assertEquals(5, extractedUrls.size());
    }
}
