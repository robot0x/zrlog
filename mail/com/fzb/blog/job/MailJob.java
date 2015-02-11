package com.fzb.blog.job;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.fzb.blog.util.plugin.ZrlogPublicQuery;
import com.fzb.common.util.Mail;

public class MailJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		new Mail(ZrlogPublicQuery.getwebSiteMap(),"","","定时备份邮件 "+new Date()).sendMail();
	}

}
