 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Comment;
 import com.fzb.blog.model.Link;
 
 public class CommentControl extends ManageControl
 {
   public void delete()
   {
	 String[] ids=getPara("id").split(",");
	 for (String id : ids) {
		Comment.dao.deleteById(id);
	 }
   }
   
 
   public void queryAll() {
     renderJson(Comment.dao.getCommentsByPage(getParaToInt("page").intValue(), getParaToInt("rows").intValue()));
   }

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
 
 }

