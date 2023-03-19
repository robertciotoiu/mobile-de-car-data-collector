package com.robertciotoiu;

import com.robertciotoiu.exception.PaginationNotFoundError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PaginationHelper {
    private static final Logger logger = LogManager.getLogger(PaginationHelper.class);
    public static final String PAGINATION_CLASS_NAME = "pagination";

    public static boolean isAnySubpage(Document doc) {
        return !doc.getElementsByClass(PAGINATION_CLASS_NAME).isEmpty();
    }

    public static Element getLastPageElement(Document doc) {
        var paginationElement = doc
                .getElementsByClass(PAGINATION_CLASS_NAME)
                .first();

        if(paginationElement == null){
            logger.error("Failed to find pagination element! Make sure you firstly call isAnySubpage method. Check PaginationHelper and update XPATHS! Document: {}", doc);
            throw new PaginationNotFoundError("Failed to find pagination element! Make sure you firstly call isAnySubpage method. Check PaginationHelper and update XPATHS! Stopping program execution...");
        }
        var paginationElements = paginationElement.childNodes().stream().filter(Element.class::isInstance).toList();

        // Go to penultimate node, as the ultimate one is the next page button
        var paginationSize = paginationElements.size() - 2;
        return (Element) paginationElements
                .get(paginationSize)
                .childNode(0);
    }
}
