package com.fzb.common.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 将信息发布为静态页面
 * @author xchun
 * 
 */
public class Jsp2Html {
	 
	/**
	 * @param sSourceUrl 原URL
	 * @param file 需要存储的路径
	 * @throws IOException
	 */
	public static void convert2Html(String sSourceUrl, File file) throws IOException {
		int HttpResult;
		URL url = new URL(sSourceUrl);
		
		URLConnection urlconn = url.openConnection();
		urlconn.connect();
		HttpURLConnection httpconn = (HttpURLConnection) urlconn;
		HttpResult = httpconn.getResponseCode();
		if (HttpResult != HttpURLConnection.HTTP_OK) {
			
		} else {
			InputStream in = urlconn.getInputStream(); 
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			FileOutputStream fout =new FileOutputStream(file);
			byte temp[]=new byte[1024];
			Integer length=0;
			while ((length = in.read(temp)) != -1) {
				fout.write(temp,0,length);
			}
			in.close();
			fout.close();
		}
	}

}