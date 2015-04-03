package com.fzb.blog.model;

import com.fzb.common.util.ParseTools;
import com.jfinal.plugin.activerecord.Db;
/*    */
import com.jfinal.plugin.activerecord.Model;




import java.text.SimpleDateFormat;
import java.util.Date;
/*    */
import java.util.HashMap;
/*    */
import java.util.List;
/*    */
import java.util.Map;

public class Comment extends Model<Comment> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Comment dao = new Comment();

	public Map<String, Object> getCommentsByPage(int page, int pageSize) {
		Map data = new HashMap();
		String sql = "select commentId as id,userComment,userMail,userHome,userIp,userName,hide,logId from comment limit ?,?";
		data.put(
				"rows",
				find(sql,
						new Object[] {
								Integer.valueOf(ParseTools.getFirstRecord(page,
										pageSize)), Integer.valueOf(pageSize) }));
		fillData(page, pageSize, "from comment", data, new Object[0]);
		return data;
	}
	
	public Long getCommentCount(){
		String sql="select count(1) from comment";
		return Db.findFirst(sql).get("count(1)");
	}
	
	public Long getToDayCommentCount(){
		String sql="select count(1) from comment where DATE_FORMAT(commTime,'%Y_%m_%d')=?";
		return Db.findFirst(sql,new SimpleDateFormat("yyyy_MM_dd").format(new Date())).get("count(1)");
	}
	
	public Map<String, Object> noRead(int page, int pageSize) {
		Map data = new HashMap();
		String sql = "select commentId as id,userComment,userMail,userHome,userIp,userName,hide,logId from comment limit ?,?";
		data.put(
				"rows",
				find(sql,
						new Object[] {
								Integer.valueOf(ParseTools.getFirstRecord(page,
										pageSize)), Integer.valueOf(pageSize) }));
		fillData(page, pageSize, "from comment", data, new Object[0]);
		return data;
	}

	@SuppressWarnings("unchecked")
	private void fillData(int page, int pageSize, String where,
			Map<String, Object> data, Object[] obj) {
		if (((List<Comment>) data.get("rows")).size() > 0) {
			data.put("page", Integer.valueOf(page));
			long count = ((Comment) findFirst("select count(commentId) cnt "
					+ where, obj)).getLong("cnt").longValue();
			data.put("total",
					Integer.valueOf(ParseTools.getTotalPate(count, pageSize)));
			data.put("records", Long.valueOf(count));
		} else {
			data.clear();
		}
	}
	public List<Comment> getCommentsByLogId(int logId){
		return find("select * from comment where logId=?",logId);
	}
}
