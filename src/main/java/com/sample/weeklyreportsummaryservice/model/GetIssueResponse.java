package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GetIssueResponse {
    private String project_id;
    private List<IssueTrackerResponse> issues;

}
