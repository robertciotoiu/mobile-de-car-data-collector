package com.robertciotoiu;

import com.robertciotoiu.service.CarSpecUrlsExtractor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Problem: data could change over time.
 * Solution: download the html locally.
 */
class CarSpecUrlsExtractorTest {
    private final static String URL_WITH_50_SUB_URLS = "https://suchen.mobile.de/auto/volkswagen-caddy.html";
    private final static String URL_WITH_0_SUB_URLS = "https://suchen.mobile.de/auto/volkswagen-caddy.html";
    private final static String URL_WITH_2_SUB_URLS = "https://suchen.mobile.de/auto/mercedes-benz-clk-220.html";

    @Test
    void testUrlWith50SubUrls() {
        var links = CarSpecUrlsExtractor.getUrls(URL_WITH_50_SUB_URLS);

        Assertions.assertNotNull(links);
        Assertions.assertEquals(50, links.size());
    }

    @Test
    void testUrlWith0SubUrls() {
        var links = CarSpecUrlsExtractor.getUrls(URL_WITH_0_SUB_URLS);

        Assertions.assertNotNull(links);
        Assertions.assertEquals(0, links.size());
    }
}
