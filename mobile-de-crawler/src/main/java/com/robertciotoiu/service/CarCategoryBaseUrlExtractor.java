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
 * But to avoid confusing namings like CarSpec, we will call them Car Category URLs.
 */
public final class CarCategoryBaseUrlExtractor extends JsoupWrapper {
    private static final String EN_LANGUAGE_PATH = "?lang=en";
    private static final Logger logger = LogManager.getLogger(CarCategoryBaseUrlExtractor.class);

    private CarCategoryBaseUrlExtractor() {
        throw new IllegalStateException("Static class");
    }

    public static List<String> extract(String url) throws IOException {
        Document doc = getHtml(url);
        return extract(doc);
    }

    public static List<String> extract(Document doc) {
        var extractedUrls = new ArrayList<String>();
        Elements links = doc.select("loc");

        for (Element link : links) {
            var extractedUrl = link.text() + EN_LANGUAGE_PATH;
            extractedUrls.add(extractedUrl);
            logger.debug("Extracted: {}", extractedUrl);
        }

        logger.info("Extracted {} Car Category URLs", extractedUrls.size());

        return extractedUrls;
    }
}
