package com.yb.chartRobot;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.turing.androidsdk.HttpRequestListener;
import com.yb.chartRobot.VoiceUtil.OnResultListener;

public class MainActivity extends Activity {

	private ListView lvChar;
	private Button btnInput;
	private List<ChatBean> chats = new ArrayList<ChatBean>();
	private VoiceUtil voiceUtil;
	private ChatRobot mAdapter;
	private TulingUtil tulingUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		voiceUtil = new VoiceUtil(this);
		tulingUtil = new TulingUtil(this);
		
		initView();		
		initData();
	}

	private void initView() {
		lvChar = (ListView) findViewById(R.id.lv_chat_content);
		btnInput = (Button) findViewById(R.id.btn_input);
		
	}
	
	private void initData() {
		btnInput.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listenerVoice();
				
			}			
		});
		
		
		mAdapter = new ChatRobot();
		
		lvChar.setAdapter(mAdapter);
		
		tulingUtil.answer(new HttpRequestListener() {
			
			@Override
			public void onSuccess(String result) {								
				try {
					JSONObject jb = new JSONObject(result);
					String answer = jb.getString("text");

					chats.add(new ChatBean(answer, false));		
					
					mAdapter.notifyDataSetChanged();
					//声音回复
					voiceUtil.string2voice(answer);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lvChar.setSelection(chats.size());
			}
			
			@Override
			public void onFail(int arg0, String arg1) {
				//声音回复
				voiceUtil.string2voice("我反应不过来!!");
				
			}
		});
		
	}
	private void listenerVoice() {
		voiceUtil.voice2stringUI(new OnResultListener() {
			
			@Override
			public void result(String result) {
				chats.add(new ChatBean(result, true));
				mAdapter.notifyDataSetChanged();
				tulingUtil.ask(result);				
				lvChar.setSelection(chats.size());
			}
		});
		
	}
	

	class ChatRobot extends BaseAdapter{

		@Override
		public int getCount() {			
			return chats.size();
		}

		@Override
		public ChatBean getItem(int position) {			
			return chats.get(position);
		}

		@Override
		public long getItemId(int position) {			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.list_item_chat, null);
				holder = new ViewHolder();
				holder.tvAsk = (TextView) convertView.findViewById(R.id.tv_ask);
				holder.tvAnswer = (TextView) convertView.findViewById(R.id.tv_answer);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			ChatBean chatBean = getItem(position);
			if(chatBean.isAsk){
				holder.tvAsk.setText(chatBean.content);
				holder.tvAsk.setVisibility(View.VISIBLE);
				holder.tvAnswer.setVisibility(View.GONE);
			}else{
				holder.tvAnswer.setText(chatBean.content);
				holder.tvAnswer.setVisibility(View.VISIBLE);
				holder.tvAsk.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	
	static class ViewHolder {
		TextView tvAsk;
		TextView tvAnswer;
	}
}
