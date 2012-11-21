package com.softwaremaestro.Indoornavigation.Activity;

import java.io.IOException;

import kr.softwaremaestro.indoor.engine.WiFiRadioMap;

import com.softwaremaestro.Indoornavigation.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;

public class IntroActivity extends Activity {
	Handler mHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        
        mHandler = new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		// TODO Auto-generated method stub
        		super.handleMessage(msg);
        		startActivity(new Intent(IntroActivity.this,MenuActivity.class));
        		finish();
        	}
        };
    
        
        Runnable R = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					WiFiRadioMap.getinstance(getApplicationContext());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendEmptyMessage(1);
			}
		};
		
		mHandler.postDelayed(R, 500);
    }
}
