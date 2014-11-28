 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Plugin;
 
 public class PluginControl extends ManageControl
 {
   public void delete()
   {
     Plugin.dao.deleteById(getPara(0));
   }
 
   public void unpdate()
   {
   }
 
   public void queryAll()
   {
   }
 }

