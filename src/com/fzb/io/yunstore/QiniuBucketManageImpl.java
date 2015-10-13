package com.fzb.io.yunstore;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.fzb.io.api.FileManageAPI;
import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.fop.ImageInfo;
import com.qiniu.api.fop.ImageInfoRet;
import com.qiniu.api.fop.ImageView;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.net.CallRet;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;

public class QiniuBucketManageImpl implements FileManageAPI {

	
	private Map<String,Object> responseData=new HashMap<String,Object>();
	
	private BucketVO bucket;
	
	public QiniuBucketManageImpl(BucketVO bucket){
		this.bucket=bucket;
	}
	
	@Override
	public Map<String, Object> delFile(String file) {
        Mac mac = new Mac(bucket.getAccessKey(),bucket.getSecretKey());
		RSClient client = new RSClient(mac);
		CallRet cr=client.delete(bucket.getBucketName(), file);
		responseData.put("statusCode", cr.statusCode);
		responseData.put("resp", cr.getResponse());
		return responseData;
	}

	@Override
	@Deprecated
	public Map<String, Object> delFolder(String folder) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<String,Object> fopImageView(String key){
		String url = "http://fz-blog.qiniudn.com/S41125-170028.jpg";
		
        ImageView iv = new ImageView();
        /*iv.mode = 2 ;
        iv.width = 100 ;
        iv.height = 200 ;
        iv.quality = 10 ;
        iv.format = "jpg" ;*/
       // CallRet ret = iv.call(url);
        ImageInfoRet ii = ImageInfo.call(url);
       
        System.out.println(ii.width);
        File f=new File("e:/1.png");
       /* try {
			FileOutputStream out=new FileOutputStream(f);
			out.write(ret.getResponse().getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        return responseData;
	} 

	@Override
	public Map<String, Object> create(File file) {
		 // 生成一个新的文件名称  。不是太方便
        //String key = ParseTools.getRandomFileNameByOld(file).getName();
		return create(file, null);
	}
	@Override
	public Map<String, Object> create(File file, String key) {
		  PutExtra extra = new PutExtra();
	        try {
				PutRet ret = IoApi.putFile(getUptoken(), key.substring(1), file, extra);
				responseData.put("statusCode", ret.getStatusCode());
				String url="http://"+bucket.getHost()+key;
				 ImageInfoRet infoRet = ImageInfo.call(url);
				 if(infoRet.width>600){
					 url+="?imageView2/2/w/600";
				 }
				responseData.put("url", url);
				
				
				return responseData;
			} catch (AuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return null;
	}

	@Override
	public Map<String, Object> moveOrCopy(String filer, String tag,
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
	
	public String getUptoken() throws AuthException, JSONException{
		 /*
        Mac mac = new Mac(BucketUtil.getAccessKeyByBN(bucketName, "qiniu"),BucketUtil.getSecretKeyByBN(bucketName, "qiniu"));
        // 请确保该bucket已经存在
        PutPolicy putPolicy = new PutPolicy(bucketName);*/
		
		Mac mac = new Mac(bucket.getAccessKey(),bucket.getSecretKey());
	    // 请确保该bucket已经存在
	    PutPolicy putPolicy = new PutPolicy(bucket.getBucketName());
		return putPolicy.token(mac);
	}
	
	public static void main(String[] args) {
		
 	}

	

}
