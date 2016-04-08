package com.rocky.simplemvp.model.impl;
/**
 * 
 * Model层
 * 
 * @author rocky
 *
 */
import com.rocky.simplemvp.http.HTTPManager;
import com.rocky.simplemvp.model.IJokeModel;

public class JokeModelImpl implements IJokeModel{
	
	private HTTPManager mHttp;

	public JokeModelImpl() {
		mHttp = new HTTPManager();
	}
	
	@Override
	public String getJokeInfos(final String url, final String headKey,final String headVal) {
		String infos = null;
		try {
			infos = mHttp.getMethodWithOneHead(url,headKey,headVal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infos;
	}
	
}
