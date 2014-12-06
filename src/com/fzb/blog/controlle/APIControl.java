package com.fzb.blog.controlle;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fzb.blog.model.Comment;
import com.fzb.common.util.HttpUtil;
import com.fzb.common.util.ParseTools;

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
		params.put("short_name", getValuebyKey("duoshuo_short_name"));
		params.put("secret", getValuebyKey("duoshuo_secret"));
		//params.put("short_name", "xchun.duoshuo.com");
		//params.put("short_name", "xchun.duoshuo.com");
		try {
			Map<String,Object> resp= new JSONDeserializer<Map<String,Object>>().deserialize(HttpUtil.getGResponseText(urlPath, params));
			if((Integer)resp.get("code")==0){
				List<Map<String,Object>> comments=(List<Map<String, Object>>) resp.get("response");
				for (Map<String, Object> map : comments) {
					if(map.get("action").equals("create")){
						System.out.println("dsaf");
						Map<String,Object> meta=(Map<String, Object>) map.get("meta");
						new Comment().set("userIp", meta.get("ip")).set("userMail", meta.get("author_email")).set("hide", false).set("commTime", new Date()).set("userComment", meta.get("message")).set("userName", meta.get("author_name")).set("logId", meta.get("thread_key")).set("userHome", meta.get("author_url")).set("td", ParseTools.getDataBySdf("yyyy-MM-dd HH:mm:ss", meta.get("created_at"))).save();
					}
				}
			}
			System.out.println(resp);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		// 使用签名
		String urlPath="http://api.duoshuo.com/log/list.json";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("short_name", "xchun");
		params.put("secret", "5b926153aa64bbfb5742792402616830");
		//params.put("short_name", "xchun.duoshuo.com");
		//params.put("short_name", "xchun.duoshuo.com");
		
		
		try {
			Map<String,Object> resp= new JSONDeserializer<Map<String,Object>>().deserialize(HttpUtil.getGResponseText(urlPath, params));
			
			if((Integer)resp.get("code")==0){
				List<Map<String,Object>> comments=(List<Map<String, Object>>) resp.get("response");
				
				for (Map<String, Object> map : comments) {
					if(map.get("action").equals("create")){
						new Comment().set("userName", map.get("author_name")).set("logId", map.get("thread_key")).set("userHome", map.get("author_url")).set("td", new Date(((Integer)map.get("date"))*1000));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

