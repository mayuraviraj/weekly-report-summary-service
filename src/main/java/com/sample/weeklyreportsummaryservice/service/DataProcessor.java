package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.WeeklySummaryRequest;
import com.sample.weeklyreportsummaryservice.model.WeeklySummaryResponse;
import com.sample.weeklyreportsummaryservice.sbapi.SouthBoundAPIService;
import com.sample.weeklyreportsummaryservice.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DataProcessor {

    private final DataCacheService dataCacheService;
    private final DataFilter dataFilter;
    private final SouthBoundAPIService southBoundAPIService;

    public DataProcessor(DataCacheService dataCacheService, DataFilter dataFilter, SouthBoundAPIService southBoundAPIService) {
        this.dataCacheService = dataCacheService;
        this.dataFilter = dataFilter;
        this.southBoundAPIService = southBoundAPIService;
    }

    public Optional<WeeklySummaryResponse> getWeeklySummary(final WeeklySummaryRequest weeklySummaryRequest) {
        Set<RedisKey> redisKeys = Helper.getKeys(weeklySummaryRequest);
        if (!dataCacheService.areKeysAvailable(redisKeys)) {
            log.warn("Data is not available. Hence call 3rd party issue tracking api.");
            long currentTime = System.currentTimeMillis();
            southBoundAPIService.populateCache(weeklySummaryRequest.getProject_id());
            log.info("Calling 3rd party API took {}",
                    TimeUnit.SECONDS.convert((System.currentTimeMillis() - currentTime), TimeUnit.MILLISECONDS));
        }
        Optional<WeeklySummaryResponse> processedData = dataFilter.getProcessedData(weeklySummaryRequest,
                WarmUPCacheOperation.CALL_API_TO_WARM_UP_CACHE);
        if(processedData.isPresent()) {
            return processedData;
        } else {
            return dataFilter.getProcessedData(weeklySummaryRequest, WarmUPCacheOperation.SKIP_API_CALL);
        }
    }
}
