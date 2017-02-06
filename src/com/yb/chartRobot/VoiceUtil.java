package com.yb.chartRobot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class VoiceUtil {

	private Activity ctx;
	
	private static final String TAG = "VoiceUtil";
	private StringBuilder sb = new StringBuilder();;
	
	public VoiceUtil(Activity ctx) {
		this.ctx = ctx;
		SpeechUtility.createUtility(ctx, SpeechConstant.APPID + "=589832ac"); // 初始化
	}

	public void voice2stringUI(final OnResultListener resultListener) {

		// 1.创建RecognizerDialog对象
		RecognizerDialog mDialog = new RecognizerDialog(ctx, null);
		// 2.设置accent、language等参数
		mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
		// 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
		// 结果
		// mDialog.setParameter("asr_sch", "1");
		// mDialog.setParameter("nlp_version", "2.0");

		// 3.设置回调接口
		mDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				try {
					Log.d(TAG, "result:" + results.getResultString());
						
					
					String result = results.getResultString();
					JSONObject jb = new JSONObject(result);	
					
					JSONArray jbArr = jb.getJSONArray("ws");
					for (int i = 0; i < jbArr.length(); i++) {
						sb.append(jbArr.getJSONObject(i).getJSONArray("cw")
								.getJSONObject(0).getString("w"));
					}
					
					if (jb.getBoolean("ls")) {	
						System.out.println(sb.toString());
						if (resultListener != null) {
							resultListener.result(sb.toString());							
						}
						sb.delete(0, sb.length());				
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(SpeechError arg0) {

			}
		});
		// 4.显示dialog，接收语音输入
		mDialog.show();
	}

	public void string2voice(String tx) {
		// 1.创建 SpeechSynthesizer 对象, 第二个参数：本地合成时传 InitListener
		SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(ctx, null);
		// 2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
		// //设置发音人（更多在线发音人，用户可参见 附录13.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoqi");
		// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");
		// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "50");
		// 设置音量，范围 0~100
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置云端 //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
		// 保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限 //仅支持保存为 pcm 和 wav
		// 格式，如果不需要保存合成音频，注释该行代码
		// mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH,
		// "./sdcard/iflytek.pcm");
		// 3.开始合成
		mTts.startSpeaking(tx, null);

	}

	public interface OnResultListener{
		public void result(String result);
	}

}
