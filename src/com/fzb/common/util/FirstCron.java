 package com.fzb.common.util;
 
 import org.quartz.CronScheduleBuilder;
 import org.quartz.CronTrigger;
 import org.quartz.JobBuilder;
 import org.quartz.JobDetail;
 import org.quartz.Scheduler;
 import org.quartz.SchedulerFactory;
 import org.quartz.TriggerBuilder;
 import org.quartz.impl.StdSchedulerFactory;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 public class FirstCron
 {
   public void run()
     throws Exception
   {
     Logger log = LoggerFactory.getLogger(FirstCron.class);
 
     SchedulerFactory sf = new StdSchedulerFactory();
     Scheduler scher = sf.getScheduler();
 
     JobDetail job = JobBuilder.newJob(MailJob.class)
       .withIdentity("job1", "group1")
       .build();
 
     CronTrigger trigger = 
       (CronTrigger)TriggerBuilder.newTrigger()
       .withIdentity("trigger1", "group1")
       .withSchedule(CronScheduleBuilder.cronSchedule("0 * 15 * * ?"))
       .build();
 
     scher.scheduleJob(job, trigger);
 
 
     scher.start();
   }
 
   public static void main(String[] args)
   {
     try
     {
       new FirstCron().run();
     } catch (Exception e) {
       e.printStackTrace();
     }
   }
 }

