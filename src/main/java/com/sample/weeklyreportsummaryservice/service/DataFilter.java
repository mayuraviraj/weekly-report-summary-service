package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.*;
import com.sample.weeklyreportsummaryservice.sbapi.SouthBoundAPIService;
import com.sample.weeklyreportsummaryservice.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DataFilter {

    private final DataCacheService dataCacheService;
    private final SouthBoundAPIService southBoundAPIService;

    public DataFilter(DataCacheService dataCacheService, SouthBoundAPIService southBoundAPIService) {
        this.dataCacheService = dataCacheService;
        this.southBoundAPIService = southBoundAPIService;
    }

    public Optional<WeeklySummaryResponse> getProcessedData(final WeeklySummaryRequest weeklySummaryRequest,
                                                            final WarmUPCacheOperation warmUPCacheOperation) {
        Map<String, Calendar> weeksInBetween = Helper.getWeeksInBetween(weeklySummaryRequest.getFrom_week(),
                weeklySummaryRequest.getTo_week());
        Set<RedisKey> redisKeys = Helper.getKeys(weeklySummaryRequest);

        Map<String, WeeklySummary> allWeeklySummaries = getAllWeeksSummaries(weeksInBetween);

        Map<String, WeeklySummary> filteredWeeklySummary = new HashMap<>();

        String weekString = null;
        for (RedisKey redisKey : redisKeys) {
            int issueCount = 0;
            StateSummary stateSummary = new StateSummary(redisKey.getState(), 0, new ArrayList<>());
            RedisValue redisValue = dataCacheService.get(redisKey);
            if (redisValue != null) {
                Issue issue = null;
                for (ChangeLog changeLog : redisValue.getChangeLog()) {
                    weekString = Helper.getWeekString(changeLog.getChanged_on());

                    WeeklySummary weeklySummary = allWeeklySummaries.get(weekString);
                    if (weeklySummary == null) {
                        if (warmUPCacheOperation.equals(WarmUPCacheOperation.CALL_API_TO_WARM_UP_CACHE)) {
                            southBoundAPIService.populateCache(weeklySummaryRequest.getProject_id());
                            //return empty hoping we get second call to run through processing logic
                            return Optional.empty();
                        }
                    } else {
                        weeklySummary.getState_summaries().add(stateSummary);

                        filteredWeeklySummary.put(weekString, weeklySummary);

                        if (weeksInBetween.containsKey(weekString)) {
                            issue = new Issue(redisValue.getIssueId(), redisKey.getType());
                            stateSummary.getIssues().add(issue);
                            issueCount++;
                            break;
                        }
                    }
                }
                if (issue == null) {
                    log.warn("No issue was found for week {}", weekString);
                    if (warmUPCacheOperation.equals(WarmUPCacheOperation.CALL_API_TO_WARM_UP_CACHE)) {
                        southBoundAPIService.populateCache(weeklySummaryRequest.getProject_id());
                        //return empty hoping we get second call to run through processing logic
                        return Optional.empty();
                    }
                }
                stateSummary.setIssueCount(issueCount);
            } else {
                log.warn("No data was found for {}", redisKey);
                if (warmUPCacheOperation.equals(WarmUPCacheOperation.CALL_API_TO_WARM_UP_CACHE)) {
                    southBoundAPIService.populateCache(weeklySummaryRequest.getProject_id());
                    //return empty hoping we get second call to run through processing logic
                    return Optional.empty();
                }
            }
        }

        WeeklySummaryResponse weeklySummaryResponse = new WeeklySummaryResponse(weeklySummaryRequest.getProject_id(),
                new ArrayList<>(filteredWeeklySummary.values()));

        return Optional.of(weeklySummaryResponse);

    }

    private Map<String, WeeklySummary> getAllWeeksSummaries(final Map<String, Calendar> weeksInBetween) {
        Map<String, WeeklySummary> allWeeklySummaries = new HashMap<>();
        for (Map.Entry<String, Calendar> weeksStringWithCalendarEntry : weeksInBetween.entrySet()) {
            WeeklySummary weeklySummary = new WeeklySummary(weeksStringWithCalendarEntry.getKey(), new ArrayList<>());
            allWeeklySummaries.put(weeksStringWithCalendarEntry.getKey(), weeklySummary);
        }
        return allWeeklySummaries;
    }

}
