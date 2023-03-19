package com.robertciotoiu.service;

import com.robertciotoiu.connection.JsoupWrapper;
import com.robertciotoiu.operational.service.CategoryCooldownHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.robertciotoiu.PaginationHelper.getLastPageElement;
import static com.robertciotoiu.PaginationHelper.isAnySubpage;

/**
 * Chrome uses XPath 1.0 => is it safe to use Jsoup.Element.selectXpath as it also uses XPath 1.0.
 */
@Component
public class CarCategoryParsableUrlExtractor {
    private static final Logger logger = LogManager.getLogger(CarCategoryParsableUrlExtractor.class);
    private static final String EN_LANGUAGE_PATH = "&lang=en";

    @Autowired
    private Paginator paginator;
    @Autowired
    private JsoupWrapper jsoupWrapper;
    @Autowired
    CategoryCooldownHandler categoryCooldownHandler;

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
    public List<String> extractParsableUrls(String firstPageUrl) {
        var isAnyCooldown = categoryCooldownHandler.hasCooldown(firstPageUrl);
        if(isAnyCooldown)
            return Collections.emptyList();
        try {
            var doc = jsoupWrapper.getHtml(firstPageUrl);
            var parsableUrls = extractParsableUrls(firstPageUrl, doc);
            categoryCooldownHandler.calculateAndSetCooldown(parsableUrls);
            return parsableUrls;
        } catch (IOException e) {
            logger.warn("Cannot connect to this URL: {}. Exception: {}", firstPageUrl, e);
        }

        return Collections.emptyList();
    }

    public List<String> extractParsableUrls(String firstPageUrl, Document doc) {
        var urls = new ArrayList<String>();
        urls.add(firstPageUrl);

        if (isAnySubpage(doc)) {
            String lastUrl = extractLastPageUrl(doc);
            urls.addAll(paginator.computeAllUrls(lastUrl));
        }

        return urls;
    }

    private String extractLastPageUrl(Document doc) {
        var lastUrlElement = getLastPageElement(doc);
        return lastUrlElement.attr("data-href") + EN_LANGUAGE_PATH;
    }
}
