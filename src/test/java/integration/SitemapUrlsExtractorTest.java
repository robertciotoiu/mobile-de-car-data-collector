package integration;

import dto.CarCategoryUrls;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import scraper.SitemapUrlsExtractor;

import java.io.IOException;
import java.util.List;

public class SitemapUrlsExtractorTest {

    private static SitemapUrlsExtractor underTest;

    @BeforeAll
    static void setUp() throws IOException {
        underTest = new SitemapUrlsExtractor();
    }

    @Test
    void shouldReturnListOfFoundLinks(){
        //given


        //when
        List<CarCategoryUrls> result = underTest.getAllCategoryUrls();

        //then
        Assertions.assertNotEquals(result.size(), 0);
    }

    @Test
    void checkCarCategoryUrlsDtoParentLink(){
        //given


        //when
        List<CarCategoryUrls> result = underTest.getAllCategoryUrls();

        //then
        Assertions.assertNotEquals(result.size(), 0);
        Assertions.assertNotNull(result.get(0).getParentLink());
    }

    @Test
    void checkCarCategoryUrlsDtoCategoryLinks(){
        //given


        //when
        List<CarCategoryUrls> result = underTest.getAllCategoryUrls();

        //then
        Assertions.assertNotEquals(result.size(), 0);
        Assertions.assertNotNull(result.get(0).getCategoryLink());
        Assertions.assertNotEquals(result.get(0).getCategoryLink().size(), 0);
    }
}
