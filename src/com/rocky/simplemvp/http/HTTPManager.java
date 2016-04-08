package com.rocky.simplemvp.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class HTTPManager {
	
	private static final String TAG = "HTTPManager";
	private static OkHttpClient mOkHttpClient = new OkHttpClient();
	
	static{
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
    }
	public String getMethodWithOneHead(String url,String headKey,String headVal) throws IOException {
		Request request = new Request.Builder().url(url).get().addHeader(headKey, headVal).build();
		return sendRequest(request);
	}
	
	public String getMethodWithHeads(String url,HashMap<String, String> headMap) throws IOException {
		Request.Builder builder = new Request.Builder().url(url);
		for (Entry<String, String> entry : headMap.entrySet()) {
			builder = builder.addHeader(entry.getKey(), entry.getValue());
		}
		Request request = builder.build();
		return sendRequest(request);
	}
	
	public void getMethodWithHeads(String url,HashMap<String, String> headMap, MCallback callback) throws IOException {
		Request.Builder builder = new Request.Builder().url(url);
		for (Entry<String, String> entry : headMap.entrySet()) {
			builder = builder.addHeader(entry.getKey(), entry.getValue());
		}
		Request request = builder.build();
		callback.onRequestProgress();
		sendRequest(request, callback);
	}
	
	public String getMethod(String url) throws IOException {
		Request request = new Request.Builder().url(url).get().build();
		return sendRequest(request);
	}
	
	public void getMethod(String url, MCallback callback) throws IOException {
		Request request = new Request.Builder().url(url).get().build();
		callback.onRequestProgress();
		sendRequest(request,callback);
	}
	
	public InputStream getMethodFileStream(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		return sendRequestBackFileStream(request);
	}
	
	
	public String postMethodWithJson(String url,String json) throws IOException {
		MediaType JsonContentType = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JsonContentType, json); 
		Request request = new Request.Builder().url(url).post(body).build();
		return sendRequest(request);
	}
	
	public void postMethodWithJson(String url,String json, MCallback callback) throws IOException {
		MediaType JsonContentType = MediaType.parse("application/json; charset=utf-8");
		RequestBody body = RequestBody.create(JsonContentType, json); 
		Request request = new Request.Builder().url(url).post(body).build();
		callback.onRequestProgress();
		sendRequest(request,callback);
	}
	
	public String postMethodWithKeyAndVal(String url,HashMap<String, String> map) throws IOException {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for (Entry<String, String> entry : map.entrySet()) {
			builder = builder.add(entry.getKey(), entry.getValue());
		}
		RequestBody formBody = builder.build();
		Request request = new Request.Builder().url(url).post(formBody).build();
		return sendRequest(request);
	}

	public void postMethodWithKeyAndVal(String url,HashMap<String, String> map, MCallback callback) throws IOException {
		FormEncodingBuilder builder = new FormEncodingBuilder();
		for (Entry<String, String> entry : map.entrySet()) {
			builder = builder.add(entry.getKey(), entry.getValue());
		}
		RequestBody formBody = builder.build();
		Request request = new Request.Builder().url(url).post(formBody).build();
		sendRequest(request,callback);
	}
	
	protected void sendRequest(Request request, final MCallback mCallback) throws IOException {
		if(mCallback != null){
			mOkHttpClient.newCall(request).enqueue(new Callback() {
				
				@Override
				public void onResponse(Response response) throws IOException {
					Log.i(TAG, "返回码："+response.code());
					mCallback.onRequestSuccess(response.code(),response.body().string());
				}
				
				@Override
				public void onFailure(Request request, IOException exception) {
					Log.e(TAG, "异常信息："+exception.getMessage());
					mCallback.onRequestFail(exception.getMessage());
				}
			});
		}else {
			throw new IllegalStateException("mCallback can not null!");
		}
	}
	
	private String sendRequest(Request request) throws IOException {
		Response response = mOkHttpClient.newCall(request).execute();
		Log.i(TAG, "code:" + response.code());
		if (response.isSuccessful()) {
			return response.body().string();
		}
		return null;
	}
	
	private InputStream sendRequestBackFileStream(Request request) throws IOException {
		Response response = mOkHttpClient.newCall(request).execute();
		Log.i(TAG, ""+response.code());
		if (response.isSuccessful()) {
			return response.body().byteStream();
		}
		return null;
	}
	
	public static String attachHttpGetParams(String url, List<BasicNameValuePair> params){
        return url + "?" + formatParams(params);
    }
	
	private static String formatParams(List<BasicNameValuePair> params) {
		String args = "";
		for (BasicNameValuePair nameValuePair : params) {
			args += nameValuePair.getName() + "=" + nameValuePair.getValue()+"&";
		}
		return args;
	}

	public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }
	
	public interface MCallback{
		void onRequestProgress();
		void onRequestSuccess(int code, String result);
		void onRequestFail(String error);
	}
}
