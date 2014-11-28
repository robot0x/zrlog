package com.fzb.common.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ParseTools {

	public ParseTools() {
	};

	public static String isRecommend(int isRecommend) {
		return isRecommend == 1 ? "是" : "否";
	}

	public static String htmlspecialchars(String str) {
		// str = str.replaceAll("&", "&amp;");
		// str = str.replaceAll("<", "&lt;");
		// str = str.replaceAll(">", "&gt;");
		 str = str.replaceAll("<div(.*)|(.*)(\n*)</div>", "");
		 str = str.replaceAll("<script(.*)|(.*)(\n*)</script>", "");
		return str;
	}
	public static String subString(String str,int lenght) {
		// str = str.replaceAll("&", "&amp;");
		// str = str.replaceAll("<", "&lt;");
		// str = str.replaceAll(">", "&gt;");
		if(str.length()>lenght)
		{
			str=str.substring(0,lenght)+" ...";
		}
		return str;
	}

	public static String getDbtype(int dbType) {
		if(dbType==1)
		{
			return "sql server";
		}
		return "mysql";
	}
	public static String getRandomFileName() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()) + "_" + new Random().nextInt(1000) + ".bak";
	}
	public static String getRandomFileName(String subfix) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()) + "_" + new Random().nextInt(1000) + "."+subfix;
	}
	
	public static File getRandomFileNameByOld(File file) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return new File(file.getParent()+df.format(new Date()) + "_" + new Random().nextInt(1000)+file.toString().substring(file.toString().lastIndexOf(".")));
	}
	public static String getRandomStr() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date()) + "_" + new Random().nextInt(1000);
	}
	
	public static String paramToString(String str){
		if(str!=null){
			return str;
		}
		else{
			return "";
		}
	}
	
}
