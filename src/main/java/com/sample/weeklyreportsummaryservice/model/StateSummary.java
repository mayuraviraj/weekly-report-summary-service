package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StateSummary {
    private String state;
    private int count;
    private List<Issue> issues;

    public void setIssueCount(int issueCount) {
        count = issueCount;
    }
}
