package com.fzb.blog.controlle;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.fzb.blog.model.Comment;
import com.fzb.common.util.HexaConversionUtil;
import com.fzb.common.util.HttpUtil;
import com.fzb.common.util.ParseTools;
import com.jfinal.plugin.activerecord.Db;

import flexjson.JSONDeserializer;

/**
 * @author zhengchangchun 对QueryLogControl 的扩展 响应的数据均为Json格式
 */
public class APIControl extends QueryLogControl {

	/**
	 * 多说反向同步接口
	 */
	public void duoshuo() {

		Map<String, Object> param = getdouShuoRequest();
		String action = (String) param.get("action");
		String signature = (String) param.get("signature");
		param.remove("signature");
		try {
			// check signature
			if(signature.equals(HmacSHA1Encrypt(mapToQueryStr(param), getStrValuebyKey("duoshuo_secret")))){
				// 使用签名
				String urlPath = "http://api.duoshuo.com/log/list.json";
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("short_name", getValuebyKey("duoshuo_short_name"));
				params.put("secret", getValuebyKey("duoshuo_secret"));
				params.put("limit", 1);
				params.put("order", "desc");
				try {
					Map<String, Object> resp = new JSONDeserializer<Map<String, Object>>()
							.deserialize(HttpUtil.getResponse(urlPath, params));
					if ((Integer) resp.get("code") == 0) {
						List<Map<String, Object>> comments = (List<Map<String, Object>>) resp.get("response");
						for (Map<String, Object> map : comments) {
							if (map.get("action").equals("create")) {
								Map<String, Object> meta = (Map<String, Object>) map.get("meta");
								new Comment() .set("userIp", meta.get("ip"))
								.set("userMail", meta.get("author_email"))
								.set("hide", false) .set("commTime", new Date())
								.set("userComment", meta.get("message"))
								.set("userName", meta.get("author_name"))
								.set("logId", meta.get("thread_key"))
								.set("userHome", meta.get("author_url")) 
								.set("td",ParseTools.getDataBySdf( "yyyy-MM-dd HH:mm:ss",
								meta.get("created_at")))
								.set("postId", meta.get("post_id"))
								.save();
								 
							} else if(map.get("action").equals("delete")) {
								List<String> l=(List<String>)map.get("meta");
								for (String str : l) {
									Db.update("delete from comment where postID=?",str);
								}
							}
							//System.out.println(map.get("action"));
						}
					}
					//System.out.println(resp);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("status", 200);
					setAttr("data", map);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		

	}

	private Map<String, Object> getdouShuoRequest() {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List items = upload.parseRequest(getRequest());
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					map.put(item.getFieldName(), item.getString());
				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return map;

	}

	private static String mapToQueryStr(Map<String, Object> params) {
		String queryStr = "";
		if (params != null && !params.isEmpty()) {
			for (Entry<String, Object> param : params.entrySet()) {
				if (param.getValue() instanceof List) {
					@SuppressWarnings("unchecked")
					List<Object> values = (List<Object>) param.getValue();
					for (Object object : values) {
						queryStr += param.getKey() + "=" + object + "&";
					}
				} else {
					queryStr += param.getKey() + "=" + param.getValue() + "&";
				}
			}
			queryStr = queryStr.substring(0, queryStr.length() - 1);
		}
		return queryStr;
	}

	private static String HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws Exception {
		String HMAC_SHA1_ALGORITHM = "HmacSHA1";
		SecretKeySpec signingKey = new SecretKeySpec(encryptKey.getBytes(),
				HMAC_SHA1_ALGORITHM);
		// Get an hmac_sha1 Mac instance and initialise with the signing key
		Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
		mac.init(signingKey);
		// Compute the hmac
		byte[] rawHmac = mac.doFinal(encryptText.getBytes());
		byte[] hexBytes = new Hex().encode(rawHmac);
		byte hex[] = HexaConversionUtil.hexString2Bytes(new String(hexBytes,"ISO-8859-1"));
		return new String(Base64.encodeBase64(hex));
	}

}
