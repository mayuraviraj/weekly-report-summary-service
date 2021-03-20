package com.sample.weeklyreportsummaryservice.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class RedisKey implements Serializable {
    private final String project_id;
    private final String type;
    private final String state;

}
