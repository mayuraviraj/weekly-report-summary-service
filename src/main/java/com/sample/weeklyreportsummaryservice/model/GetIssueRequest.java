package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetIssueRequest {
    private String project_id;
}
