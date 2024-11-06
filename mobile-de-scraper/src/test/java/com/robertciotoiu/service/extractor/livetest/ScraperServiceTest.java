package com.robertciotoiu.service.extractor.livetest;

import static org.mockito.Mockito.*;

import com.robertciotoiu.cooldown.service.CategoryCooldownHandler;
import com.robertciotoiu.service.CarCategoryMetadataService;
import com.robertciotoiu.service.ScraperService;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@ContextConfiguration(classes = JsoupWrapper.class)
@SpringBootTest
class ScraperServiceTest {

    @MockBean
    CarCategoryMetadataService carCategoryMetadataService;

    @MockBean
    CategoryCooldownHandler categoryCooldownHandler;

    @Autowired
    ScraperService scraperService;

    @Test
    void testScrape() throws IOException {
        String urlToScrape = "https://suchen.mobile.de/auto/volkswagen-golf-limousine.html?lang=en";//todo: set a category that 100% will contain data in future
//        Document mockDocument = mock(Document.class);

//        when(jsoupWrapper.getHtml(urlToScrape)).thenReturn(mockDocument);
        when(carCategoryMetadataService.isFirstPage(urlToScrape)).thenReturn(false);
        when(categoryCooldownHandler.hasCooldown(urlToScrape)).thenReturn(false);


        scraperService.scrape(urlToScrape);

//        verify(jsoupWrapper, times(1)).getHtml(urlToScrape);
//        verify(listingService, times(1)).scrapeAndIngestListings(mockDocument, urlToScrape);
//        verify(carCategoryMetadataService, times(1)).scrapeAndIngestCarCategoryMetadata(mockDocument, urlToScrape);
//        verify(categoryCooldownHandler, times(1)).calculateAndSetCooldown(anyList());
    }
}