package com.fzb.blog.controlle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fzb.blog.model.Comment;
import com.fzb.blog.model.Log;
import com.fzb.blog.model.Type;
import com.fzb.blog.util.WebTools;
import com.fzb.common.util.ParseTools;

public class QueryLogControl extends BaseControl {

	private static final Logger log = LoggerFactory.getLogger(QueryLogControl.class);

	public void index() {
		if ((getRequest().getServletPath().startsWith("/post"))
				&& (getPara(0) != null)) {
			if (getPara(0).equals("all")) {
				all();
			} else if (getPara(0) != null) {
				detail();
			}
		} else {
			all();
		}
	}

	public void search() {
		String key = "";
		if (getParaToInt(1) == null) {
			if (isNotNullOrNotEmptyStr(getPara("key"))) {
				key = convertRequestParam(getPara("key"));
				setAttr("data",
						Log.dao.getLogsByTitleOrContent(1, getDefaultRows(), key));
			}
			else{
				all();
				removeSessionAttr("key");
				return;
			}
			
		} else {
			key = convertRequestParam(getPara(0));
			setAttr("data", Log.dao.getLogsByTitleOrContent(getParaToInt(1)
					.intValue(), getDefaultRows(), key));
		}
		// 记录回话的Key
		setSessionAttr("key", key);
		setAttr("yurl", "post/search/" + key + "-");

		setAttr("tipsType", "搜索");
		setAttr("tipsName", key);
	}

	public void record() {
		setAttr("data", Log.dao.getLogsByData(
				getParaToInt(1, Integer.valueOf(1)).intValue(),
				getDefaultRows(), getPara(0)));

		setAttr("yurl", "post/record/" + getPara(0) + "-");
		setAttr("tipsType", "存档");
		setAttr("tipsName", getPara(0));
	}

	public void addComment() {
		// FIXME　如何过滤垃圾信息
		if (getPara("userComment") != null) {
			new Comment().set("userHome", getPara("userHome"))
					.set("userMail", getPara("userMail"))
					.set("userIp", WebTools.getRealIp(getRequest()))
					.set("userName", getPara("userName"))
					.set("logId", getPara("logId"))
					.set("userComment", getPara("userComment"))
					.set("commTime", new Date()).set("hide", 1).save();
		}
		detail(getPara("logId"));
	}

	public void detail() {
		detail(getPara());
	}

	private void detail(Object id) {
		Map<String, Object> log = new HashMap<String, Object>();
		Map<String, Object> data = Log.dao.getLogByLogId(id);
		if (data != null) {
			Integer logId = (Integer) data.get("logId");
			log.putAll(Log.dao.getLogByLogId(logId));
			Log.dao.clickChange((Integer) logId);
			log.put("lastLog", Log.dao.getLastLog(logId.intValue()));
			log.put("nextLog", Log.dao.getNextLog(logId.intValue()));
			log.put("comments", Comment.dao.getCommentsByLogId(logId));
			setAttr("log", log);
		}
	}

	public void sort() {
		setAttr("data", Log.dao.getLogsBySort(
				getParaToInt(1, Integer.valueOf(1)).intValue(),
				getDefaultRows(), getPara(0)));
		setAttr("yurl", "post/sort/" + getPara(0) + "-");
		Type t = Type.dao.findByAlias(getPara(0));

		setAttr("type", t);
		setAttr("tipsType", "分类");
		if (t != null) {
			setAttr("tipsName", t.getStr("typeName"));
		}
	}

	public void tag() {
		if (getPara(0) != null) {
			String tag = convertRequestParam(getPara(0));
			setAttr("data", Log.dao.getLogsByTag(
					getParaToInt(1, Integer.valueOf(1)).intValue(),
					getDefaultRows(), tag));

			setAttr("yurl", "post/tag/" + getPara(0) + "-");
			setAttr("tipsType", "标签");
			setAttr("tipsName", tag);
		}
	}

	public void all() {
		int page = ParseTools.strToInt(getPara(1), 1);
		setAttr("data", Log.dao.getLogsByPage(page, getDefaultRows()));
		setAttr("yurl", "post/all-");
	}

	private String convertRequestParam(String param) {
		if (param != null) {
			try {
				return URLDecoder.decode(param, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("request convert to UTF-8 error ", e);
			}
		}
		return "";
	}
}
