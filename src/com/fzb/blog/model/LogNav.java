 package com.fzb.blog.model;
 
 import com.jfinal.plugin.activerecord.Model;
 import java.util.List;
 
 public class LogNav extends Model<LogNav>
 {
   public static final LogNav dao = new LogNav();
 
   public List<LogNav> queryAll() {
     String sql = "select l.navId as id,l.navName,l.url,l.sort from  logNav l order by sort";
     return find(sql);
   }
 }

