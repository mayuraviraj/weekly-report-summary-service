package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeeklySummaryRequest {
    private String project_id;
    private String from_week;
    private String to_week;
    private List<String> types;
    private List<String> states;

}
