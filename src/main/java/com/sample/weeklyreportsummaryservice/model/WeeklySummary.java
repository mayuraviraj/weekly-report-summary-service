package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class WeeklySummary {
    private String week;
    private List<StateSummary> state_summaries;
}
