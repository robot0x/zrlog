package com.fzb.blog.controlle;

import java.util.HashMap;
import java.util.Map;

import com.fzb.common.util.InstallUtil;
import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PathKit;

public class InstallControl extends Controller{

	public void testDbConn(){
		Map<String,String> dbConn=new HashMap<String,String>();
	    dbConn.put("jdbcUrl", "jdbc:mysql://"+getPara("dbhost")+":"+getPara("port")+"/"+getPara("dbname")+"?&characterEncoding=UTF-8");
	    dbConn.put("user", getPara("dbuser"));
	    dbConn.put("password", getPara("dbpwd"));
	    dbConn.put("deviceClass", "com.mysql.jdbc.Driver");
		setSessionAttr("dbConn", dbConn);
	    if(new InstallUtil(PathKit.getWebRootPath()+"/WEB-INF",dbConn).testDbConn()){
			 render("/install/message.jsp");
		}
	}
	public void installJblog(){
		
		Map<String,String> dbConn=getSessionAttr("dbConn");
		Map<String,String> configMsg=new HashMap<String,String>();
	    configMsg.put("webTitle", getPara("webTitle"));
	    configMsg.put("username", getPara("username"));
	    configMsg.put("password", "password");
	    if(new InstallUtil(PathKit.getWebRootPath()+"/WEB-INF",dbConn,configMsg).installJblog()){
			 render("/install/success.jsp");
		}
	    
		
	}
	
	public void index(){
			render("/install/index.jsp");
	} 
}
