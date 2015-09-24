package com.fzb.blog.incp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fzb.blog.controlle.BaseControl;
import com.fzb.blog.util.WebTools;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.JFinal;

public class BlackListInterceptor extends PrototypeInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BlackListInterceptor.class);

	@Override
	public void doIntercept(ActionInvocation ai) {
		if (ai.getController() instanceof BaseControl) {
			BaseControl baseControl = (BaseControl) ai.getController();
			String ipStr = baseControl.getStrValuebyKey("blackList");
			if (ipStr != null) {
				Set<String> ipSet = new HashSet<String>(Arrays.asList(ipStr.split(",")));
				String requestIP = WebTools.getRealIp(baseControl.getRequest());
				if (ipSet.contains(requestIP)) {
					baseControl.render(JFinal.me().getConstants().getErrorView(403));
				} else {
					ai.invoke();
				}
			} else {
				ai.invoke();
			}
		}
		else{
			ai.invoke();
		}
	}
}
