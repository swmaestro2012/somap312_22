package com.softwaremaestro.indoormap.acivity;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.EditText;

import com.softwaremaestro.indoormap.R;
import com.softwaremaestro.indoormap.util.MeasurePointOverlayView;
import com.softwaremaestro.indoormap.util.POIOverlayView;
import com.softwaremaestro.indoormap.util.PreferenceUtil;
import com.softwaremaestro.indoormap.util.StaticValue;

public class IntroActivity extends Activity{
	MappingService svc;
	Handler mHandler;
	String IP;

	Handler inputhandler;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setInputHandler();
		createPOIInputDialog().show();
	}
	public Builder createPOIInputDialog() {
		Builder POIInputDialog;
		final EditText input = new EditText(IntroActivity.this);
		input.setText("");
		POIInputDialog = new AlertDialog.Builder(IntroActivity.this);
		POIInputDialog.setView(input);
		POIInputDialog.setTitle("DB 서버 IP입력 ");
		POIInputDialog.setPositiveButton("확인",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						IP = input.getText().toString();
						inputhandler.sendEmptyMessage(1);
					}
				});

		return POIInputDialog;

	}
	public void setInputHandler() {
		inputhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				PreferenceUtil.setAppPreferences(IntroActivity.this, "IP", IP);
				startActivity(new Intent(IntroActivity.this,selectBuildingActivity.class));
				finish();
			}
		};
	}
}
