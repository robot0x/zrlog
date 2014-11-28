 package com.fzb.blog.controlle;
 
 import java.util.HashMap;
 import java.util.Map;
 
 public class ManageControl extends BaseControl
 {
   private Map<String, Object> data = new HashMap<String, Object>();
 
   public Map<String, Object> getData() {
     return this.data;
   }
 
   public void setData(Map<String, Object> data) {
     this.data = data;
   }
 }

