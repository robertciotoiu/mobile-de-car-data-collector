package com.robertciotoiu.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CrawlerService {
    private static final Logger logger = LogManager.getLogger(CrawlerService.class);
    private static final String URL = "https://suchen.mobile.de/fahrzeuge/sitemap-pls-carspecification-0.xml";
    @Autowired
    private RunningModeStrategyFactory runningModeStrategyFactory;
    @Value("${running-mode:new_only}")
    private String runningModeString;
    @Value("${scheduler.fixed-delay}")
    private String fixedDelayString;

    @Scheduled(fixedDelayString = "${scheduler.fixed-delay}", timeUnit = TimeUnit.MINUTES)
    private void crawl() {

        logger.info("Start new crawling in {} running mode.", runningModeString);
        var runningMode = RunningMode.fromString(runningModeString);
        RunningModeStrategy runningModeStrategy = runningModeStrategyFactory.getRunningModeStrategy(runningMode);
        runningModeStrategy.execute(URL);
        logger.info("Crawler sleep for {} minutes.", fixedDelayString);
    }
}
