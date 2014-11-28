 package com.fzb.blog.model;
 
 import com.jfinal.plugin.activerecord.Model;
 import java.util.List;
 
 public class Link extends Model<Link>
 {
   public static final Link dao = new Link();
 
   public List<Link> queryAll()
   {
     return find("select linkName,linkId as id,sort,url from link order by sort");
   }
 }

