package com.fzb.blog.controlle;

import com.fzb.blog.model.Log;
import com.fzb.blog.model.Type;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

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
			if(getPara("key")==null){
				all();
				return;
			}
			try {
				key = URLDecoder.decode(getPara("key"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			setAttr("data", Log.dao.getLogsByTitleOrContent(1, 10, key));
		} else {
			try {
				key = URLDecoder.decode(getPara(0), "UTF-8");
				setAttr("data", Log.dao.getLogsByTitleOrContent(getParaToInt(1)
						.intValue(), 10, key));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}
		// 记录回话的Key
		setSessionAttr("key", key);
		setAttr("yurl", "post/search/" + key + "-");
	}

	public void record() {
		setAttr("data", Log.dao.getLogsByData(
				getParaToInt(1, Integer.valueOf(1)).intValue(), 10, getPara(0)));
		setAttr("yurl", "post/record/" + getPara(0) + "-");
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
				getParaToInt(1, Integer.valueOf(1)).intValue(), 10, getPara(0)));
		setAttr("yurl", "post/sort/" + getPara(0) + "-");
		setAttr("type",
				Type.dao.findFirst("select * from type where alias='"
						+ getPara(0) + "'"));
	}

	public void tag() {
		try {
			setAttr("data", Log.dao.getLogsByTag(
					getParaToInt(1, Integer.valueOf(1)).intValue(), 10,
					URLDecoder.decode(getPara(0), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setAttr("yurl", "post/tag/" + getPara(0) + "-");
	}

	public void all() {
		setAttr("data", Log.dao.getLogsByPage(
				getParaToInt(1, Integer.valueOf(1)).intValue(), 10));
		setAttr("yurl", "post/all-");
	}
}
