package com.softwaremaestro.Indoornavigation.Activity;

import com.softwaremaestro.Indoornavigation.R;
import com.softwaremaestro.Indoornavigation.Util.StaticValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SelectMyLocationType extends Activity {
	Button buttonWifi;
	Button buttonMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_selectmylocationtype);

		buttonWifi = (Button) findViewById(R.id.dialog_selectmylocationtype_button_wifi);
		buttonMap = (Button) findViewById(R.id.dialog_selectmylocationtype_button_map);
		buttonWifi.setOnClickListener(l);
		buttonMap.setOnClickListener(l);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == buttonMap) {
				setResult(RESULT_OK, getIntent());
				finish();
			} else if (v == buttonWifi) {
				setResult(RESULT_CANCELED, getIntent());
				finish();
			}
		}
	};
}
