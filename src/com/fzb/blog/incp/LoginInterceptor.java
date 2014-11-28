 package com.fzb.blog.incp;
 
 import com.fzb.blog.controlle.BaseControl;
 import com.fzb.blog.controlle.QueryLogControl;
 import com.jfinal.aop.PrototypeInterceptor;
 import com.jfinal.core.ActionInvocation;
 import com.jfinal.core.Controller;
 import javax.servlet.http.HttpSession;
 
 /**
 * @author zhengchangchun
 * 登陆拦截器,负责权限控制
 */
public class LoginInterceptor extends PrototypeInterceptor
 {
   public void doIntercept(ActionInvocation ai)
   {
	   if(ai.getController() instanceof BaseControl){
		   ((BaseControl)ai.getController()).initData();
	   }
     if (ai.getActionKey().startsWith("/post")) {
       ai.invoke();
       if (ai.getController().getAttr("log") != null) {
         ai.getController().render(((QueryLogControl)ai.getController()).getTemplatePath() + "/detail.jsp");
       }
       else if (ai.getController().getAttr("data") != null) {
         ai.getController().render(((QueryLogControl)ai.getController()).getTemplatePath() + "/page.jsp");
       }
       else{
    	   ai.getController().render(((QueryLogControl)ai.getController()).getTemplatePath() + "/index.jsp");
       }
     }
     else if (ai.getActionKey().startsWith("/api")) {
       ai.invoke();
       if (ai.getController().getAttr("log") != null) {
         ai.getController().renderJson(ai.getController().getAttr("log"));
       }
       else {
         ai.getController().renderJson(ai.getController().getAttr("data"));
       }
     }
     else if (ai.getActionKey().equals("/")) {
       ai.invoke();
       ai.getController().render(((QueryLogControl)ai.getController()).getTemplatePath() + "/index.jsp");
     }
     else if (ai.getActionKey().startsWith("/admin")) {
       if (ai.getController().getSession().getAttribute("user") != null) {
         ai.getController().setAttr("user", ai.getController().getSession().getAttribute("user"));
         ai.invoke();
       }
       else if (ai.getMethodName().equals("login")) {
         ai.invoke();
       }
       else {
         ai.getController().redirect("/admin/login");
       }
     }
     else{
    	 // 其他情况也放行
    	 ai.invoke();
     }
   }
 }

