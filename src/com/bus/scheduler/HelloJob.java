package com.bus.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

public class HelloJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// Say Hello to the World and display the date/time
		JobKey jobKey = context.getJobDetail().getKey();
		System.out.println("SimpleJob says: " + jobKey + " executing at " + new Date());
	}
}
