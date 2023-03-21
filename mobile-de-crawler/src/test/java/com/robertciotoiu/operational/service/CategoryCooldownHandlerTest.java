package com.robertciotoiu.operational.service;

import com.robertciotoiu.operational.data.CarCategoryCooldown;
import com.robertciotoiu.operational.data.CarCategoryCooldownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(repository.findById(firstPageUrl)).thenReturn(Optional.empty());

        assertFalse(handler.hasCooldown(firstPageUrl));
        verify(repository).findById(firstPageUrl);
        verifyNoMoreInteractions(repository);
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
        when(repository.findById(firstPageUrl)).thenReturn(Optional.of(carCategoryCooldown));

        assertTrue(handler.hasCooldown(firstPageUrl));
        verify(repository).findById(firstPageUrl);
        verifyNoMoreInteractions(repository);
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
        when(repository.findById(firstPageUrl)).thenReturn(Optional.of(carCategoryCooldown));

        assertFalse(handler.hasCooldown(firstPageUrl));
        verify(repository).findById(firstPageUrl);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testCalculateAndSetCooldown_lessThanMaxPages() {
        String firstPageUrl = "https://example.com";
        when(repository.save(any())).thenReturn(null);

        handler.calculateAndSetCooldown(Collections.singletonList(firstPageUrl));

        verify(repository).save(argThat(carCategoryCooldown ->
                carCategoryCooldown.getCarCategoryUrl().equals(firstPageUrl) &&
                        carCategoryCooldown.getCrawlTime() != null &&
                        carCategoryCooldown.getCooldownMinutes() == CategoryCooldownHandler.COOLDOWN_HOT_PAGES));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testCalculateAndSetCooldown_maxPages() {
        String firstPageUrl = "https://example.com";
        when(repository.save(any())).thenReturn(null);

        handler.calculateAndSetCooldown(Collections.nCopies(50 + 1, firstPageUrl));

        verify(repository).save(argThat(carCategoryCooldown ->
                carCategoryCooldown.getCarCategoryUrl().equals(firstPageUrl) &&
                        carCategoryCooldown.getCrawlTime() != null &&
                        carCategoryCooldown.getCooldownMinutes() == CategoryCooldownHandler.COOLDOWN_COLD_PAGES));
        verifyNoMoreInteractions(repository);
    }
}
