package com.robertciotoiu.service.extractor.category;

import com.robertciotoiu.connection.JsoupWrapper;
import com.robertciotoiu.data.model.category.CarCategoryMetadata;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CarCategoryMetadataExtractorTest {

    @MockBean
    private JsoupWrapper jsoupWrapper;

    @Autowired
    private CarCategoryMetadataExtractor carCategoryMetadataExtractor;

    @Test
    void testExtract() throws IOException {
        String carSpecPageUrl = "http://www.google.com";
        File in = new File("src/test/resources/listings-with-multiple-ads.html");
        Document carSpecPageDoc = Jsoup.parse(in, null);
        when(jsoupWrapper.getHtml(anyString())).thenReturn(carSpecPageDoc);

        Optional<CarCategoryMetadata> result = carCategoryMetadataExtractor.extract(carSpecPageUrl);

        assertEquals(carSpecPageUrl, result.get().getUrl());
        assertEquals(5280, result.get().getTotalListings());
        assertEquals(50, result.get().getTotalPages());
        assertNotNull(result.get().getScrapedTime());

    }
}