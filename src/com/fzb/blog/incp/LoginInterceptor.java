package com.fzb.blog.incp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fzb.blog.controlle.BaseControl;
import com.fzb.blog.controlle.QueryLogControl;
import com.fzb.blog.util.InstallUtil;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.kit.PathKit;

/**
 * @author zhengchangchun 登陆拦截器,负责权限控制
 */
public class LoginInterceptor extends PrototypeInterceptor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LoginInterceptor.class);

	public void doIntercept(ActionInvocation ai) {
		try {
			if (ai.getController() instanceof BaseControl) {
				HttpServletRequest request = ai.getController().getRequest();
				ai.getController().setAttr("requrl", request.getRequestURL());
				((BaseControl) ai.getController()).initData();
			}
			if (ai.getActionKey().startsWith("/post")
					|| ai.getActionKey().equals("/")) {
				visitorPermssion(ai);
			} else if (ai.getActionKey().startsWith("/api")) {
				apiPermssion(ai);
			} else if (ai.getActionKey().startsWith("/admin")) {
				adminPermssion(ai);
			} else if (ai.getActionKey().startsWith("/install")) {
				instanllPermssion(ai);
			} else {
				// 其他未知情况也放行
				ai.invoke();
			}
		} catch (Exception e) {
			LOGGER.error("permission incp exception ", e);
		}
	}

	private void visitorPermssion(ActionInvocation ai) {
		ai.invoke();
		if (ai.getController().getAttr("log") != null) {
			ai.getController().render(
					((QueryLogControl) ai.getController()).getTemplatePath()
							+ "/detail.jsp");
		} else if (ai.getController().getAttr("data") != null) {
			ai.getController().render(
					((QueryLogControl) ai.getController()).getTemplatePath()
							+ "/page.jsp");
		} else {
			ai.getController().render(
					((QueryLogControl) ai.getController()).getTemplatePath()
							+ "/index.jsp");
		}
	}

	private void apiPermssion(ActionInvocation ai) {
		ai.invoke();
		if (ai.getController().getAttr("log") != null) {
			ai.getController().renderJson(ai.getController().getAttr("log"));
		} else if (ai.getController().getAttr("data") != null) {
			ai.getController().renderJson(ai.getController().getAttr("data"));
		} else {
			Map<String, Object> error = new HashMap<String, Object>();
			error.put("status", 500);
			error.put("message", "unsupport");
			ai.getController().renderJson(error);
		}
	}

	private void instanllPermssion(ActionInvocation ai) {
		if (!new InstallUtil(PathKit.getWebRootPath() + "/WEB-INF")
				.checkInstall()) {
			ai.invoke();
		} else {
			ai.getController().getRequest().getSession();
			ai.getController().render("/install/forbidden.jsp");
		}
	}

	private void adminPermssion(ActionInvocation ai) {
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
							+ ai.getController().getRequest().getRequestURL());
		}
	}
}
