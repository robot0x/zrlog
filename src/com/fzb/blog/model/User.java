 package com.fzb.blog.model;
 
 import java.io.Serializable;

import com.jfinal.plugin.activerecord.Model;
 
 public class User extends Model<User> implements Serializable
 {
   private int userId;
   private String email;
   private String userName;
   private String password;
   private String header;
   public static final User dao = new User();
 
   public int getUserId() {
     return this.userId;
   }
 
   public void setUserId(int userId) {
     this.userId = userId;
   }
 
   public String getEmail() {
     return this.email;
   }
 
   public void setEmail(String email) {
     this.email = email;
   }
 
   public String getUserName() {
     return this.userName;
   }
 
   public void setUserName(String userName) {
     this.userName = userName;
   }
 
   public String getPassword() {
     return this.password;
   }
 
   public void setPassword(String password) {
     this.password = password;
   }
 
   public User login(String userName, String password) {
	   
     return (User)findFirst("select * from user where username=? and password=?", new Object[] { userName, password });
   }

public String getHeader() {
	return header;
}

public void setHeader(String header) {
	this.header = header;
}
 }

