package com.fzb.blog.incp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.concurrent.ConcurrencyUtil;

import com.fzb.blog.controlle.BaseControl;
import com.fzb.blog.controlle.QueryLogControl;
import com.fzb.common.util.InstallUtil;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.PathKit;

/**
 * @author zhengchangchun 登陆拦截器,负责权限控制
 */
public class LoginInterceptor extends PrototypeInterceptor {
	public void doIntercept(ActionInvocation ai) {
		try{
			if (ai.getController() instanceof BaseControl) {
				HttpServletRequest request = ai.getController().getRequest();
				ai.getController().setAttr("requrl", request.getRequestURL());
				((BaseControl) ai.getController()).initData();
			}
			if (ai.getActionKey().startsWith("/post")) {
				ai.invoke();
				if (ai.getController().getAttr("log") != null) {
					ai.getController().render(
							((QueryLogControl) ai.getController())
									.getTemplatePath() + "/detail.jsp");
				} else if (ai.getController().getAttr("data") != null) {
					ai.getController().render(
							((QueryLogControl) ai.getController())
									.getTemplatePath() + "/page.jsp");
				} else {
					ai.getController().render(
							((QueryLogControl) ai.getController())
									.getTemplatePath() + "/index.jsp");
				}
			} else if (ai.getActionKey().startsWith("/api")) {
				ai.invoke();
				if (ai.getController().getAttr("log") != null) {
					ai.getController()
							.renderJson(ai.getController().getAttr("log"));
				} else if (ai.getController().getAttr("data") != null) {
					ai.getController().renderJson(
							ai.getController().getAttr("data"));
				} else {
					Map<String, Object> error = new HashMap<String, Object>();
					error.put("status", 500);
					error.put("message", "unsupport");
					ai.getController().renderJson(error);
				}
			} else if (ai.getActionKey().equals("/")) {
				ai.invoke();
				ai.getController().render(
						((QueryLogControl) ai.getController()).getTemplatePath()
								+ "/index.jsp");
			} else if (ai.getActionKey().startsWith("/admin")) {
	
				if (ai.getController().getSession().getAttribute("user") != null) {
					ai.getController().setAttr("user",
							ai.getController().getSession().getAttribute("user"));
					ai.invoke();
					// 存在消息提示
					if (ai.getController().getRequest().getAttribute("message") != null) {
						ai.getController().render("/admin/message.jsp");
					}
				} else if (ai.getMethodName().equals("login")) {
					ai.invoke();
				} else {
					ai.getController().redirect(
							ai.getController().getRequest().getContextPath()
									+ "/admin/login?redirectFrom="
									+ ai.getController().getRequest()
											.getRequestURL());
					// ai.getController().redirect("/admin/login");
				}
			}
	
			else if (ai.getActionKey().startsWith("/install")) {
				System.out.println(!new InstallUtil(PathKit.getWebRootPath()
						+ "/WEB-INF").checkInstall());
				if (!new InstallUtil(PathKit.getWebRootPath() + "/WEB-INF")
						.checkInstall()) {
					ai.invoke();
				} else {
					ai.getController().getRequest().getSession();
					ai.getController().render("/install/forbidden.jsp");
				}
			} else {
				// 其他情况也放行
				ai.invoke();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
