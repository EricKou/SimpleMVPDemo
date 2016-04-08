package com.rocky.simplemvp.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.rocky.simplemvp.presenter.JokePresenter;
import com.rocky.testsimplemvp.view.activity.R;
/**
 * 
 * View层
 * 
 * @author rocky
 *
 */
public class MainActivity extends Activity implements IJokeView, OnClickListener{
	private static final String TAG = "MainActivity";
	private JokePresenter presenter;
	private Button mBt;
	private TextView mTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBt = (Button) findViewById(R.id.bt);
		mTv = (TextView) findViewById(R.id.tv);
		mBt.setOnClickListener(this);
		presenter = new JokePresenter(this);
	}
	
	@Override
	public void setJoke(String jokes) {
		Log.i(TAG, ""+jokes);
		mTv.setText(jokes);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt:
			getJoke();
			break;
		}
	}
	
	private void getJoke(){
		presenter.getJoke();
	}
}
