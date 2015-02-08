package com.fzb.blog.controlle;

import java.util.HashMap;
import java.util.Map;

public abstract class ManageControl extends BaseControl {
	private Map<String, Object> data = new HashMap<String, Object>();

	public Map<String, Object> getData() {
		return this.data;
	}

	public void oper() {
		if (getPara("oper") != null) {
			if ("del".equals(getPara("oper"))) {
				this.delete();
			} else if ("update".equals(getPara("oper"))
					|| "edit".equals(getPara("oper"))) {
				this.update();
			} else if ("add".equals(getPara("oper"))) {
				this.add();
			} else {
				System.out.println("unSupport ");
			}

		}
		renderJson("OK");
		
		//清空数据缓存
		BaseControl.refreshCache();
	}

	/*
	 * public void index() { String
	 * cont=getRequest().getRequestURI().substring(getRequest
	 * ().getRequestURI().indexOf("/")); render("/admin/"+cont+".jsp"); }
	 */
	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public abstract void add();

	public abstract void update();

	public abstract void delete();

	public abstract void queryAll();

}
