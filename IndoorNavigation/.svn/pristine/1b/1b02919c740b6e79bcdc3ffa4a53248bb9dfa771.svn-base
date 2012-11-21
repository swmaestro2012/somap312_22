package com.softwaremaestro.Indoornavigation.Activity;

import com.softwaremaestro.Indoornavigation.R;
import com.softwaremaestro.Indoornavigation.Util.PreferenceUtil;
import com.softwaremaestro.Indoornavigation.Util.StaticValue;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MyMenuActivity extends Activity {
	View buttonRecentSearchList;
	View switchSoundOn;
	View switchSoundOff;
	View switchVibrateOn;
	View switchVibrateOff;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymenu);

		buttonRecentSearchList = (View) findViewById(R.id.activity_mymenu_button_recentsearchlist);
		switchSoundOn = (View) findViewById(R.id.activity_mymenu_switch_sound_on);
		switchSoundOff = (View) findViewById(R.id.activity_mymenu_switch_sound_off);
		switchVibrateOn = (View) findViewById(R.id.activity_mymenu_switch_vibrate_on);
		switchVibrateOff = (View) findViewById(R.id.activity_mymenu_switch_vibrate_off);

		buttonRecentSearchList.setOnClickListener(l);
		switchSoundOn.setOnClickListener(l);
		switchSoundOff.setOnClickListener(l);
		switchVibrateOn.setOnClickListener(l);
		switchVibrateOff.setOnClickListener(l);

		initSwitch();
	}

	private void initSwitch() {
		String sound = PreferenceUtil.getAppPreferences(this,
				StaticValue.KEY_SOUND_ENABLE);
		String vibrate = PreferenceUtil.getAppPreferences(this,
				StaticValue.KEY_VIBRATE_ENABLE);

		if (sound.equals("")) {
			sound = "off";
			PreferenceUtil.setAppPreferences(this,
					StaticValue.KEY_SOUND_ENABLE, sound);
		}
		if (vibrate.equals("")) {
			vibrate = "off";
			PreferenceUtil.setAppPreferences(this,
					StaticValue.KEY_VIBRATE_ENABLE, vibrate);
		}
		if (sound.equals("on")) {
			setSoundOn();
		} else {
			setSoundOff();
		}
		if (vibrate.equals("on"))
			setVibrateOn();
		else
			setVibrateOff();
	}

	public void setSoundOn() {
		switchSoundOn.setVisibility(View.VISIBLE);
		switchSoundOff.setVisibility(View.INVISIBLE);
		switchSoundOn.setEnabled(true);
		switchSoundOff.setEnabled(false);
	}

	public void setSoundOff() {
		switchSoundOn.setVisibility(View.INVISIBLE);
		switchSoundOff.setVisibility(View.VISIBLE);
		switchSoundOn.setEnabled(false);
		switchSoundOff.setEnabled(true);
	}
	
	public void setVibrateOn() {
		switchVibrateOn.setVisibility(View.VISIBLE);
		switchVibrateOff.setVisibility(View.INVISIBLE);
		switchVibrateOn.setEnabled(true);
		switchVibrateOff.setEnabled(false);
	}

	public void setVibrateOff() {
		switchVibrateOn.setVisibility(View.INVISIBLE);
		switchVibrateOff.setVisibility(View.VISIBLE);
		switchVibrateOn.setEnabled(false);
		switchVibrateOff.setEnabled(true);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == buttonRecentSearchList)
				startActivityForResult(new Intent(MyMenuActivity.this,
						RecentSearchList.class),
						StaticValue.REQUEST_RECENTSEARCHLIST);
			if (v == switchSoundOn) {
				setSoundOff();
				PreferenceUtil.setAppPreferences(MyMenuActivity.this,
						StaticValue.KEY_SOUND_ENABLE, "off");
			}
			if (v == switchSoundOff) {
				setSoundOn();
				PreferenceUtil.setAppPreferences(MyMenuActivity.this,
						StaticValue.KEY_SOUND_ENABLE, "on");
			}
			if (v == switchVibrateOn) {
				setVibrateOff();
				PreferenceUtil.setAppPreferences(MyMenuActivity.this,
						StaticValue.KEY_VIBRATE_ENABLE, "off");
			}
			if (v == switchVibrateOff) {
				setVibrateOn();
				PreferenceUtil.setAppPreferences(MyMenuActivity.this,
						StaticValue.KEY_VIBRATE_ENABLE, "on");
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == StaticValue.REQUEST_RECENTSEARCHLIST) {
			if (resultCode == RESULT_OK) {
				int poiID = data.getIntExtra(StaticValue.KEY_POI_ID, -1);
				if (poiID != -1) {
					Intent intent = new Intent(MyMenuActivity.this,
							MapActivity.class);
					intent.putExtra(StaticValue.KEY_POI_ID, poiID);
					intent.putExtra(StaticValue.KEY_POI_X,
							data.getDoubleExtra(StaticValue.KEY_POI_ID, -1));
					intent.putExtra(StaticValue.KEY_POI_Y,
							data.getDoubleExtra(StaticValue.KEY_POI_ID, -1));
					intent.putExtra(StaticValue.KEY_POI_Z,
							data.getDoubleExtra(StaticValue.KEY_POI_ID, -1));
					intent.putExtra(StaticValue.KEY_CALL_TYPE,
							StaticValue.TYPE_SELECT_DESTINATION);
					startActivity(intent);
					finish();
				}
			}
		}
	}

}
