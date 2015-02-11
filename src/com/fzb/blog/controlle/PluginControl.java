package com.fzb.blog.controlle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import com.fzb.blog.model.Plugin;
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
	
	public void install(){
		if(isNotNullOrNotEmptyStr(getPara("name"))){
			String pName=getPara("name");
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
						String webLibPath=PathKit.getWebRootPath()+"/WEB-INF/lib/";
						String classPath=PathKit.getWebRootPath()+"/WEB-INF/lib/classes";
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
						IOUtil.moveOrCopy(pluginPath+"/temp/src/", classPath, false);
						map=tmap;
						System.out.println(tmap);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					map=new JSONDeserializer<Map<String,Object>>().deserialize(pluginContent);
				}
				
				Object tPlugin;
				try {
					tPlugin = Class.forName(map.get("classLoader").toString()).newInstance();
					if(tPlugin instanceof IZrlogPlugin){
						//PluginsUtil.addPlugin(map.get("key").toString(), (IZrlogPlugin)tPlugin);
						((IZrlogPlugin)tPlugin).install(paramMap);
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
				//do nothing
			}
		}
	}
}
