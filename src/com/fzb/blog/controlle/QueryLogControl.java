package com.fzb.blog.controlle;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.fzb.blog.model.Log;
import com.fzb.blog.model.Type;

public class QueryLogControl extends BaseControl {
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
			if(getPara("key")==null || "".equals(getPara("key"))){
				all();
				removeSessionAttr("key");
				return;
			}
			try {
				key = URLDecoder.decode(getPara("key"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			setAttr("data", Log.dao.getLogsByTitleOrContent(1, getDefaultRows(), key));
		} else {
			try {
				key = URLDecoder.decode(getPara(0), "UTF-8");
				setAttr("data", Log.dao.getLogsByTitleOrContent(getParaToInt(1)
						.intValue(), getDefaultRows(), key));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		// 记录回话的Key
		setSessionAttr("key", key);
		setAttr("yurl", "post/search/" + key + "-");
		
		setAttr("tipsType", "搜素");
		setAttr("tipsName", key);
	}

	public void record() {
		setAttr("data", Log.dao.getLogsByData(
				getParaToInt(1, Integer.valueOf(1)).intValue(), getDefaultRows(), getPara(0)));
		setAttr("yurl", "post/record/" + getPara(0) + "-");
		
		setAttr("tipsType", "存档");
		setAttr("tipsName", getPara(0));
	}

	public void detail() {
		Map log = new HashMap();
		Integer logId = null;
		try {
			logId = getParaToInt(0);
		} catch (NumberFormatException e) {
			logId = Integer.valueOf(Log.dao.getLogByLogIdAlias(getPara(0)));
		}
		log.putAll(Log.dao.getLogByLogId(logId.intValue()));
		log.put("lastLog", Log.dao.getLastLog(logId.intValue()));
		log.put("nextLog", Log.dao.getNextLog(logId.intValue()));
		setAttr("log", log);
	}

	public void sort() {
		setAttr("data", Log.dao.getLogsBySort(
				getParaToInt(1, Integer.valueOf(1)).intValue(), getDefaultRows(), getPara(0)));
		setAttr("yurl", "post/sort/" + getPara(0) + "-");
		setAttr("type",
				Type.dao.findFirst("select * from type where alias='"
						+ getPara(0) + "'"));
		setAttr("tipsType", "分类");
		Type t=Type.dao.findFirst("select * from type where alias='"+ getPara(0) + "'");
		if(t!=null){
			setAttr("tipsName", t.getStr("typeName"));
		}
	}

	public void tag() {
		try {
			setAttr("data", Log.dao.getLogsByTag(
					getParaToInt(1, Integer.valueOf(1)).intValue(), getDefaultRows(),
					URLDecoder.decode(getPara(0), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setAttr("yurl", "post/tag/" + getPara(0) + "-");
		setAttr("tipsType", "标签");
		try {
			setAttr("tipsName", URLDecoder.decode(getPara(0), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void all() {
		setAttr("data", Log.dao.getLogsByPage(
				getParaToInt(1, Integer.valueOf(1)).intValue(), getDefaultRows()));
		setAttr("yurl", "post/all-");
	}
}
