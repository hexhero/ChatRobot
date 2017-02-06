package com.yb.chartRobot;

public class ChatBean {

	public ChatBean(String content, boolean isAsk) {
		super();
		this.content = content;
		this.isAsk = isAsk;
	}

	public String content;	//内容
	
	public boolean isAsk;	//是否是提问
	
}
