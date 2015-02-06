 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Plugin;
 
 public class PluginControl extends ManageControl
 {
   public void delete()
   {
     Plugin.dao.deleteById(getPara(0));
   }
 
   public void queryAll()
   {
	   
   }

	@Override
	public void add() {
		//Plugin.dao.set("typeName", getPara("typeName")).set("alias", getPara("alias")).set("remark", getPara("remark"))
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}

