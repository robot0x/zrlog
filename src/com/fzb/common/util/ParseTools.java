 package com.fzb.common.util;
 
 import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
 
 public class ParseTools
 {
   public static int getFirstRecord(int page, int pageSize)
   {
     return (page - 1) * pageSize;
   }
   public static int getTotalPate(long count, int pageSize) {
     return (int)Math.ceil(count / (pageSize * 1.0D));
   }
   public static String autoDigest(String str, int size) {
	 String digest="";
   /*  Document document = Jsoup.parse(str);
     Node localNode = null;
     for (Iterator localIterator = document.childNodes().iterator(); localIterator.hasNext(); localNode = (Node)localIterator.next()){
    	 if(localNode!=null){
    		 digest=localNode.outerHtml();
    	 }
    	 System.out.println(localNode);
     }
     System.out.println(digest);*/
     return str;
   }
 
   public static String toISO8601(Date releaseTime) {
     return releaseTime.getYear() + 1900 + (releaseTime.getMonth() + 1) + releaseTime.getDate() + "T" + releaseTime.getHours() + releaseTime.getMinutes() + releaseTime.getSeconds() + "+08";
   }
   
   public static Date getDataBySdf (String sdfStr,Object dateStr) throws ParseException{
	   if(dateStr!=null){
		   SimpleDateFormat sdf=new SimpleDateFormat(sdfStr);
		   return sdf.parse(dateStr.toString().replace("T"," "));
	   }
	   return new Date();
   }
   public static void main(String[] args) {
	try {
		getDataBySdf("yyyy-MM-dd hh:mm", "2014-03-16T22:57:26+08:00");
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
 }


