 package com.fzb.blog.model;
 
 import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
 import java.util.HashMap;
 import java.util.List;
import java.util.Map;
 
 public class WebSite extends Model<WebSite>
 {
   public static final WebSite dao = new WebSite();
 
   public Map<String, Object> getWebSite() {
     Map webSites = new HashMap();
     List<WebSite> lw = find("select * from website where status=1");
     for (WebSite webSite : lw) {
       webSites.put(webSite.getStr("name"), webSite.get("value"));
       webSites.put(webSite.getStr("name") + "Status", webSite.get("value"));
       webSites.put(webSite.getStr("name") + "Remark", webSite.get("remark"));
     }
     return webSites;
   }
   public boolean updateByKV(String name,String value){
	   Db.update("update website set value=? where name=?",value,name);
	   return true;
   }
 }

