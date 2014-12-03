 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Type;
 
 public class TypeControl extends ManageControl
 {
   public void delete()
   {
     Type.dao.deleteById(getPara(0));
   }
 
   public void unpdate()
   {
   }
 
   public void queryAll()
   {
     renderJson(Type.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
   }
 }

