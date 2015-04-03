package com.fzb.blog.controlle;

import java.io.File;
import java.io.IOException;
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

	public void write() {
		render("/admin/edit.jsp");
	}
	
	public void addToSession(){
		Map<String, String[]> param = getRequest().getParameterMap();
		Log log = new Log();
		for (Map.Entry tmap : param.entrySet()) {
			log.set((String) tmap.getKey(), ((String[]) tmap.getValue())[0]);
		}
		int logId = log.getMaxRecord() + 1;
		((Log) log.set("userId",
				((User) getSessionAttr("user")).getInt("userId"))).set(
				"releaseTime", new Date());
		log.set("logId", Integer.valueOf(logId));

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
		} else {
			log.set("recommended", Boolean.valueOf(false));
		}

		// 自动摘要
		//System.out.println(param.get("digest"));
		if (param.get("digest") == null || "".equals(param.get("digest"))) {
			log.set("digest", log.get("content"));
		}
		
		setSessionAttr("log", log);
		Object map = new HashMap<String,Object>();
		((Map) map).put("add", Boolean.valueOf(true));
		renderJson(map);
	}

	public void update() {
		Integer logId = null;
		logId = Integer.parseInt(getPara("logId"));
		// compare tag
		String oldTagStr = Log.dao.findById(logId).get("keywords");
		Tag.dao.update(getPara("keywords"), oldTagStr);
		Log log = Log.dao.findById(logId);
		Map<String, String[]> param = getRequest().getParameterMap();
		for (Map.Entry tmap : param.entrySet()) {
			log.set((String) tmap.getKey(), ((String[]) tmap.getValue())[0]);
		}
		
		if (param.get("canComment") != null) {
			log.set("canComment", Boolean.valueOf(true));
		} else {
			log.set("canComment", Boolean.valueOf(false));
		}
		if (param.get("recommended") != null) {
			log.set("recommended", Boolean.valueOf(true));
		} else {
			log.set("recommended", Boolean.valueOf(false));
		}
		
		if (log.get("digest") == null || "".equals(log.get("digest"))) {
			log.set("digest",
					ParseTools.autoDigest(log.get("content").toString(), 200));
		} else {
			log.set("digest", getPara("digest"));
		}
		log.update();
		renderJson("OK");
	}

	public void preview() {
		Integer logId=null;
		Log log=null;
		if(getPara("logId")!=null){
			log=new Log().getLogByLogIdA(getPara("logId"));
			logId=log.getInt("logId");
		}
		else{
			Map<String, String[]> param = getRequest().getParameterMap();
			log= new Log();
			for (Map.Entry tmap : param.entrySet()) {
				log.set((String) tmap.getKey(), ((String[]) tmap.getValue())[0]);
			}
			logId = log.getMaxRecord() + 1;
			((Log) log.set("userId",
					((User) getSessionAttr("user")).getInt("userId"))).set(
					"releaseTime", new Date());
			log.set("logId", Integer.valueOf(logId));

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
			} else {
				log.set("recommended", Boolean.valueOf(false));
			}

			// 自动摘要
			if (log.get("digest") == null || "".equals(log.get("digest"))) {
				log.set("digest",
						ParseTools.autoDigest(log.get("content").toString(), 200));
			}
		}
		log.put("lastLog", Log.dao.getLastLog(logId));
		log.put("nextLog", Log.dao.getNextLog(logId));
		setAttr("log", log);
		render(getTemplatePath() + "/detail.jsp");
	}

	public void editFrame() {
		Map log = new HashMap();
		Integer logId = null;
		logId = Integer.parseInt(getPara("logId"));
		log.putAll(Log.dao.getLogByLogIdA(logId).getAttrs());
		log.put("lastLog", Log.dao.getLastLog(logId));
		log.put("nextLog", Log.dao.getNextLog(logId));
		setAttr("log", log);
		render("/admin/edit_frame.jsp");
	}

	public void delete() {
		Map<String, Object> log = Log.dao.getLogByLogId(getParaToInt("id")
				.intValue());
		Tag.dao.deleteTag(log.get("keywords").toString());
		Log.dao.deleteById(getParaToInt("id"));

		renderJson("OK");
	}

	public void add() {
		Map<String, String[]> param = getRequest().getParameterMap();
		Log log = new Log();
		for (Entry<String,String[]> tmap : param.entrySet()) {
			if(tmap.getValue().length>0){
				log.set((String) tmap.getKey(), ((String[]) tmap.getValue())[0]);
			}
		}
		int logId = log.getMaxRecord() + 1;
		((Log) log.set("userId",
				((User) getSessionAttr("user")).getInt("userId"))).set(
				"releaseTime", new Date());
		log.set("logId", Integer.valueOf(logId));
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
		} 
		else {
			log.set("recommended", Boolean.valueOf(false));
		}
		// 自动摘要
		if (log.get("digest") == null || "".equals(log.get("digest"))) {
			log.set("digest", ParseTools.autoDigest(log.get("content").toString(), 200));
		}
		//
		Tag.dao.insertTag(getPara("keywords"));
		Object map = new HashMap<String,Object>();
		((Map) map).put("add", Boolean.valueOf(log.save()));
		renderJson(map);
	}

	public void upload() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileExt = getFile("imgFile")
				.getFileName()
				.substring(getFile("imgFile").getFileName().lastIndexOf(".") + 1)
				.toLowerCase();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String url = "/attached/" + getPara("dir") + "/"
				+ sdf.format(new Date()) + "/" + df.format(new Date()) + "_"
				+ new Random().nextInt(1000) + "." + fileExt;
		IOUtil.moveOrCopyFile(PathKit.getWebRootPath()+"/attached/"+getFile("imgFile").getFileName(),PathKit.getWebRootPath() + url,true);
		getData().put("error", Integer.valueOf(0));

		// put to cloud
		String prefix = getStrValuebyKey("bucket_type");
		if (prefix != null) {
			BucketVO bucket = new BucketVO(getStrValuebyKey(prefix
					+ "_bucket"), getStrValuebyKey(prefix + "_access_key"),
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
}
