package com.robertciotoiu.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Responsible to compute all the existing URLs from the last URL.
 */
@Component
public class Paginator {
    private static final Logger logger = LogManager.getLogger(Paginator.class);
    private static final String PAGE_NUMBER_URL_PATH = "pageNumber=";
    private static final String PAGE_NUMBER_REGEX = "pageNumber=(\\d+)";

    public List<String> computeAllUrls(String lastUrl) {
        var urls = new ArrayList<String>();
        int lastUrlNumber = extractPageNumber(lastUrl);
        for (int i = 2; i <= lastUrlNumber; i++) {
            String nextUrl = lastUrl.replace(PAGE_NUMBER_URL_PATH + "50", PAGE_NUMBER_URL_PATH + i);
            urls.add(nextUrl);
        }
        return urls;
    }

    private int extractPageNumber(String url) {
        Pattern pattern = Pattern.compile(PAGE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }
}
