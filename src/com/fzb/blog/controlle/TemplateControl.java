package com.fzb.blog.controlle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.fzb.blog.model.Link;
import com.fzb.common.util.HttpUtil;
import com.fzb.common.util.ResponseData;
import com.fzb.common.util.ZipUtil;
import com.jfinal.kit.PathKit;

public class TemplateControl extends ManageControl {
	public void delete() {
		Link.dao.deleteById(getPara(0));
	}

	public void apply() {

	}
	
	public void index(){
		queryAll();
	}

	public void queryAll() {
		String webPath = PathKit.getWebRootPath();
		File[] templatesFile = new File(webPath + "/include/templates/")
				.listFiles();
		List<Map<String, Object>> templates = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < templatesFile.length; i++) {

			if (templatesFile[i].isFile())
				continue;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("template",
					templatesFile[i].toString().substring(webPath.length())
							.replace("\\", "/"));
			map.put("author", "xiaochun");
			map.put("name", "模板");
			map.put("digest", "这个是模板雅");
			map.put("version", "1.0");
			templates.add(map);
		}
		setAttr("templates", templates);
		render("/admin/template.jsp");
		// renderJson(Tag.dao.queryAll(getParaToInt("page"),getParaToInt("rows")));
	}

	@Override
	public void add() {

	}

	@Override
	public void update() {

	}

	public void download() {
		ResponseData<File> data = new ResponseData<File>() {
		};
		try {
			HttpUtil.getResponse(getPara("host") + "/template/download?id="
					+ getParaToInt("id"), data, PathKit.getWebRootPath()
					+ "/include/templates/");
			String folerName=data.getT().getName().toString().substring(0,data.getT().getName().toString().indexOf("."));
			ZipUtil.unZip(data.getT().toString(), PathKit.getWebRootPath()+ "/include/templates/"+folerName+"/");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderHtml("<div class='page-content'><div class='alert alert-block alert-success'><p>下载模板成功</p><p><a href='javascript:history.go(-1);'><button class='btn btn-sm btn-success'>返回</button></a></p></div></div>");

	}

}
