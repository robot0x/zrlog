package com.fzb.blog.model;

import java.io.Serializable;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class User extends Model<User> implements Serializable {

	public static final User dao = new User();

	public User login(String userName, String password) {

		return (User) findFirst(
				"select * from user where username=? and password=?",
				new Object[] { userName, password });
	}
	public String getPasswordByName(String userName){
		return (String) findFirst("select password from user where username=?",userName).get("password");
	}
	
	public boolean updatePassword(String userName,String password){
		return Db.update("update user set password=? where userName=?",password,userName)>0;
	}

}
