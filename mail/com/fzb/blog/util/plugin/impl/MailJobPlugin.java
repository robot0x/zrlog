package com.fzb.blog.util.plugin.impl;

import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.fzb.blog.job.MailJob;
import com.fzb.blog.util.plugin.ZrlogPublicAction;
import com.fzb.blog.util.plugin.ZrlogPublicQuery;
import com.fzb.blog.util.plugin.api.IZrlogPlugin;

public class MailJobPlugin implements IZrlogPlugin{

	private Scheduler scher;
	
	public MailJobPlugin(){
		
	}
	
	@Override
	public boolean start() {
		System.out.println("plugin start");
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			this.scher = sf.getScheduler();
			JobDetail mailJob = JobBuilder.newJob(MailJob.class)
					.withIdentity("sendMail", "util").build();
			/*job.getJobDataMap().put("webSite",
					JFinal.me().getServletContext().getAttribute("webSite"));
			WebSite site=WebSite.dao.findFirst("select value from webSite where name='home'");
			
			log.info("site address --> "+ site.get("value"));
			siteMapJob.getJobDataMap().put("webHome",
					site.getStr("value"));*/
			CronTrigger trigger = (CronTrigger) TriggerBuilder
					.newTrigger()
					.withIdentity("sendMail", "util")
					.withSchedule(CronScheduleBuilder.cronSchedule(ZrlogPublicQuery.getwebSiteMap().get("mail_services_cron").toString()))
					.build();
			
			this.scher.scheduleJob(mailJob, trigger);
			this.scher.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public boolean stop() {
		return false;
	}

	@Override
	public boolean install(Map<String, Object> arg0) {
		for (Entry<String, Object> param : arg0.entrySet()) {
			ZrlogPublicAction.storeToWebSite(param.getKey(), (String) param.getValue());
		}
		System.out.println("plugin install "+ arg0);
		return false;
	}

	@Override
	public boolean unstall() {
		// TODO Auto-generated method stub
		return false;
	}

}
