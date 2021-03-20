package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RedisValue implements Serializable {
    private String issueId;
    private List<ChangeLog> changeLog;
}
