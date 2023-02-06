package com.robertciotoiu.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible to compute all the existing URLs from the last URL.
 */
public class Paginator {
    private static final Logger logger = LogManager.getLogger(Paginator.class);
    private static final String PAGE_NUMBER_URL_PATH = "pageNumber=";

    private Paginator() {
        throw new IllegalStateException("Static class");
    }

    public static List<String> computeAllUrls(String lastUrl) {
        var urls = new ArrayList<String>();
        int lastUrlNumber = extractPageNumber(lastUrl);
        for (int i = 2; i <= lastUrlNumber; i++) {
            String nextUrl = lastUrl.replace(PAGE_NUMBER_URL_PATH + "50", PAGE_NUMBER_URL_PATH + i);
            urls.add(nextUrl);
        }
        return urls;
    }

    public static int extractPageNumber(String url) {
        String pageNumberString = url.substring(url.indexOf(PAGE_NUMBER_URL_PATH) + PAGE_NUMBER_URL_PATH.length());
        return Integer.parseInt(pageNumberString);
    }
}
