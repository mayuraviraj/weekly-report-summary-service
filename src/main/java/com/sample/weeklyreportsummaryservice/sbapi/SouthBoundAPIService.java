package com.sample.weeklyreportsummaryservice.sbapi;

import com.sample.weeklyreportsummaryservice.model.GetIssueRequest;
import com.sample.weeklyreportsummaryservice.model.GetIssueResponse;
import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;
import com.sample.weeklyreportsummaryservice.service.DataCacheService;
import com.sample.weeklyreportsummaryservice.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class SouthBoundAPIService {

    private final IssueTrackerApi issueTrackerApi;
    private final DataCacheService dataCacheService;

    public SouthBoundAPIService(final IssueTrackerApi issueTrackerApi, final DataCacheService dataCacheService) {
        this.issueTrackerApi = issueTrackerApi;
        this.dataCacheService = dataCacheService;
    }

    public void populateCache(final String project) {
        GetIssueRequest getIssueRequest = new GetIssueRequest(project);
        GetIssueResponse issueResponse = issueTrackerApi.getIssue(getIssueRequest);
        log.info("OUTGOING API CALL Done -> /getissues - Parameters {}", getIssueRequest);
        Map<RedisKey, RedisValue> convertedDataSet = Helper.convert(issueResponse);
        dataCacheService.put(convertedDataSet);
    }
}
