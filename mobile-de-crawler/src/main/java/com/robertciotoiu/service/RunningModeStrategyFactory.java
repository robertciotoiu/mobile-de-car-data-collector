package com.robertciotoiu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RunningModeStrategyFactory {
    private final Map<RunningMode, RunningModeStrategy> strategyMap;

    @Autowired
    public RunningModeStrategyFactory(FullRunningModeStrategy fullRunningModeStrategy,
                                      NewOnlyRunningModeStrategy newOnlyRunningModeStrategy) {
        strategyMap = Map.of(RunningMode.FULL, fullRunningModeStrategy,
                RunningMode.NEW_ONLY, newOnlyRunningModeStrategy);
    }

    public RunningModeStrategy getRunningModeStrategy(RunningMode mode) {
        return strategyMap.get(mode);
    }
}
