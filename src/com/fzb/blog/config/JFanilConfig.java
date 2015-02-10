package com.fzb.blog.config;

import com.fzb.blog.controlle.APIControl;
import com.fzb.blog.controlle.InstallControl;
import com.fzb.blog.controlle.QueryLogControl;
import com.fzb.blog.incp.LoginInterceptor;
import com.fzb.blog.model.Comment;
import com.fzb.blog.model.Link;
import com.fzb.blog.model.Log;
import com.fzb.blog.model.LogNav;
import com.fzb.blog.model.Plugin;
import com.fzb.blog.model.Tag;
import com.fzb.blog.model.Type;
import com.fzb.blog.model.User;
import com.fzb.blog.model.WebSite;
import com.fzb.blog.util.plugin.QuartzPlugin;
import com.fzb.common.util.InstallUtil;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.i18n.I18N;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;

/**
 * @author zhengchangchun JFinal 一些参数的配置
 */
public class JFanilConfig extends JFinalConfig {
	public void configConstant(Constants con) {
		con.setDevMode(true);
		con.setViewType(ViewType.JSP);
		con.setEncoding("utf-8");
		con.setI18n("i18n");
		I18N.init("i18n", null, null);
		con.setError404View("/error/404.html");
		con.setError500View("/error/404.html");
		con.setUploadedFileSaveDirectory(PathKit.getWebRootPath() + "/attached");
	}

	public void configHandler(Handlers handlers) {
		handlers.add(new JspSkipHandler());
	}

	public void configInterceptor(Interceptors incp) {
		incp.add(new SessionInViewInterceptor());
		incp.add(new LoginInterceptor());
	}

	public void configPlugin(Plugins plugins) {
		// 启动时候进行数据库链接,
		loadPropertyFile("db.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbcUrl"),
				getProperty("user"), getProperty("password"));
		try {
			// 如果不存在 install.lock 文件的情况下不初始化数据
			if (new InstallUtil(PathKit.getWebRootPath() + "/WEB-INF")
					.checkInstall()) {
			}
			plugins.add(c3p0Plugin);
			ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
			arp.addMapping("user", "userId", User.class);
			arp.addMapping("log", "logId", Log.class);
			arp.addMapping("type", "typeId", Type.class);
			arp.addMapping("link", "linkId", Link.class);
			arp.addMapping("comment", "commentId", Comment.class);
			arp.addMapping("lognav", "navId", LogNav.class);
			arp.addMapping("website", "siteId", WebSite.class);
			arp.addMapping("plugin", "pluginId", Plugin.class);
			arp.addMapping("tag", "tagId", Tag.class);
			// 添加表与实体的映射关系
			plugins.add(arp);
			// 添加QuartzPlugin 用于定时生成 sitemap.xml
			plugins.add(new EhCachePlugin());
			plugins.add(new QuartzPlugin());
			JFinal.me().getServletContext().setAttribute("plugins", plugins);
		} catch (Exception e) {

		}

	}

	public void configRoute(Routes routes) {
		// 添加浏览者能访问Control 路由
		routes.add("/post", QueryLogControl.class);
		routes.add("/api", APIControl.class);
		routes.add("/", QueryLogControl.class);
		routes.add("/install", InstallControl.class);
		// 后台管理者
		routes.add(new UserRoutes());
	}
}