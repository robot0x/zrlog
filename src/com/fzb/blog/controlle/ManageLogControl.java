package com.fzb.blog.controlle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import com.fzb.api.io.bucket.FileManageAPI;
import com.fzb.blog.model.Log;
import com.fzb.blog.model.Tag;
import com.fzb.blog.model.User;
import com.fzb.common.util.ParseTools;
import com.fzb.yunstore.BucketVO;
import com.fzb.yunstore.QiniuBucketManageImpl;
import com.jfinal.kit.PathKit;

public class ManageLogControl extends ManageControl
{
  public void list()
  {
     renderJson(Log.dao.getList(getParaToInt("page").intValue(), getParaToInt("rows").intValue()));
  }

  public void write() {
     render("/admin/edit.jsp");
  }
  public void update(){
	  Integer logId = null;
      logId =Integer.parseInt(getPara("logId"));
	  // compare tag
      System.out.println(logId);
      System.out.println(getPara("keywords"));
      String oldTagStr=Log.dao.findById(logId).get("keywords");
      System.out.println(oldTagStr);
	  Tag.dao.update(getPara("keywords"), oldTagStr);
	  Log log=Log.dao.findById(logId);
	  log.set("content", getPara("content"));
	  log.set("keywords", getPara("keywords"));
	  
	  if (getPara("digest") == null || "".equals(getPara("digest"))) {
	       log.set("digest",ParseTools.autoDigest(log.get("content").toString(),200));
	  }
	  else{
		  log.set("digest",getPara("digest"));
	  }
	 log.update();
	 renderJson("OK");
  }
  public void oper() {
     renderJson("OK");
  }
  public void editFrame(){
	  Map log = new HashMap();
     Integer logId = null;
      logId =Integer.parseInt(getPara("logId"));
     log.putAll(Log.dao.getLogByLogId(logId));
     log.put("lastLog", Log.dao.getLastLog(logId));
     log.put("nextLog", Log.dao.getNextLog(logId));
     setAttr("log", log);
	 render("/admin/edit_frame.jsp");
  }

  public void delete(){
	  Map<String,Object> log = Log.dao.getLogByLogId(getParaToInt("id").intValue());
      Tag.dao.deleteTag(log.get("keywords").toString());
      Log.dao.deleteById(getParaToInt("id"));
      
      renderJson("OK");
  }

  public void add() {
     Map<String,String[]> param = getRequest().getParameterMap();
     Log log = new Log();
     for (Map.Entry tmap : param.entrySet()) {
       log.set((String)tmap.getKey(), ((String[])tmap.getValue())[0]);
    }
     int logId = log.getMaxRecord() + 1;
     ((Log)log.set("userId", ((User)getSessionAttr("user")).getInt("userId"))).set("releaseTime", new Date());
     log.set("logId", Integer.valueOf(logId));

     if (param.get("alias") == null) {
       log.set("alias", Integer.valueOf(logId));
    }
     if (param.get("canComment") != null) {
       log.set("canComment", Boolean.valueOf(true));
    }
    else {
       log.set("canComment", Boolean.valueOf(false));
    }
     if (param.get("recommended") != null) {
       log.set("recommended", Boolean.valueOf(true));
    }
    else {
       log.set("recommended", Boolean.valueOf(false));
    }
     
     // 自动摘要
     if (param.get("digest") == null || "".equals(param.get("digest"))) {
       log.set("digest", log.get("content"));
    }

     Object map = new HashMap();
     ((Map)map).put("add", Boolean.valueOf(log.save()));
     // 
     Tag.dao.insertTag(getPara("keywords"));
     renderJson(map);
  }

  public void upload() {
     SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
     String fileExt = getFile("imgFile").getFileName().substring(getFile("imgFile").getFileName().lastIndexOf(".") + 1).toLowerCase();
     SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
     String url = "/attached/" +getPara("dir")+"/" + sdf.format(new Date()) + "/" + df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
    try {
       FileUtils.moveFile(getFile("imgFile").getFile().getAbsoluteFile(), new File(PathKit.getWebRootPath() + url));
       getData().put("error", Integer.valueOf(0));
       
       // put to cloud
       String prefix=getValuebyKey("bucket_type").toString();
       BucketVO bucket=new BucketVO(getStrValuebyKey(prefix+"_bucket"), getStrValuebyKey(prefix+"_access_key"), getStrValuebyKey(prefix+"_secret_key"), getStrValuebyKey(prefix+"_host"));
       FileManageAPI man=new QiniuBucketManageImpl(bucket);
       String nurl=man.create( new File(PathKit.getWebRootPath() + url),url).get("url").toString();
       getData().put("url",nurl);
    } catch (IOException e) {
       getData().put("error", "上传失败了");
       e.printStackTrace();
    }
     renderJson(getData());
  }
}
