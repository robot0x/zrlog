package com.fzb.blog.config;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.fzb.common.util.MailJob;
import com.fzb.common.util.SiteMapJob;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.IPlugin;

public class QuartzPlugin implements IPlugin {
	private static Logger log=Logger.getLogger(QuartzPlugin.class);
	
	private Scheduler scher;

	public boolean start() {
		SchedulerFactory sf = new StdSchedulerFactory();
		try {
			this.scher = sf.getScheduler();
			JobDetail job = JobBuilder.newJob(MailJob.class)
					.withIdentity("sendMail", "util").build();
			JobDetail siteMapJob = JobBuilder.newJob(SiteMapJob.class)
					.withIdentity("siteMap", "util").build();

			/*job.getJobDataMap().put("webSite",
					JFinal.me().getServletContext().getAttribute("webSite"));
			WebSite site=WebSite.dao.findFirst("select value from webSite where name='home'");
			
			log.info("site address --> "+ site.get("value"));
			siteMapJob.getJobDataMap().put("webHome",
					site.getStr("value"));*/
			siteMapJob.getJobDataMap().put("webDiskPath",
					JFinal.me().getServletContext().getRealPath("sitemap.xml"));
			CronTrigger trigger = (CronTrigger) TriggerBuilder
					.newTrigger()
					.withIdentity("siteMap", "util")
					.withSchedule(
							CronScheduleBuilder.cronSchedule("0 15 23 * * ? *"))
					.build();

			this.scher.scheduleJob(siteMapJob, trigger);
			this.scher.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean stop() {
		try {
			if (!this.scher.isShutdown())
				this.scher.shutdown();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return true;
	}
}
