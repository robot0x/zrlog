 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Type;
 
 public class TypeControl extends ManageControl
 {
   public void delete()
   {
     Type.dao.deleteById(getPara(0));
   }
 
   public void queryAll()
   {
     renderJson(Type.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
   }

	@Override
	public void add() {
		Type.dao.set("typeName", getPara("typeName")).set("alias", getPara("alias")).set("remark", getPara("remark")).save();
	}
	
	@Override
	public void update() {
		Type.dao.set("typeName", getPara("typeName")).set("alias", getPara("alias")).set("remark", getPara("remark")).update();
	}
}

