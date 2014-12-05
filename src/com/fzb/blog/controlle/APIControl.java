package com.fzb.blog.controlle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fzb.common.util.HttpUtil;

import flexjson.JSONDeserializer;

/**
 * @author zhengchangchun
 * 对QueryLogControl 的扩展 响应的数据均为Json格式
 */
public class APIControl extends QueryLogControl
{
	
	/**
	 * 多说反向同步接口
	 */
	public void duoshuo(){
		String action=getPara("action");
		String signature=getPara("signature ");
		
		// 使用签名
		String urlPath="http://api.duoshuo.com/log/list.json";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("short_name", "xchun");
		params.put("secret", "5b926153aa64bbfb5742792402616830");
		//params.put("short_name", "xchun.duoshuo.com");
		//params.put("short_name", "xchun.duoshuo.com");
		try {
			Map<String,Object> resp= new JSONDeserializer<Map<String,Object>>().deserialize(HttpUtil.getGResponseText(urlPath, params));
			System.out.println(resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}

