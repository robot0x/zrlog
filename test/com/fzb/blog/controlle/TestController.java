package com.fzb.blog.controlle;

import java.io.File;
import java.util.Random;

import com.fzb.blog.dev.MailUtil;

public class TestController extends ManageControl{

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryAll() {
		// TODO Auto-generated method stub
		
	}

	public void sendMail(){
		try {
			MailUtil.sendMail("504008147@qq.com",
					"测试邮件" + new Random().nextDouble(), "这里改写些撒?",new File("/home/xiaochun/图片/4.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
