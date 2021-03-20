package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Issue {
    private String issue_id;
    private String type;
}
