package com.fzb.blog.controlle;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fzb.blog.dev.MailUtil;
import com.fzb.blog.model.Comment;
import com.fzb.common.util.HttpUtil;
import com.fzb.common.util.ParseTools;
import com.jfinal.plugin.activerecord.Db;

import flexjson.JSONDeserializer;

public class CommentControl extends ManageControl {
	private static final Logger log = LoggerFactory
			.getLogger(CommentControl.class);
	
	public void delete() {
		String[] ids = getPara("id").split(",");
		for (String id : ids) {
			Comment.dao.deleteById(id);
		}
	}

	public void queryAll() {
		renderJson(Comment.dao.getCommentsByPage(getParaToInt("page")
				.intValue(), getParaToInt("rows").intValue()));
	}

	@Override
	public void add() {

	}

	@Override
	public void update() {
		
	}

	public void refresh() {
		// 清空表数据
		Db.update("TRUNCATE comment");

		// 使用签名
		String urlPath = "http://api.duoshuo.com/log/list.json";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("short_name", getValuebyKey("duoshuo_short_name"));
		params.put("secret", getValuebyKey("duoshuo_secret"));
		params.put("limit", 10000);
		params.put("order", "desc");
		try {
			Map<String, Object> resp = new JSONDeserializer<Map<String, Object>>()
					.deserialize(HttpUtil.getResponse(urlPath, params));
			if ((Integer) resp.get("code") == 0) {
				List<Map<String, Object>> comments = (List<Map<String, Object>>) resp
						.get("response");
				for (Map<String, Object> map : comments) {
					if (map.get("action").equals("create")) {
						Map<String, Object> meta = (Map<String, Object>) map
								.get("meta");
							if (meta.get("thread_key") != null) {
								new Comment()
										.set("userIp", meta.get("ip"))
										.set("userMail",
												meta.get("author_email"))
										.set("hide", false)
										.set("commTime", new Date())
										.set("userComment", meta.get("message"))
										.set("userName",
												meta.get("author_name"))
										.set("logId", meta.get("thread_key"))
										.set("userHome", meta.get("author_url"))
										.set("td",
												ParseTools.getDataBySdf(
														"yyyy-MM-dd HH:mm:ss",
														meta.get("created_at")))
										.set("postId", meta.get("post_id"))
										.save();
							}
					}
					// System.out.println(map.get("action"));
				}
			}
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("status", 200);
			map.put("message", "更新完成");
			setAttr("data", map);
			renderJson(map);
		} catch (Exception e) {
			log.error("refresh sync error ",e);
		}
	}

}
