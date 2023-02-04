package com.robertciotoiu.service;

import com.robertciotoiu.connection.JsoupWrapper;
import com.robertciotoiu.exception.PaginationNotFoundError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Chrome uses XPath 1.0 => is it safe to use Jsoup.Element.selectXpath as it also uses XPath 1.0.
 */
public final class CarCategoryParsableUrlExtractor extends JsoupWrapper {
    private static final Logger logger = LogManager.getLogger(CarCategoryParsableUrlExtractor.class);
    private static final String EN_LANGUAGE_PATH = "&lang=en";
    private static final String PAGINATION_CLASS_NAME = "pagination";

    private CarCategoryParsableUrlExtractor(){throw new IllegalStateException("Static class");}
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
            urls.addAll(getUrls(firstPageUrl, doc));
        } catch (IOException e) {
            logger.warn("Cannot connect to this URL: {}. Exception: {}", firstPageUrl, e);
        }

        return urls;
    }

    public static List<String> getUrls(String firstPageUrl, Document doc) {
        var urls = new ArrayList<String>();
        urls.add(firstPageUrl);

        if (isAnySubpage(doc)) {
            String lastUrl = extractLastPageUrl(doc);
            urls.addAll(Paginator.computeAllUrls(lastUrl));
        }

        return urls;
    }

    private static boolean isAnySubpage(Document doc) {
        return !doc.getElementsByClass(PAGINATION_CLASS_NAME).isEmpty();
    }

    private static String extractLastPageUrl(Document doc) {
        var paginationElement = doc
                .getElementsByClass(PAGINATION_CLASS_NAME)
                .first();

        if(paginationElement == null){
            logger.error("Failed to find pagination element! Check CarCategoryParsableUrlExtractor and update XPATHS! Document: {}", doc);
            throw new PaginationNotFoundError("Failed to find pagination element! Check CarCategoryParsableUrlExtractor and update XPATHS! Stopping program execution...");
        }

        // Go to penultimate node, as the ultimate one is the next page button
        var paginationSize = paginationElement.childNodeSize() - 2;
        var lastUrlElement = paginationElement
                .childNode(paginationSize)
                .childNode(0);

        return lastUrlElement.attr("data-href") + EN_LANGUAGE_PATH;
    }
}
