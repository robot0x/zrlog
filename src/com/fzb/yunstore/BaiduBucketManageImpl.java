package com.fzb.yunstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import com.baidu.inf.iis.bcs.BaiduBCS;
import com.baidu.inf.iis.bcs.auth.BCSCredentials;
import com.baidu.inf.iis.bcs.auth.BCSSignCondition;
import com.baidu.inf.iis.bcs.http.HttpMethodName;
import com.baidu.inf.iis.bcs.model.ObjectMetadata;
import com.baidu.inf.iis.bcs.request.GenerateUrlRequest;
import com.baidu.inf.iis.bcs.request.PutObjectRequest;
import com.fzb.io.api.FileManageAPI;

public class BaiduBucketManageImpl implements FileManageAPI {

	private static final Logger log = Logger.getLogger(BaiduBucketManageImpl.class);
	
	private BucketVO bucket;
	public BaiduBucketManageImpl(BucketVO bucket){
		this.bucket=bucket;
	}
	
	@Override
	public Map<String, Object> delFile(String file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> delFolder(String folder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> create(File file) {
		try{
			BCSCredentials credentials = new BCSCredentials(bucket.getAccessKey(), bucket.getSecretKey());
			BaiduBCS baiduBCS = new BaiduBCS(credentials, bucket.getHost());
			baiduBCS.setDefaultEncoding("UTF-8"); 
			InputStream fileContent = new FileInputStream(file);
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.length());
			try {
				objectMetadata.setContentType(Magic.getMagicMatch(file, false).getMimeType());
				System.out.println(Magic.getMagicMatch(file, false).getMimeType());
			} catch (MagicParseException e) {
				e.printStackTrace();
			} catch (MagicMatchNotFoundException e) {
				e.printStackTrace();
			} catch (MagicException e) {
				e.printStackTrace();
			}
			PutObjectRequest request = new PutObjectRequest(bucket.getBucketName(), "/abl/temp.jpg", fileContent, objectMetadata);
			ObjectMetadata result = baiduBCS.putObject(request).getResult();
			GenerateUrlRequest generateUrlRequest = new GenerateUrlRequest(HttpMethodName.GET, bucket.getBucketName(),"/abl/temp.jpg");
			generateUrlRequest.setBcsSignCondition(new BCSSignCondition());
			log.info(result);
			System.out.println(baiduBCS.generateUrl(generateUrlRequest));
			return null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	}

	@Override
	public Map<String, Object> moveOrCopy(String folder, String tag,
			boolean isMove) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> moveOrCopyFile(String src, String tag,
			boolean isMove) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> CopyFileByInStream(InputStream in, String tag) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> modifyFile(String root, String code,
			String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getFileList(String folder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> create(File file, String key) {
		// TODO Auto-generated method stub
		return null;
	}

}
