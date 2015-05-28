package com.fzb.blog.controlle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import com.fzb.blog.model.Log;
import com.fzb.blog.model.Tag;
import com.fzb.blog.model.User;
import com.fzb.common.util.IOUtil;
import com.fzb.common.util.ParseTools;
import com.fzb.io.api.FileManageAPI;
import com.fzb.yunstore.BucketVO;
import com.fzb.yunstore.QiniuBucketManageImpl;
import com.jfinal.kit.PathKit;

public class ManageLogControl extends ManageControl {

	public void timeline() {
		render("/admin/ext/timeline.jsp");
	}

	public void update() {
		Integer logId = Integer.parseInt(getPara("logId"));
		// compare tag
		String oldTagStr = Log.dao.findById(logId).get("keywords");
		Tag.dao.update(getPara("keywords"), oldTagStr);
		getLog().update();
		Object map = new HashMap<String, Object>();
		((Map) map).put("update", true);
		renderJson(map);
		return;
	}

	public void preview() {
		Integer logId = null;
		Log log = null;
		if (getPara("logId") != null) {
			log = new Log().getLogByLogIdA(getPara("logId"));
		} else {
			log = getLog();
		}
		logId = log.getInt("logId");
		log.put("lastLog", Log.dao.getLastLog(logId));
		log.put("nextLog", Log.dao.getNextLog(logId));
		setAttr("log", log);
		render(getTemplatePath() + "/detail.jsp");
	}

	public void editFrame() {
		Integer logId = Integer.parseInt(getPara("logId"));
		setAttr("log", Log.dao.getLogByLogIdA(logId).getAttrs());
		render("/admin/edit_frame.jsp");
	}

	public void delete() {

		String ids[] = getPara("id").split(",");
		for (String id : ids) {
			delete(id);
		}
		Object map = new HashMap<String, Object>();
		((Map) map).put("delete", true);
		renderJson(map);
	}

	private void delete(Object logId) {
		Log log = Log.dao.getLogByLogIdA(logId);
		if (log != null && log.get("keywords") != null) {
			Tag.dao.deleteTag(log.get("keywords").toString());
		}
		Log.dao.deleteById(log.get("logId"));
	}

	public void add() {
		Log log = getLog();
		boolean result = false;
		if ("rubbish".equals(getPara("scope"))) {
			log.set("rubbish", true);
			getSession().setAttribute("log", log);
		} else {
			Tag.dao.insertTag(getPara("keywords"));
		}
		Log tlog = Log.dao.findById(log.getInt("logId"));
		if (tlog != null) {
			result = log.update();
		} else {
			result = log.save();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("add", result);
		map.put("logId", log.get("logId"));
		renderJson(map);
	}

	public void upload() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileExt = getFile("imgFile")
				.getFileName()
				.substring(
						getFile("imgFile").getFileName().lastIndexOf(".") + 1)
				.toLowerCase();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String url = "/attached/" + getPara("dir") + "/"
				+ sdf.format(new Date()) + "/" + df.format(new Date()) + "_"
				+ new Random().nextInt(1000) + "." + fileExt;
		IOUtil.moveOrCopyFile(PathKit.getWebRootPath() + "/attached/"
				+ getFile("imgFile").getFileName(), PathKit.getWebRootPath()
				+ url, true);
		getData().put("error", Integer.valueOf(0));

		// put to cloud
		String prefix = getStrValuebyKey("bucket_type");
		if (prefix != null) {
			BucketVO bucket = new BucketVO(
					getStrValuebyKey(prefix + "_bucket"),
					getStrValuebyKey(prefix + "_access_key"),
					getStrValuebyKey(prefix + "_secret_key"),
					getStrValuebyKey(prefix + "_host"));
			FileManageAPI man = new QiniuBucketManageImpl(bucket);
			String nurl = man
					.create(new File(PathKit.getWebRootPath() + url), url)
					.get("url").toString();
			getData().put("url", nurl);
		} else {
			if (getRequest().getContextPath() != null) {
				url = getRequest().getContextPath() + url;
			}
			getData().put("url", url);
		}
		renderJson(getData());
	}

	@Override
	public void queryAll() {
		renderJson(Log.dao.queryAll(getParaToInt("page").intValue(),
				getParaToInt("rows").intValue()));
	}

	private Log getLog() {
		Map<String, String[]> param = getRequest().getParameterMap();
		Log log = new Log();
		Integer logId = null;
		for (Entry<String, String[]> tmap : param.entrySet()) {
			if (tmap.getValue().length > 0) {
				if ("logId".equals(tmap.getKey())) {
					log.set("logId",
							Integer.parseInt(((String[]) tmap.getValue())[0]));
					continue;
				} else if (!"scope".equals(tmap.getKey())) {
					log.set((String) tmap.getKey(),
							((String[]) tmap.getValue())[0]);
				}
			}
		}
		logId = log.getInt("logId");
		if (logId == null) {
			logId = log.getMaxRecord() + 1;
			log.set("logId", Integer.valueOf(logId));
		}
		((Log) log.set("userId",
				((User) getSessionAttr("user")).getInt("userId"))).set(
				"releaseTime", new Date());
		log.set("private", false);
		log.set("rubbish", false);
		if (param.get("alias") == null) {
			log.set("alias", Integer.valueOf(logId));
		}
		if (param.get("canComment") != null) {
			log.set("canComment", Boolean.valueOf(true));
		} else {
			log.set("canComment", Boolean.valueOf(false));
		}
		if (param.get("recommended") != null) {
			log.set("recommended", Boolean.valueOf(true));
		}
		if (param.get("private") != null) {
			log.set("private", true);
		}
		if (param.get("rubbish") != null) {
			log.set("rubbish", true);
		} else {
			log.set("recommended", Boolean.valueOf(false));
		}
		// 自动摘要
		if (log.get("digest") == null || "".equals(log.get("digest"))) {
			log.set("digest",
					ParseTools.autoDigest(log.get("content").toString(), 200));
		}
		return log;
	}
}
