package com.sample.weeklyreportsummaryservice.sbapi;

import com.sample.weeklyreportsummaryservice.model.GetIssueRequest;
import com.sample.weeklyreportsummaryservice.model.GetIssueResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "issuetracker", url = "${issue.tracker.api}")
public interface IssueTrackerApi {

    @RequestMapping(method = RequestMethod.GET, value = "/getissues")
    GetIssueResponse getIssue(final GetIssueRequest getIssueRequest);

}
