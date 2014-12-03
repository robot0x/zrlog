 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.LogNav;
 
 public class LogNavControl extends ManageControl
 {
   public void delete()
   {
     LogNav.dao.deleteById(getPara(0));
   }
 
   public void unpdate()
   {
   }
 
   public void queryAll() {
     renderJson(LogNav.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
   }
   
   
 }

