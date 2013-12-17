package com.bus.scheduler;

import java.util.Calendar;
import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class SimpleExample {

	public SimpleExample() {
		try {
			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();
			// define the job and tie it to our HelloJob class
			JobDetail job = JobBuilder.newJob(HelloJob.class)
					.withIdentity("job1", "group1").build();

			// compute a time that is on the next round minute
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND,cal.get(Calendar.SECOND)+5);
			

			//is scheduled to run every 20 seconds
			CronTrigger trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger1", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
				    .build();
			sched.scheduleJob(job, trigger);

			//is scheduled to run every other minute, starting at 15 seconds past the minute.
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job2", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger2", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("15/2 * * * * ?"))
				    .build();
			sched.scheduleJob(job, trigger);
			
			//is scheduled to every other minute, between 8am and 5pm (17 o'clock)
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job3", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger3", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/2 8-17 * * ?"))
				    .build();
			sched.scheduleJob(job, trigger);
			
			//is scheduled to run every three minutes but only between 5pm and 11pm
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job4", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger4", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0/3 17-23 * * ?"))
				    .build();
			sched.scheduleJob(job, trigger);
			
			//is scheduled to run at 10am on the 1st and 15th days of the month
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job5", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger5", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10am 1,15 * ?"))
				    .build();
			sched.scheduleJob(job, trigger);
			
			//is scheduled to run every 30 seconds on Weekdays (Monday through Friday)
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job6", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger6", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * MON-FRI"))
				    .build();
			sched.scheduleJob(job, trigger);
			
			//is scheduled to run every 30 seconds on Weekends (Saturday and Sunday)
			job = JobBuilder.newJob(HelloJob.class)
				    .withIdentity("job7", "group1")
				    .build();
			trigger = TriggerBuilder.newTrigger()
				    .withIdentity("trigger7", "group1")
				    .withSchedule(CronScheduleBuilder.cronSchedule("0,30 * * ? * SAT,SUN"))
				    .build();
			sched.scheduleJob(job, trigger);
			
//			sched.start();
//			Thread.sleep(30L * 1000L);
//			sched.shutdown(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
