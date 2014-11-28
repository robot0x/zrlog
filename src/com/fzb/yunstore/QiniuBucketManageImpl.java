package com.fzb.yunstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.fzb.api.io.bucket.FileManageAPI;
import com.fzb.common.util.ParseTools;
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
	
	private String bucketName;
	public QiniuBucketManageImpl(String bucketName){
		this.bucketName=bucketName;
	}
	
	@Override
	public Map<String, Object> delFile(String file) {
        Mac mac = new Mac(BucketUtil.getAccessKeyByBN(bucketName, "qiniu"),BucketUtil.getSecretKeyByBN(bucketName, "qiniu"));
		RSClient client = new RSClient(mac);
		CallRet cr=client.delete(bucketName, file);
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
        PutExtra extra = new PutExtra();
        // 生成一个新的文件名称  。不是太方便
        String key = ParseTools.getRandomFileNameByOld(file).getName();
        try {
			PutRet ret = IoApi.putFile(getUptoken(), "jpeg/"+file.getName(), file, extra);
			responseData.put("statusCode", ret.getStatusCode());
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
		 
        Mac mac = new Mac(BucketUtil.getAccessKeyByBN(bucketName, "qiniu"),BucketUtil.getSecretKeyByBN(bucketName, "qiniu"));
        // 请确保该bucket已经存在
        PutPolicy putPolicy = new PutPolicy(bucketName);
		return putPolicy.token(mac);
	}
	
	public static void main(String[] args) {
		QiniuBucketManageImpl qin=new QiniuBucketManageImpl("fz-blog");
		System.out.println(qin.create(new File("C:\\Users\\xiaochun\\Pictures\\S41125-170028.jpg")));
		System.out.println(qin.delFile("QQ截图20141128105713.png"));
		System.out.println(qin.fopImageView(""));
		
		FileManageAPI bucket=new BaiduBucketManageImpl("a94fzb","94fzb");
		bucket.create(new File("C:\\Users\\xiaochun\\Pictures\\S41125-170028.jpg"));
	}

}
