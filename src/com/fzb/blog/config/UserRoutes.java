package com.fzb.blog.config;

import com.fzb.blog.controlle.CommentControl;
import com.fzb.blog.controlle.LinkControl;
import com.fzb.blog.controlle.LogNavControl;
import com.fzb.blog.controlle.ManageLogControl;
import com.fzb.blog.controlle.PluginControl;
import com.fzb.blog.controlle.TagControl;
import com.fzb.blog.controlle.TemplateControl;
import com.fzb.blog.controlle.TypeControl;
import com.fzb.blog.controlle.UserControl;
import com.fzb.blog.controlle.WebSiteControl;
import com.jfinal.config.Routes;

/**
 * @author zhengchangchun 后台管理者路由
 */
public class UserRoutes extends Routes {
	public void config() {
		add("/admin", UserControl.class);
		add("/admin/link", LinkControl.class);
		add("/admin/comment", CommentControl.class);
		add("/admin/tag", TagControl.class);
		add("/admin/plugin", PluginControl.class);
		add("/admin/type", TypeControl.class);
		add("/admin/nav", LogNavControl.class);
		add("/admin/log", ManageLogControl.class);
		add("/admin/website", WebSiteControl.class);
		add("/admin/template", TemplateControl.class);
	}
}
