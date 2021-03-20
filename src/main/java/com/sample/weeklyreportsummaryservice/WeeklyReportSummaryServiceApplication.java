package com.sample.weeklyreportsummaryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WeeklyReportSummaryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeeklyReportSummaryServiceApplication.class, args);
	}

}
