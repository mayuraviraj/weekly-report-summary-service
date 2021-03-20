package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class IssueTrackerResponse {
    private String issue_id;
    private String type;
    private String current_state;
    private List<ChangeLog> changelogs;
}
