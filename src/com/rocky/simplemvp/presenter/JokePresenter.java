package com.rocky.simplemvp.presenter;

import android.os.Handler;
import android.os.Message;

import com.rocky.simplemvp.model.IJokeModel;
import com.rocky.simplemvp.model.impl.JokeModelImpl;
import com.rocky.simplemvp.util.Constant;
import com.rocky.simplemvp.view.activity.IJokeView;

/**
 * 
 * Presenter层
 * 
 * @author rocky
 *
 */
public class JokePresenter {
	private IJokeModel model;
	private IJokeView view;
	private String infos = "";
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				view.setJoke(infos);
				break;

			default:
				break;
			}
		};
	};
	
	public JokePresenter(IJokeView view) {
		model = new JokeModelImpl();
		this.view = view;
	}
	
	public void getJoke() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				infos = model.getJokeInfos(Constant.url, Constant.headKey, Constant.headVal);
				if(infos != null){
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = infos;
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
}
