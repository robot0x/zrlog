package com.fzb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtil {

	public static void unZip(String src,String target) throws IOException{
		ZipInputStream zipIn=null;
		ZipFile zip=null;
		try {
			//FIXME zip 文件不能有中文
			zipIn = new ZipInputStream(new FileInputStream(src));
			ZipEntry in=null;
			zip=new ZipFile(src);
			while((in=zipIn.getNextEntry())!=null){
				File file=new File(target+in.getName());
				if(in.getName().endsWith("/")){
					file.mkdirs();
				}
				else{
					byte[] b=IOUtil.getByteByInputStream(zip.getInputStream(in));
					FileOutputStream fout=new FileOutputStream(file);
					fout.write(b);
					fout.close();
				}
			}
			zip.close();
		} finally{
			if(zipIn!=null){
				zipIn.close();
			}
			if(zip!=null){
				zip.close();
			}
		}
	}
	public static void inZip(String src,String target){
		
	}
	public static void main(String[] args) {
		try {
			unZip("E:/putty.zip", "E:/test/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
