package com.robertciotoiu.service;

import com.robertciotoiu.connection.JsoupWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts all the CarSpecification Urls from this sitemap: sitemap-pls-carspecification-0.xml
 * <p>
 * What is CarSpec/CarSpecification Url? It is a URL for a "category" extracted from the sitemap above.
 * It doesn't really define a category as it has multiple variations of each category for, for example for vw caddy, we
 * will have the following links:
 * https://suchen.mobile.de/auto/volkswagen-caddy-benzin.html
 * https://suchen.mobile.de/auto/volkswagen-caddy-maxi-trendline.html
 * https://suchen.mobile.de/auto/volkswagen-caddy-2007.html
 * etc.
 */
public class CarSpecSitemapUrlExtractor extends JsoupWrapper {
    private static final String URL = "https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml";
    private static final String EN_LANGUAGE_PATH = "?lang=en";
    private static final Logger logger = LogManager.getLogger(CarSpecSitemapUrlExtractor.class);

    private CarSpecSitemapUrlExtractor() {
        throw new IllegalStateException("Static class");
    }

    public static List<String> extract() throws IOException {
        var urls = new ArrayList<String>();
        Document doc = getHtml(URL);
        Elements links = doc.select("loc");

        for (Element link : links) {
            var url = link.text() + EN_LANGUAGE_PATH;
            urls.add(url);
            logger.debug("Extracted: {}", url);
        }

        logger.info("Extracted {} CarSpecification URLs", urls.size());

        return urls;
    }
}
