 package com.fzb.common.util;
 
 import com.jfinal.core.JFinal;
 import java.util.Map;
 import javax.servlet.ServletContext;
 import org.quartz.Job;
 import org.quartz.JobDetail;
 import org.quartz.JobExecutionContext;
 import org.quartz.JobExecutionException;
 import org.quartz.JobKey;
 
 public class MailJob
   implements Job
 {
   public void execute(JobExecutionContext arg0)
     throws JobExecutionException
   {
     if (JFinal.me().getServletContext().getAttribute("webSite") != null)
       new Mail((Map)JFinal.me().getServletContext().getAttribute("webSite"), "50400814@qq.com", arg0.getJobDetail().getKey().getName(), null, null).sendMail();
   }
 }

