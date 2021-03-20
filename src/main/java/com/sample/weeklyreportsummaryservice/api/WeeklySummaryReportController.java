package com.sample.weeklyreportsummaryservice.api;

import com.sample.weeklyreportsummaryservice.model.WeeklySummaryRequest;
import com.sample.weeklyreportsummaryservice.model.WeeklySummaryResponse;
import com.sample.weeklyreportsummaryservice.service.DataProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Slf4j
public class WeeklySummaryReportController {

    private final DataProcessor dataProcessor;

    public WeeklySummaryReportController(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    @PostMapping("/getweeklysummary")
    public ResponseEntity<WeeklySummaryResponse> getWeeklySummary(@RequestBody final WeeklySummaryRequest weeklySummaryRequest) {
        log.debug("Received weekly summary request with {}", weeklySummaryRequest);
        Optional<WeeklySummaryResponse> weeklySummaryResponseOptional = dataProcessor.getWeeklySummary(weeklySummaryRequest);
        if (weeklySummaryResponseOptional.isPresent()) {
            return ResponseEntity.ok(weeklySummaryResponseOptional.get());
        } else {
            log.warn("Weekly summary not found for {}", weeklySummaryRequest);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
