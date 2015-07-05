package com.fzb.blog.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fzb.common.util.ParseTools;
import com.jfinal.plugin.activerecord.Model;

public class Link extends Model<Link> {
	public static final Link dao = new Link();

	public List<Link> queryAll() {
		return find("select linkName,linkId as id,sort,url from link order by sort");
	}

	public Map<String, Object> queryAll(Integer page, Integer pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(
				"rows",
				find("select linkName,linkId as id,sort,url,alt from link order by sort limit ?,?",
						new Object[] {
								Integer.valueOf(ParseTools.getFirstRecord(page,
										pageSize)), Integer.valueOf(pageSize) }));
		fillData(page, pageSize, "from link", data, new Object[0]);
		return data;
	}

	private void fillData(int page, int pageSize, String where,
			Map<String, Object> data, Object[] obj) {
		if (((List<Link>) data.get("rows")).size() > 0) {
			data.put("page", Integer.valueOf(page));
			long count = ((Link) findFirst("select count(1) cnt " + where, obj))
					.getLong("cnt").longValue();
			data.put("total",
					Integer.valueOf(ParseTools.getTotalPate(count, pageSize)));
			data.put("records", Long.valueOf(count));
		} else {
			data.clear();
		}
	}
}
