 package com.fzb.blog.model;
 
 import com.jfinal.plugin.activerecord.Model;
 import java.util.List;
 
 public class Type extends Model<Type>
 {
   public static final Type dao = new Type();
 
   public List<Type> queryAll() {
     return dao.find("select t.typeId as id,t.alias,t.typeName,t.remark,(select count(logId) from log where typeid=t.typeid) as typeamount from type t");
   }
 }

