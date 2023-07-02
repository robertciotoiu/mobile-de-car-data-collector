package com.robertciotoiu.service;

import com.robertciotoiu.cooldown.service.CategoryCooldownHandler;
import com.robertciotoiu.data.model.category.CarCategoryCooldown;
import com.robertciotoiu.data.repository.CarCategoryCooldownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryCooldownHandlerTest {

    @Mock
    private CarCategoryCooldownRepository repository;

    @InjectMocks
    private CategoryCooldownHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckCooldown_notFound() {
        String firstPageUrl = "https://example.com";
        Mockito.when(repository.findById(firstPageUrl)).thenReturn(Optional.empty());

        assertFalse(handler.hasCooldown(firstPageUrl));
        Mockito.verify(repository).findById(firstPageUrl);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCheckCooldown_notExpired() {
        String firstPageUrl = "https://example.com";
        LocalDateTime crawlTime = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(25);
        CarCategoryCooldown carCategoryCooldown =
                CarCategoryCooldown.builder()
                        .carCategoryUrl(firstPageUrl)
                        .crawlTime(crawlTime)
                        .cooldownMinutes(CategoryCooldownHandler.COOLDOWN_HOT_PAGES)
                        .build();
        Mockito.when(repository.findById(firstPageUrl)).thenReturn(Optional.of(carCategoryCooldown));

        assertTrue(handler.hasCooldown(firstPageUrl));
        Mockito.verify(repository).findById(firstPageUrl);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCheckCooldown_expired() {
        String firstPageUrl = "https://example.com";
        LocalDateTime crawlTime = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(CategoryCooldownHandler.COOLDOWN_HOT_PAGES + 1);
        CarCategoryCooldown carCategoryCooldown =
                CarCategoryCooldown.builder()
                        .carCategoryUrl(firstPageUrl)
                        .crawlTime(crawlTime)
                        .cooldownMinutes(CategoryCooldownHandler.COOLDOWN_HOT_PAGES)
                        .build();
        Mockito.when(repository.findById(firstPageUrl)).thenReturn(Optional.of(carCategoryCooldown));

        assertFalse(handler.hasCooldown(firstPageUrl));
        Mockito.verify(repository).findById(firstPageUrl);
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCalculateAndSetCooldown_lessThanMaxPages() {
        String firstPageUrl = "https://example.com";
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(null);

        handler.calculateAndSetCooldown(Collections.singletonList(firstPageUrl));

        Mockito.verify(repository).save(ArgumentMatchers.argThat(carCategoryCooldown ->
                carCategoryCooldown.getCarCategoryUrl().equals(firstPageUrl) &&
                        carCategoryCooldown.getCrawlTime() != null &&
                        carCategoryCooldown.getCooldownMinutes() == CategoryCooldownHandler.COOLDOWN_COLD_PAGES));
        Mockito.verifyNoMoreInteractions(repository);
    }

    @Test
    void testCalculateAndSetCooldown_maxPages() {
        String firstPageUrl = "https://example.com";
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(null);

        handler.calculateAndSetCooldown(Collections.nCopies(50 + 1, firstPageUrl));

        Mockito.verify(repository).save(ArgumentMatchers.argThat(carCategoryCooldown ->
                carCategoryCooldown.getCarCategoryUrl().equals(firstPageUrl) &&
                        carCategoryCooldown.getCrawlTime() != null &&
                        carCategoryCooldown.getCooldownMinutes() == CategoryCooldownHandler.COOLDOWN_HOT_PAGES));
        Mockito.verifyNoMoreInteractions(repository);
    }
}
