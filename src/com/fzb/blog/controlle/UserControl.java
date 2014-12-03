 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Link;
import com.fzb.blog.model.Tag;
import com.fzb.blog.model.User;
import com.fzb.common.util.Md5Util;
 
 public class UserControl extends ManageControl
 {
   public void delete()
   {
     Link.dao.deleteById(getPara(0));
   }
 
   public void unpdate()
   {
   }
 
   public void queryAll()
   {
   }
 
   public void index() {
     if (getSessionAttr("user") != null) {
       if (getPara(0) == null) {
         redirect("/admin/index");
       }
       else {
         render("/admin/" + getPara(0) + ".jsp");
       }
     }
     else
     {
       render("/admin/login.jsp");
     }
     
     
   }
 
   public void logout() {
     getSession().invalidate();
     render("/admin/login.jsp");
   }
 
   public void login() {
     User user = User.dao.login(getPara("userName"), Md5Util.MD5(getPara("password")));
     if (user != null) {
    	 System.out.println(user);
       getSession().setAttribute("user", user);
     }
     index();
     
     Tag.dao.refreshTag();
   }
 }

