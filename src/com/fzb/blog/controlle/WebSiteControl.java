package com.fzb.blog.controlle;

import java.util.Map;
import java.util.Map.Entry;

import com.fzb.blog.model.WebSite;

public class WebSiteControl extends ManageControl
{
	public void update(){
		Map<String,String[]> tparamMap=getParaMap();
		for (Entry<String, String[]>  param: tparamMap.entrySet()) {
			new WebSite().updateByKV(param.getKey(),param.getValue()[0]);
		}
		if(getPara("resultType")!=null && "html".equals(getPara("resultType"))){
			setAttr("message", "变更完成");
		}
		else{
			getData().put("success", true);
			renderJson(getData());
		}
		// 更新缓存数据
		BaseControl.refreshCache();
	}
	
	public void siteMapPlugin(){
		/*if(isNotNullOrNotEmptyStr(getPara("disable"))){
			List<IPlugin> plugins=((Plugins)JFinal.me().getServletContext().getAttribute("plugins")).getPluginList();
			for (IPlugin plugin : plugins) {
				if(plugin.getClass().getSimpleName().equals(SiteMapPlugin.class.getSimpleName())){
					plugin.stop();
					plugins.remove(plugin);
					break;
				}
			}
			getData().put("success", true);
			setAttr("message", "插件停用成功");
		}
		else{
			List<IPlugin> plugins=((Plugins)JFinal.me().getServletContext().getAttribute("plugins")).getPluginList();
			IPlugin plugin=new SiteMapPlugin();
			plugin.start();
			plugins.add(plugin);
			setAttr("message", "插件启用成功");
		}*/
		 
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

