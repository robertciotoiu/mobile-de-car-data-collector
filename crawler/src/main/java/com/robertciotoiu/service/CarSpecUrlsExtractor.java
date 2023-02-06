package com.robertciotoiu.service;

import com.robertciotoiu.connection.JsoupWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Chrome uses XPath 1.0 => is it safe to use Jsoup.Element.selectXpath as it also uses XPath 1.0.
 */
public class CarSpecUrlsExtractor extends JsoupWrapper {
    private static final Logger logger = LogManager.getLogger(CarSpecUrlsExtractor.class);
    private static final String EN_LANGUAGE_PATH = "&lang=en";
    private static final String PAGINATION_XPATH = "/html/body/div[4]/div[1]/div[3]/div[4]/div[2]/div[2]/div[27]/div[3]/ul";
    private static final String SECOND_PAGE_URL_XPATH = "//*[@id=\"p-2\"]";
    private static final String LAST_PAGE_URL_XPATH = "/html/body/div[4]/div[1]/div[3]/div[4]/div[2]/div[2]/div[27]/div[3]/ul/li[7]/span";

    /**
     * Extracts all URLs for the given CarSpecification URL.
     * Example:
     * https://suchen.mobile.de/fahrzeuge/search.html?cn=DE&isSearchRequest=true&makeModelVariant1.makeId=25200&makeModelVariant1.modelId=9&pageNumber=2&ref=srpNextPage&scopeId=C&sortOption.sortBy=creationTime&sortOption.sortOrder=DESCENDING&refId=f98a7572-fc93-7e2d-1b0f-bd4d342636d4&lang=en
     * https://suchen.mobile.de/fahrzeuge/search.html?cn=DE&isSearchRequest=true&makeModelVariant1.makeId=25200&makeModelVariant1.modelId=9&pageNumber=3&ref=srpNextPage&scopeId=C&sortOption.sortBy=creationTime&sortOption.sortOrder=DESCENDING&refId=f98a7572-fc93-7e2d-1b0f-bd4d342636d4&lang=en
     * Observation: it is not sufficient to append pageNumber=2. We should
     *
     * @param firstPageUrl looks like: https://suchen.mobile.de/auto/mercedes-benz-clk-220.html?lang=en
     * @return all pages for this car category(between 1 and 50)
     */
    public static List<String> getUrls(String firstPageUrl) {
        var urls = new ArrayList<String>();

        try {
            var doc = getHtml(firstPageUrl);
            if (doc.selectXpath(PAGINATION_XPATH).isEmpty()) {
                urls.add(firstPageUrl);
            } else {
                String lastUrl = extractLastPageUrl(doc);
                urls.addAll(Paginator.computeAllUrls(lastUrl));
            }
        } catch (IOException e) {
            logger.warn("Cannot connect to this URL: {}. Exception: {}", firstPageUrl, e);
        }

        return urls;
    }

    private static String extractLastPageUrl(Document doc) {
        var lastUrlElement = doc.selectXpath(LAST_PAGE_URL_XPATH).first();

        String lastUrl = null;
        if (lastUrlElement != null)
            lastUrl = lastUrlElement.attr("data-href") + EN_LANGUAGE_PATH;

        return lastUrl;
    }
}
