 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.LogNav;
 
 public class LogNavControl extends ManageControl
 {
   public void delete()
   {
     LogNav.dao.deleteById(getPara(0));
   }
 
   public void queryAll() {
     renderJson(LogNav.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
   }

	@Override
	public void add() {
		LogNav.dao.set("navName", getPara("navName")).set("url", getPara("url")).set("sort", getParaToInt("sort")).save();
	}
	
	@Override
	public void update() {
		LogNav.dao.set("navName", getPara("navName")).set("url", getPara("url")).set("sort", getParaToInt("sort")).update();
	}
   
 }

