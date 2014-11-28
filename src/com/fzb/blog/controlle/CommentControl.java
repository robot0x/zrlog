 package com.fzb.blog.controlle;
 
 import com.fzb.blog.model.Comment;
 import com.fzb.blog.model.Link;
 
 public class CommentControl extends ManageControl
 {
   public void delete()
   {
     Link.dao.deleteById(getPara(0));
   }
 
   public void oper() {
     if (getPara("oper").equals("del")) {
       String[] ids = ((String[])getParaMap().get("id"))[0].split(",");
       for (String id : ids) {
         Comment.dao.deleteById(id);
       }
     }
     renderJson("OK");
   }
 
   public void queryAll() {
     renderJson(Comment.dao.getCommentsByPage(getParaToInt("page").intValue(), getParaToInt("rows").intValue()));
   }
 }

