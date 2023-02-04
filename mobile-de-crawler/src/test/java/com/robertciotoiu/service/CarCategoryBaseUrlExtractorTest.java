package com.robertciotoiu.service;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class CarCategoryBaseUrlExtractorTest {
    @Test
    void extractTest() throws IOException {
        File in = new File("src/test/resources/sitemap-pls-carspecification-0.xml");
        var doc = Jsoup.parse(in, null);
        Assertions.assertNotNull(doc, "Check the input file!");

        var extractedUrls = CarCategoryBaseUrlExtractor.extract(doc);
        Assertions.assertEquals(5, extractedUrls.size());
    }
}
