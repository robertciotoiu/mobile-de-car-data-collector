package scraper;

import connection.JsoupWrapper;
import dto.CarCategoryUrls;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SitemapUrlsExtractor {
    private static final String SITEMAP_URL = "https://www.mobile.de/sitemap.xml";
    private final JsoupWrapper jsoupWrapper = new JsoupWrapper();
    private final Logger logger = LogManager.getLogger(this.getClass());

    public List<CarCategoryUrls> getAllCategoryUrls() {
        //sitemap.xml was removed. We cannot get CarSpecsUrls anymore from there.
        //until it's back we'll use getHardcodedCarSpecUrls method.
        List<String> carSpecUrls = getHardCodedCarSpecUrls();

//        if(carSpecUrls == null){
//            return null;
//        }

        return carSpecUrls.stream()
                .map(this::getCarCategoryUrls)
                .toList();
    }

    private List<String> getHardCodedCarSpecUrls() {
        return List.of(
                "https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml",
                "https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-1.xml"
        );
    }

    /**
     * Extract all URLs from SITEMAP_URL that are like:
     * - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml
     * - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-1.xml
     * - https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-2.xml
     *
     * @return
     */
    private List<String> getCarSpecUrls() {
        Document doc;

        try {
            doc = jsoupWrapper.getHtml(SITEMAP_URL);
            return doc.getElementsByTag("loc")
                    .stream()
                    .map(Element::text)
                    .filter(text -> text.contains("carspecification"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Failed access:" + SITEMAP_URL + " to get car categories URLs.\n", e);
        }

        return Collections.emptyList();
    }

    /**
     * Extract all car category URLs from each car specification url.
     * Url should look like:
     * - https://suchen.mobile.de/auto/bmw-135-coupe-135i.html
     * - https://suchen.mobile.de/auto/opel-corsa-2000.html
     * - https://suchen.mobile.de/auto/bmw-3er-reihe-autogas-lpg.html
     *
     * @param url
     * @return
     */
    private Optional<Object> getCarCategoryUrls(String url) {
        try {
            //http get request to get html of the URL
            Document carSpecUrl = jsoupWrapper.getHtml(url);

            //parse and map content
            var categoryLinks = carSpecUrl.getElementsByTag("loc")
                    .stream()
                    .map(Element::text).toList();

            //store parsed data into a CarCategoryUrls object
            CarCategoryUrls carCategoryUrls = new CarCategoryUrls(url);
            carCategoryUrls.setCategoryLink(categoryLinks);

            return Optional.of(carCategoryUrls);
        } catch (IOException e) {
            logger.error("Failed access:" + url + " to get car categories URLs.\n", e);
        }
        return Optional.empty();
    }
}
