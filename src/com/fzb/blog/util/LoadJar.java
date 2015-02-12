package com.fzb.blog.util;

import java.net.URL;
import java.net.URLClassLoader;

public class LoadJar extends URLClassLoader{

	public LoadJar(URL[] urls) {
		super(urls);
	}
	@Override
	protected void addURL(URL url) {
		super.addURL(url);
	}
	
	public void addJar(URL url){
		this.addURL(url);
	}

}
