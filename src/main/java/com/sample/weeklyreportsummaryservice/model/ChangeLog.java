package com.sample.weeklyreportsummaryservice.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ChangeLog implements Serializable {
    private String changed_on;
    private String from_state;
    private String to_state;
}
