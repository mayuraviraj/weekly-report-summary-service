package com.sample.weeklyreportsummaryservice.util;

import com.sample.weeklyreportsummaryservice.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helper {


    //project_id, type, state
    public static Set<RedisKey> getKeys(WeeklySummaryResponse weeklySummaryResponse) {
        Set<RedisKey> redisKeys = new LinkedHashSet<>();
        weeklySummaryResponse.getWeekly_summaries().forEach( weeklySummary -> {
            weeklySummary.getState_summaries().forEach( stateSummary -> {
                stateSummary.getIssues().forEach(issue ->{
                    redisKeys.add(new RedisKey(weeklySummaryResponse.getProject_id(), issue.getType(), stateSummary.getState()));
                });
            });
        });
        return redisKeys;
    }

    public static Set<RedisKey> getKeys(WeeklySummaryRequest weeklySummaryRequest) {
        Set<RedisKey> redisKeys = new LinkedHashSet<>();
        for (String type : weeklySummaryRequest.getTypes()) {
            for (String state : weeklySummaryRequest.getStates()) {
                redisKeys.add(new RedisKey(weeklySummaryRequest.getProject_id(), type, state));
            }
        }
        return redisKeys;
    }

    public static Map<RedisKey, RedisValue> convert(final GetIssueResponse issueResponse) {
        String projectId = issueResponse.getProject_id();
        List<IssueTrackerResponse> issues = issueResponse.getIssues();
        Map<RedisKey, RedisValue> keyValueStore = new HashMap<>();
        for (IssueTrackerResponse issue : issues) {
            String type = issue.getType();
            RedisKey redisKey = new RedisKey(projectId, type, issue.getCurrent_state());
            RedisValue redisValue = new RedisValue(issue.getIssue_id(), issue.getChangelogs());
            keyValueStore.put(redisKey, redisValue);
        }
        return keyValueStore;
    }

    // 2017W01, 2017W03
    public static Map<String,Calendar> getWeeksInBetween(String fromWeek, String toWeek) {
        String[] split = fromWeek.split("W");
        String[] ws = toWeek.split("W");

        Calendar fromCal = Calendar.getInstance();
        fromCal.set(Calendar.YEAR, Integer.parseInt(split[0]));
        fromCal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(split[1]));

        Calendar toCal = Calendar.getInstance();
        toCal.set(Calendar.YEAR, Integer.parseInt(ws[0]));
        toCal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(ws[1]));

        boolean isToDatePassedFrom = false;

        Map<String,Calendar> weeksInBetween = new HashMap<>();

        while (!isToDatePassedFrom){
            fromCal.add(Calendar.WEEK_OF_YEAR, 1);
            if (fromCal.after(toCal)) {
                isToDatePassedFrom = true;
            } else {
                weeksInBetween.put(fromCal.getWeekYear() + "W" + fromCal.get(Calendar.WEEK_OF_YEAR),
                        fromCal);
            }
        }

        return weeksInBetween;
    }

    private static Pair<Integer,Integer> getYearAndWeekNo(String weekWithYear){
        String[] split = StringUtils.split("W");
        return Pair.of(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    public static String getWeekString(String dateString) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mma z");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(dateString));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return calendar.getWeekYear() + "W" + calendar.get(Calendar.WEEK_OF_YEAR);
    }
}
