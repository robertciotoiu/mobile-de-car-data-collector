package scraper;

import dto.CarCategoryUrls;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SitemapUrlsExtractor {
    private static final String SITEMAP_URL = "https://www.mobile.de/sitemap.xml";
    private final Document doc;

    public SitemapUrlsExtractor() throws IOException {
        this.doc = Jsoup.connect(SITEMAP_URL).get();
    }


    public List<CarCategoryUrls> getAllCategoryUrls() {
        List<String> carSpecUrls = getCarSpecUrls();

        return carSpecUrls.stream()
                .map(this::getCarCategoryUrls)
                .toList();
    }

    private List<String> getCarSpecUrls() {
        return doc.getElementsByTag("loc")
                .stream()
                .map(Element::text)
                .filter(text -> text.contains("carspecification"))
                .collect(Collectors.toList());
    }

    private CarCategoryUrls getCarCategoryUrls(String url) {
        try {
            CarCategoryUrls carCategoryUrls = new CarCategoryUrls(url);
            Document carSpecUrl = Jsoup.connect(url).get();

            var categoryLinks = carSpecUrl.getElementsByTag("loc")
                    .stream()
                    .map(Element::text).toList();
            carCategoryUrls.setCategoryLink(categoryLinks);

            return carCategoryUrls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
