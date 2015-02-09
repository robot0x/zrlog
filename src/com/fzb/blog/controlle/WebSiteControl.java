package com.fzb.blog.controlle;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fzb.blog.config.QuartzPlugin;
import com.fzb.blog.model.LogNav;
import com.fzb.blog.model.WebSite;
import com.fzb.common.util.SiteMapJob;
import com.jfinal.config.Plugins;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.IPlugin;

public class WebSiteControl extends ManageControl
{
	public void update(){
		Map<String,String[]> tparamMap=getParaMap();
		for (Entry<String, String[]>  param: tparamMap.entrySet()) {
			new WebSite().updateByKV(param.getKey(),param.getValue()[0]);
		}
		getData().put("success", true);
		renderJson(getData());
		// 更新缓存数据
		BaseControl.refreshCache();
	}
	
	public void siteMapPlugin(){
		if(getPara("disable")!=null && !"".equals(getPara("disable"))){
			List<IPlugin> plugins=((Plugins)JFinal.me().getServletContext().getAttribute("plugins")).getPluginList();
			for (IPlugin plugin : plugins) {
				if(plugin.getClass().getSimpleName().equals(QuartzPlugin.class.getSimpleName())){
					plugin.stop();
					plugins.remove(plugin);
					break;
				}
			}
			getData().put("success", true);
			renderJson(getData());
		}
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
}

