package com.example.springwork.support.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ScheduledTasks {

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	// https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling
	// @Scheduled(cron="*/5 * * * * MON-FRI")
	// @Scheduled(fixedRate = 5000)
	// @Scheduled(initialDelay=1000, fixedRate=5000)
	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		log.info("The time is now {}", dateFormat.format(new Date()));
	}
}