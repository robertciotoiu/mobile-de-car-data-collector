package com.robertciotoiu.service.extractor.category;

import com.robertciotoiu.PaginationHelper;
import com.robertciotoiu.data.model.category.CarCategoryMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.robertciotoiu.PaginationHelper.isAnySubpage;

@Component
public class CarCategoryMetadataExtractor {
    private static final Logger logger = LogManager.getLogger(CarCategoryMetadataExtractor.class);

    public CarCategoryMetadata extract(Document carSpecPageDoc, String carCategoryUrl) {
        var carCategoryMetadataBuilder = CarCategoryMetadata.builder();
        return carCategoryMetadataBuilder
                .url(carCategoryUrl)
                .totalListings(extractTotalListings(carSpecPageDoc))
                .totalPages(extractTotalPages(carSpecPageDoc))
                .scrapedTime(LocalDateTime.now(ZoneOffset.UTC))
                .build();
    }

    private Integer extractTotalListings(Document carSpecPageDoc) {
        try {
            var totalListingsString = carSpecPageDoc.selectXpath(CarCategoryXPaths.TOTAL_LISTINGS_XPATH).text();

            return extractTotalListings(totalListingsString);
        } catch (Exception e) {
            logger.error("Exception thrown trying to extract total number of listings", e);
        }
        return null;
    }

    private Integer extractTotalListings(String input) {
        Pattern pattern = Pattern.compile("(\\d[\\d.]*)(?:\\D|$)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String match = matcher.group(1);
            return Integer.parseInt(match.replace(".", ""));
        }

        return null;
    }

    private Integer extractTotalPages(Document carSpecPageDoc) {
        try {
            if (isAnySubpage(carSpecPageDoc)) {
                var totalPages = PaginationHelper.getLastPageElement(carSpecPageDoc).text();
                return Integer.parseInt(totalPages);
            } else {
                return 0;
            }
        } catch (Exception e) {
            logger.error("Exception thrown trying to extract total number of pages", e);
        }
        return null;
    }
}
