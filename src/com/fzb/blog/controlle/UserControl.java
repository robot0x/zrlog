package com.fzb.blog.controlle;

import com.fzb.blog.model.Comment;
import com.fzb.blog.model.Link;
import com.fzb.blog.model.User;
import com.fzb.common.util.Md5Util;

public class UserControl extends ManageControl {
	public void delete() {
		Link.dao.deleteById(getPara(0));
	}

	public void queryAll() {

	}

	public void index() {
		if (getSessionAttr("user") != null) {
			if (getPara(0) == null) {
				render("/admin/index.jsp");
			} else {
				render("/admin/" + getPara(0) + ".jsp");
			}
		} else {
			render("/admin/login.jsp");
		}

	}

	public void logout() {
		getSession().invalidate();
		render("/admin/login.jsp");
	}

	public void login() {
		if(getPara("userName")!=null && getPara("password")!=null){
			User user = User.dao.login(getPara("userName"),
					Md5Util.MD5(getPara("password")));
			if (user != null) {
				getSession().setAttribute("user", user);
				getSession().setAttribute("comments", Comment.dao.noRead(1, 5));
				if (getPara("redirectFrom") != null
						&& !"".equals(getPara("redirectFrom"))) {
					redirect(getPara("redirectFrom"));
					return;
				}
			} else {
				setAttr("errorMsg", "用户名或密码错误");
			}
		}
		index();
		
		
	}

	@Override
	public void add() {

	}

	@Override
	public void update() {
		
	}

	public void changePassword() {
		
		if(isNullOrEmptyStr(getPara("oldPassword"),getPara("newPassword"))){
			// compare oldPasswd
			String userName=((User)getSessionAttr("user")).getStr("userName");
			String dbPassword=User.dao.getPasswordByName(userName);
			String oldPassword=getPara("oldPassword");
			if(Md5Util.MD5(oldPassword).equals(dbPassword)){
				User.dao.updatePassword(userName, Md5Util.MD5(getPara("newPassword")));
				setAttr("message", CHANGEPWDSUCC);
			}
			else{
				setAttr("message", OLDPWDERROR);
			}
		}
		else{
			setAttr("message", ARGSCHECKFAIL);
		}
		
	}

}
