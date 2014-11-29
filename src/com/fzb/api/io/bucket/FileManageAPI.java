package com.fzb.api.io.bucket;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public interface FileManageAPI {

	/**
	 * 删除单个文件
	 * @param file
	 * @return
	 */
	public abstract Map<String,Object> delFile(String file);

	/**
	 * 删除文件夹
	 * @param folder
	 * @return
	 */
	public abstract Map<String,Object> delFolder(String folder);
	
	/**
	 * 添加/创建文件
	 * @param file
	 * @return
	 */
	public abstract Map<String,Object> create(File file);
	
	public abstract Map<String,Object> create(File file,String key);

	public abstract Map<String,Object> moveOrCopy(String folder, String tag, boolean isMove);

	/**
	 * 单个文件的复制/粘贴
	 * @param src
	 * @param tag
	 * @param isMove
	 * @return
	 */
	public abstract Map<String,Object> moveOrCopyFile(String src, String tag, boolean isMove);

	public abstract Map<String,Object> CopyFileByInStream(InputStream in, String tag);

	public abstract Map<String,Object> modifyFile(String root, String code, String content);
	
	public abstract Map<String,Object> getFileList(String folder);

}