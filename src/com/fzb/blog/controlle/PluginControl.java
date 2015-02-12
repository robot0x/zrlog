package com.fzb.blog.controlle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fzb.blog.model.Plugin;
import com.fzb.blog.util.LoadJar;
import com.fzb.blog.util.plugin.PluginsUtil;
import com.fzb.blog.util.plugin.api.IZrlogPlugin;
import com.fzb.common.util.IOUtil;
import com.fzb.common.util.ZipUtil;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;

import flexjson.JSONDeserializer;

public class PluginControl extends ManageControl {
	public void delete() {
		Plugin.dao.deleteById(getPara(0));
	}

	public void queryAll() {

	}

	@Override
	public void add() {
		// Plugin.dao.set("typeName", getPara("typeName")).set("alias",
		// getPara("alias")).set("remark", getPara("remark"))
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
	
	public void start(){
		if(isNotNullOrNotEmptyStr(getPara("name"))){
			String pName=getPara("name");
			IZrlogPlugin zPlugin=PluginsUtil.getPlugin(pName);
			if(zPlugin!=null){
				zPlugin.stop();
				PluginsUtil.addPlugin(pName, zPlugin);
			}
			else{
				setAttr("message", "不存在插件");
			}
		}
	}
	
	public void stop(){
		if(isNotNullOrNotEmptyStr(getPara("name"))){
			String pName=getPara("name");
			IZrlogPlugin zPlugin=PluginsUtil.getPlugin(pName);
			if(zPlugin!=null){
				PluginsUtil.romvePlugin(pName);
			}
			else{
				setAttr("message", "不存在插件");
			}
		}
	}
	
	public void unstall(){
		if(isNotNullOrNotEmptyStr(getPara("name"))){
			String pName=getPara("name");
			IZrlogPlugin zPlugin=PluginsUtil.getPlugin(pName);
			zPlugin.stop();
			PluginsUtil.romvePlugin(pName);
		}
	}
	
	public void install(){
		if(isNotNullOrNotEmptyStr(getPara("name"))){
			final String pName=getPara("name");
			IZrlogPlugin zPlugin=PluginsUtil.getPlugin(pName);
			if(zPlugin==null){
				//TODO 
				Map<String,Object> paramMap=new HashMap<String, Object>();
				Map<String,String[]> tparamMap=getParaMap();
				for (Entry<String, String[]>  param: tparamMap.entrySet()) {
					paramMap.put(param.getKey(), param.getValue()[0]);
				}
				paramMap.remove("name");
				String pluginContent=Db.queryFirst("select content from plugin where pluginName=?",pName);
				Map<String,Object> map=null;
				if(pluginContent==null){
					//TODO 解压 pluginName.zip
					try {
						String pluginPath=PathKit.getWebRootPath()+"/admin/plugin/"+pName+"";
						String webLibPath=PathKit.getWebRootPath()+"/WEB-INF/";
						String classPath=PathKit.getWebRootPath()+"/WEB-INF/";
						new File(pluginPath+"/temp/").mkdirs();
						ZipUtil.unZip(pluginPath+".zip", pluginPath+"/temp/");
						
						String installStr=IOUtil.getStringInputStream(new FileInputStream(pluginPath+"/temp/installGuide.txt"));
						//System.out.println(installStr);
						String installArgs[]=installStr.split("\r\n");
						Map<String,Object> tmap=new HashMap<String, Object>();
						for(String arg:installArgs){
							tmap.put(arg.split(":")[0], arg.substring(arg.split(":")[0].length()+1));
						}
						//copy File
						/*String htmlFiles[]=tmap.get("html").toString().split(",");
						for (String string : htmlFiles) {
							IOUtil.moveOrCopyFile(pluginPath+"/temp/html/"+string, pluginPath+string, false);
							System.out.println(pluginPath+"/temp/html/"+string);
						}
						String libFiles[]=tmap.get("jarFile").toString().split(",");
						for (String string : htmlFiles) {
							IOUtil.moveOrCopyFile(pluginPath+"/temp/lib/"+string, webLibPath+string, false);
							System.out.println(pluginPath+"/temp/lib/"+string);
						}*/
						IOUtil.moveOrCopy(pluginPath+"/temp/html/", pluginPath, false);
						IOUtil.moveOrCopy(pluginPath+"/temp/lib/", webLibPath, false);
						IOUtil.moveOrCopy(pluginPath+"/temp/classes/", classPath, false);
						File[] jarFiles=new File(pluginPath+"/temp/lib/").listFiles();
						//List<File> tfiles=new ArrayList<File>();
						//FIXME 动态加载Jar
						List<String> names=new ArrayList<String>();
						for (File file : jarFiles) {
							//file.toURI();
							File tf=new File(webLibPath+file.toString().substring((pluginPath+"/temp/").length()));
							//new ClassFileLoader().regClass(file.toString().substring((pluginPath+"/temp/bin").length()+1,file.toString().lastIndexOf(".")).replace("\\", "."));
							//System.out.println(webLibPath+file.toString().substring((pluginPath+"/temp/").length()));
							System.out.println(tf.toURL());
							new LoadJar(new URL[]{}).addJar(tf.toURL());
						}
						
						//IOUtil.getAllFiles(classPath, files);
						map=tmap;
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					map=new JSONDeserializer<Map<String,Object>>().deserialize(pluginContent);
				}
				
				final Object tPlugin;
				try {
					System.out.println(map.get("classLoader").toString());
					Thread.currentThread().getContextClassLoader().loadClass(map.get("classLoader").toString());
					tPlugin = Class.forName(map.get("classLoader").toString()).newInstance();
					if(tPlugin instanceof IZrlogPlugin){
						//PluginsUtil.addPlugin(map.get("key").toString(), (IZrlogPlugin)tPlugin);
						((IZrlogPlugin)tPlugin).install(paramMap);
						new Thread(){
							public void run() {
								PluginsUtil.addPlugin(pName, ((IZrlogPlugin)tPlugin));
							};
						}.start();
					}
					setAttr("message", "安装成功");
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			else{
				setAttr("message", "插件已经在运行了");
			}
		}
	}
}
