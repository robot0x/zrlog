 package com.fzb.blog.model;
 
 import com.jfinal.plugin.activerecord.Model;
 import java.util.HashSet;
 import java.util.List;
 import java.util.Set;
 
 public class Tag extends Model<Tag>
 {
   public static final Tag dao = new Tag();
 
   public List<Tag> queryAll() {
     return find("select tagid as id,text,count from tag");
   }
 
   public boolean update(String nowTagStr, String oldTagStr)
   {
     if (nowTagStr.equals(oldTagStr)) {
       return true;
     }
 
     String[] old = oldTagStr.split(",");
     String[] nstr = nowTagStr.split(",");
     Set addSet = new HashSet();
     Set deleteSet = new HashSet();
     for (String oset : nstr) {
       addSet.add(oset);
     }
     for (String oset : old) {
       if (!addSet.contains(oset)) {
         deleteSet.add(oset);
       }
       else {
         addSet.remove(oset);
       }
     }
 
     insertTag(addSet.toString());
     deleteTag(deleteSet.toString());
 
     return true;
   }
 
   public boolean insertTag(String now)
   {
     for (String add : now.split(",")) {
       Tag t = (Tag)dao.findFirst("select `count` from tag where text=?", new Object[] { add });
       if (t == null) {
         ((Tag)((Tag)dao.set("text", add)).set("count", Integer.valueOf(1))).save();
       }
       else {
         ((Tag)((Tag)dao.set("text", add)).set("count", Integer.valueOf(t.getInt("count").intValue() + 1))).update();
       }
     }
     return true;
   }
 
   public boolean deleteTag(String old)
   {
     for (String del : old.split(",")) {
       Tag t = (Tag)dao.findFirst("select `count` from tag where text=?", new Object[] { del });
       if (t != null) {
         if (t.getInt("count").intValue() > 1) {
           ((Tag)((Tag)dao.set("text", del)).set("count", Integer.valueOf(t.getInt("count").intValue() - 1))).update();
         }
         else {
           ((Tag)dao.set("text", del)).delete();
         }
       }
     }
     return true;
   }
   public static void main(String[] args) {
     new Tag().update("java", "C#");
   }
 }

