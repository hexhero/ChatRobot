package com.yb.chartRobot;

import android.content.Context;

import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;

public class TulingUtil {

	private String API_KEY = "1a5abf341652465ba8883bf9ab12b2fb";
	private String SECRET = "37437e263a8f2207";
	private TuringManager turingManager;
	
	public TulingUtil(Context ctx) {
		turingManager = new TuringManager(ctx, API_KEY, SECRET);
	}
	
	/**
	 * 询问问题
	 * @param ask
	 */
	public void ask(String ask) {
		turingManager.requestTuring(ask);		
	}
	
	/**
	 * 回答监听
	 * @param httpRequestListener
	 */
	public void answer(HttpRequestListener httpRequestListener){
		turingManager.setHttpRequestListener(httpRequestListener);
	}
}
