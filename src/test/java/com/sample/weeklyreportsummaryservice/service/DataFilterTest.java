package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.*;
import com.sample.weeklyreportsummaryservice.sbapi.SouthBoundAPIService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DataFilterTest {

    @Autowired
    DataFilter dataFilter;

    @MockBean
    SouthBoundAPIService southBoundAPIService;

    @MockBean
    DataCacheService dataCacheService;

    @Test
    public void testGetProcessDataWithEmptyRequest() {
        String project_id = UUID.randomUUID().toString();
        WeeklySummaryRequest weeklySummaryRequest = new WeeklySummaryRequest(project_id,
                "2017W01", "2017W02", Collections.EMPTY_LIST, Collections.EMPTY_LIST);
        Optional<WeeklySummaryResponse> processedData = dataFilter.getProcessedData(weeklySummaryRequest, WarmUPCacheOperation.CALL_API_TO_WARM_UP_CACHE);
        assertTrue(processedData.isPresent());
        assertTrue(processedData.get().getWeekly_summaries().isEmpty());
        assertEquals(project_id, processedData.get().getProject_id());

    }

    @Test
    public void testGetProcessDataWithRequest() {
        String project_id = UUID.randomUUID().toString();
        List<String> types = new ArrayList<>();
        types.add("bug");

        List<String> state = new ArrayList<>();
        state.add("open");
        WeeklySummaryRequest weeklySummaryRequest = new WeeklySummaryRequest(project_id,
                "2017W01", "2017W05", types, state);

        Mockito.when(dataCacheService.get(Mockito.anySet())).thenReturn(new HashSet<>());
        List<ChangeLog> changeLogs = new ArrayList<>();
        changeLogs.add(new ChangeLog(getDate(2017, 1), "open", "close"));
        Mockito.when(dataCacheService.get(Mockito.any(RedisKey.class))).thenReturn(new RedisValue("sample-issue", changeLogs));

        Optional<WeeklySummaryResponse> processedData = dataFilter.getProcessedData(weeklySummaryRequest, WarmUPCacheOperation.SKIP_API_CALL);
        assertTrue(processedData.isPresent());
        assertEquals(1, processedData.get().getWeekly_summaries().size());

        List<StateSummary> stateSummary = new ArrayList<>();
        List<Issue> issues = new ArrayList<>();
        issues.add(new Issue("sample-issue", "bug"));
        stateSummary.add(new StateSummary("open", 1, issues));
        WeeklySummary weeklySummaryExpected = new WeeklySummary("2017W5", stateSummary);

        assertEquals(weeklySummaryExpected, processedData.get().getWeekly_summaries().get(0));
    }

    private String getDate(int year, int month) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mma z");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 0);
        return formatter.format(calendar.getTime());
    }
}