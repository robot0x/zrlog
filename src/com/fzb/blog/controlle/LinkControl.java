 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Link;
 
 public class LinkControl extends ManageControl
 {
   public void delete()
   {
     Link.dao.deleteById(getPara(0));
   }
 
   public void unpdate() {
   }
 
   public void index() {
     render("/admin/link.jsp");
   }
 
   public void queryAll() {
     renderJson(Link.dao.queryAll());
   }
 }

