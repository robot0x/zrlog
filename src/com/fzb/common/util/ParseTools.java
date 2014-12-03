 package com.fzb.common.util;
 
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
 }

