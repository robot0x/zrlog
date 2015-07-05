package com.fzb.blog.controlle;

import java.util.Map;
import java.util.Map.Entry;

import com.fzb.blog.model.WebSite;

public class WebSiteControl extends ManageControl {
	public void update() {
		Map<String, String[]> tparamMap = getParaMap();
		for (Entry<String, String[]> param : tparamMap.entrySet()) {
			new WebSite().updateByKV(param.getKey(), param.getValue()[0],
					!"-1".equals(param.getValue()[0]));
		}
		if (getPara("resultType") != null
				&& "html".equals(getPara("resultType"))) {
			setAttr("message", "变更完成");
		} else {
			getData().put("success", true);
			renderJson(getData());
		}
		// 更新缓存数据
		BaseControl.refreshCache();
	}

	@Override
	public void add() {

	}

	@Override
	public void queryAll() {

	}

	@Override
	public void delete() {

	}
}
