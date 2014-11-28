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
import java.sql.PreparedStatement;
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
		C3p0Plugin c3p0Plugin = new C3p0Plugin(dbConn.get("jdbcUrl"), dbConn.get("user"), dbConn.get("password"),dbConn.get("driverClass"));
		try {
			
			c3p0Plugin.start();
			c3p0Plugin.getDataSource().getConnection();
			c3p0Plugin.stop();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		catch (NullPointerException  e) {
			e.printStackTrace();
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
			System.out.println(tempSqlStr);
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
			ps.setString(1, blogMsg.get("userName"));
			ps.setString(2,Md5Util.MD5(blogMsg.get("password")));
			ps.setString(3,"example@94fzb.colm");
			ps.executeUpdate();
			String inserLogType="INSERT INTO `type`(`typeId`, `typeName`, `remark`, `alias`) VALUES (1,'记录','哈哈','study')";
			ps.execute(inserLogType);
			String inserLog="INSERT INTO `log`(`logId`,`typeId`,`userId`,`content`,`digest`) VALUES (1,1,1,'每天进步一点','每天进步一点')";
			ps.setDate(1, new Date(System.currentTimeMillis()));
			ps.execute(inserLog);
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
