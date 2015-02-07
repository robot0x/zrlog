package com.fzb.blog.controlle;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.fzb.blog.model.Comment;
import com.fzb.blog.model.Link;
import com.fzb.blog.model.Log;
import com.fzb.blog.model.LogNav;
import com.fzb.blog.model.Plugin;
import com.fzb.blog.model.Tag;
import com.fzb.blog.model.Type;
import com.fzb.blog.model.WebSite;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.ehcache.CacheInterceptor;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.CacheName;

public class BaseControl extends Controller {
	private String templatePath;

	private Integer rows;

	private Map<String, Object> webSite;

	@Before({ CacheInterceptor.class })
	@CacheName("/post/initData")
	public void initData() {
		Map<String, Object> init = CacheKit.get("/post/initData", "initData");
		if (init == null) {
			init = new HashMap<String, Object>();
			init.put("webSite", WebSite.dao.getWebSite());
			init.put("links", Link.dao.queryAll());
			init.put("comments", Comment.dao.noRead(1, 5));
			init.put("types", Type.dao.queryAll());
			init.put(
					"logNavs",
					LogNav.dao.queryAll(getRequest().getScheme() + "://"
							+ getRequest().getHeader("host")
							+ getRequest().getContextPath()));
			init.put("plugins", Plugin.dao.queryAll());
			init.put("archives", Log.dao.getArchives());
			init.put("tags", Tag.dao.queryAll());
			init.put("hotLog", Log.dao.getLogsByPage(1, 6));
			List<Type> types = Type.dao.queryAll();
			Map<Map<String, Object>, List<Log>> indexHotLog = new LinkedHashMap<Map<String, Object>, List<Log>>();
			for (Type type : types) {
				Map<String, Object> typeMap = new TreeMap<String, Object>();
				typeMap.put("typeName", type.getStr("typeName"));
				typeMap.put("alias", type.getStr("alias"));

				/*
				 * out Integer count=1; if(count<=3){ typeMap.put("listId",
				 * count++); } else{ count=1; typeMap.put("listId",1); }
				 */
				indexHotLog.put(
						typeMap,
						(List<Log>) Log.dao.getLogsBySort(1, 6,
								type.getStr("alias")).get("rows"));
			}
			init.put("indexHotLog", indexHotLog);
			CacheKit.put("/post/initData", "initData", init);
			JFinal.me().getServletContext()
					.setAttribute("webSite", init.get("webSite"));
		}
		setAttr("init", init);
		this.templatePath = ((Map<String, Object>) init.get("webSite")).get(
				"template").toString();
		this.rows = Integer
				.parseInt(((Map<String, Object>) init.get("webSite")).get(
						"rows").toString());

		//
		webSite = (Map<String, Object>) init.get("webSite");
	}

	public String getTemplatePath() {
		return this.templatePath;
	}

	public Integer getDefaultRows() {
		return this.rows;
	}

	public Object getValuebyKey(String key) {
		if (webSite.get(key) != null) {
			return webSite.get(key).toString();
		}
		return null;
	}

	public String getStrValuebyKey(String key) {
		if (webSite.get(key) != null) {
			return webSite.get(key).toString();
		}
		return null;
	}

	public static void refreshCache() {
		CacheKit.remove("/post/initData", "initData");
	}
}
