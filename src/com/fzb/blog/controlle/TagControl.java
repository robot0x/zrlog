 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Link;
import com.fzb.blog.model.Tag;
 
 public class TagControl extends ManageControl
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
	   Tag.dao.refreshTag();
	   renderJson(Tag.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
   }
 }

