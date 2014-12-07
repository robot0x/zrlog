package com.fzb.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServlet;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

/**
 * 用于发送HTTP POST 请求
 * @author xiaochun
 *
 */
public class HttpUtil extends HttpServlet {

	private static Logger log=Logger.getLogger(HttpUtil.class);

	 
	
	public static void main(String[] args) throws IOException{
		int  i=0;
		while(i<1){
		new Thread(){
			@Override
			public void run() {
				String urlPath="http://localhost:8080/zrlog/api/duoshuo";
				 Map<String,Object> params=new HashMap<String,Object>();
				 params.put("action", "rose");
				 params.put("_method", "login");
				 params.put("password", "123456");
				 params.put("registrationID", "777");  
				 //params.put("short_name", "xchun.duoshuo.com");
				 try {
					System.out.println(getGResponseText(urlPath, params));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();;
		i+=1;
	}
	}
	/**
	 * 利用HTTPClient 发送Post请求
	 * @param urlPath 服务器中servlet 的urlPath
	 * @param params 需要提交的参数
	 * @return 
	 * @throws IOException
	 */
	public static String getResponseText(String urlPath,Map<String,Object> params) throws IOException{
		log.info(urlPath+ " http post params "+params);
		long start=System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = postForm(urlPath,params);
		CloseableHttpResponse response = httpclient.execute(httppost);
		BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String temp=null;
		StringBuffer sb=new StringBuffer();
		while((temp=reader.readLine())!=null){
			sb.append(temp);
		}
		reader.close();
		String html=new String(sb.toString().getBytes(),"utf-8");
		log.info("used Time " +html +" " +(System.currentTimeMillis()-start));
		return html;
	}
	
	public static String getGResponseText(String urlPath,Map<String,Object> params) throws IOException{
		if(params!=null && !params.isEmpty()){
			urlPath+="?";
			for (Entry<String, Object> param : params.entrySet()) {
				if(param.getValue() instanceof List){
					List<Object> values=(List<Object>) param.getValue();
					for (Object object : values) {
						urlPath+=param.getKey()+"="+object+"&";
					}
				}
				else{
					urlPath+=param.getKey()+"="+param.getValue()+"&";
				}
			}
			urlPath=urlPath.substring(0,urlPath.length()-1);
		}
		log.info(urlPath+ " http get params "+params);
		long start=System.currentTimeMillis();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		System.out.println(urlPath);
		HttpGet httpget = new HttpGet(urlPath);
		CloseableHttpResponse response = httpclient.execute(httpget);
		BufferedReader reader=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String temp=null;
		StringBuffer sb=new StringBuffer();
		while((temp=reader.readLine())!=null){
			sb.append(temp);
		}
		reader.close();
		String html=new String(sb.toString().getBytes(),"utf-8");
		log.info("used Time " +html +" " +(System.currentTimeMillis()-start));
		return html;
	}
	
	/**
	 * 组装POST 请求参数
	 * @param url
	 * @param params
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static HttpPost postForm(String urlPath, Map<String, Object> params){  
        
        HttpPost httpost = new HttpPost(urlPath);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
        if(params==null){
        	return httpost;
        }  
        Set<String> keySet = params.keySet(); 
        for(String key : keySet) {  
        	if(params.get(key) instanceof List){
        		@SuppressWarnings("unchecked")
				List<String> list=(List<String>) params.get(key);
        		for (String string : list) {
        			nvps.add(new BasicNameValuePair(key,string));  
				}
        	}
        	else{
        		nvps.add(new BasicNameValuePair(key, (String) params.get(key)));  
        	}
        }  
          
        try {  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        } 
        return httpost;
    }
}
