package com.fzb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;

import com.fzb.blog.model.Log;
import com.fzb.blog.model.Plugin;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class InstallUtil {

	private String basePath;
	private Map<String,String> dbConn;
	private Map<String,String> configMsg;
	private Connection connect;
	
	public InstallUtil(String basePath){
		this.basePath=basePath;
	}
	public InstallUtil(String basePath,Map<String,String> dbConn,Map<String,String> configMsg){
		this.basePath=basePath;
		this.dbConn=dbConn;
		this.configMsg=configMsg;
		C3p0Plugin c3p0Plugin = new C3p0Plugin(dbConn.get("jdbcUrl"), dbConn.get("user"), dbConn.get("password"),dbConn.get("driverClass"));
		try {
			c3p0Plugin.start();
			this.connect=c3p0Plugin.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public  InstallUtil(String basePath,Map<String,String> dbConn){
		this.basePath=basePath;
		this.dbConn=dbConn;
	}
	
	public boolean testDbConn(){
		try {
			Class.forName(dbConn.get("deviceClass"));
			connect=DriverManager.getConnection(dbConn.get("jdbcUrl"), dbConn.get("user"), dbConn.get("password"));
			connect.close();
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Boolean installJblog()
	{
		File lock=new File(basePath+"/install.lock");
		if(lock.exists())
		{
			return false;
		}
		else
		{
			return startInstall(dbConn,configMsg,lock);
		}
		
	}
	public boolean checkInstall(){
		File lock=new File(basePath+"/install.lock");
		return lock.exists();
	}

	public boolean startInstall(Map<String,String> dbConn,Map<String, String> blogMsg,File lock)
	{
		File file=new File(basePath+"/db.properties");
		Statement st=null;
		int cnt=0;
		
		if(file.exists())
		{
			file.delete();
		}
		try {
			lock.createNewFile();
			file.createNewFile();
			OutputStream out=new FileOutputStream(file);
			Properties prop=new Properties();
			prop.putAll(dbConn);
			prop.store(out, "This is a database configuration file");
			out.close();
			File sqlFile=new File(basePath+"/cms.sql");
			InputStream in=new FileInputStream(sqlFile);
			byte[] b=new byte[1024];
			StringBuffer s=new StringBuffer();
			while((in.read(b))!=-1)
			{
				s.append(new String(b));
				b=new byte[1024];
			}
			String[] sql=s.toString().trim().split("\n");
			 String tempSqlStr="";
			for(String sqlSt:sql){
				if(sqlSt.startsWith("#")){
					continue;
				}
				if(sqlSt.startsWith("/*")){
					continue;
				}
				System.out.println(sqlSt);
				tempSqlStr+=sqlSt;
			}
			sql=tempSqlStr.split(";");
			for(String sqlSt:sql)
			{
				if(sqlSt.startsWith("#")){
					continue;
				}
				if(!"".equals(sqlSt))
				{
					st=connect.createStatement();
					st.execute(sqlSt);
					cnt++;
				}
				
			}
			String insertWebSql="INSERT INTO `website` (`siteId`, `name`,`status`, `value`, `remark`) VALUES (1, 'rows',?, '10', NULL),(2, 'title',?, '"+configMsg.get("webTitle")+"', NULL),(8, 'template',?, '/include/templates/default', NULL)";
			
			PreparedStatement ps=connect.prepareStatement(insertWebSql);
			ps.setBoolean(1, true);
			ps.setBoolean(2, true);
			ps.setBoolean(3, true);
			ps.executeUpdate();
			String insertUserSql="INSERT INTO `user`( `userId`,`userName`, `password`, `email`) VALUES (1,?,?,?)";
			ps=connect.prepareStatement(insertUserSql);
			ps.setString(1, blogMsg.get("username"));
			ps.setString(2,Md5Util.MD5(blogMsg.get("password")));
			ps.setString(3,configMsg.get("email"));
			ps.executeUpdate();
			
			String insertLogNavSql="INSERT INTO `lognav`( `navId`,`url`, `navName`, `sort`) VALUES (?,?,?,?)";
			ps=connect.prepareStatement(insertLogNavSql);
			ps.setObject(1, 1);
			ps.setObject(2, "/");
			ps.setObject(3,"主页");
			ps.setObject(4, 1);
			ps.executeUpdate();
			
			insertLogNavSql="INSERT INTO `lognav`( `navId`,`url`, `navName`, `sort`) VALUES (?,?,?,?)";
			ps=connect.prepareStatement(insertLogNavSql);
			ps.setObject(1, 2);
			ps.setObject(2,"/admin/login");
			ps.setObject(3,"管理");
			ps.setObject(4, 2);
			ps.executeUpdate();
			
			String insertpluginSql="INSERT INTO `plugin` VALUES (1,'系统提供的插件',b'1','分类菜单',NULL,'types',3),(2,NULL,b'1','标签云',NULL,'tags',3),(3,NULL,b'1','友链',NULL,'links',2),(4,NULL,b'1','存档',NULL,'archives',3)";
			ps=connect.prepareStatement(insertpluginSql);
			ps.executeUpdate();
			
			
			String inserLogType="INSERT INTO `type`(`typeId`, `typeName`, `remark`, `alias`) VALUES (1,'记录','哈哈','study')";
			ps.execute(inserLogType);
			String inserLog="INSERT INTO `log`(`logId`,`canComment`,`keywords`,`alias`,`typeId`,`userId`,`title`,`content`,`digest`,`releaseTime`) VALUES (1,?,'记录','first',1,1,'记录学习记录','每天进步一点','每天进步一点',?)";
			ps=connect.prepareStatement(inserLog);
			ps.setBoolean(1, true);
			ps.setObject(2, new java.util.Date());
			ps.executeUpdate();
			
			String inserTag="INSERT INTO `tag`(`tagId`,`text`,`count`) VALUES (1,'记录',1)";
			ps=connect.prepareStatement(inserTag);
			ps.executeUpdate();
			return true;
		} catch (FileNotFoundException e) {
			lock.delete();
			e.printStackTrace();
		} catch (IOException e) {
			lock.delete();
			e.printStackTrace();
		} catch (SQLException e) {
			lock.delete();
			e.printStackTrace();
		}
		finally
		{
			try {
				connect.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
}
