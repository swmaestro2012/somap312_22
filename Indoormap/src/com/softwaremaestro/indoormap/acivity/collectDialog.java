package com.softwaremaestro.indoormap.acivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.softwaremaestro.indoormap.R;
import com.softwaremaestro.indoormap.util.StaticValue;

public class collectDialog extends Activity {
	int pointID;
	int pointType;
	int measureType;

	WifiManager wifimanager;

	Button DeleteButton;
	Button ScanningButton;
	Button UpdateButton;
	TextView resultView;
	Intent intent;
	Handler mHandler;
	List<ScanResult> mScanResult;

	Runnable run;
	int count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_collect);

		DeleteButton = (Button) findViewById(R.id.dialog_collect_button_delete);
		DeleteButton.setOnClickListener(l);
		ScanningButton = (Button) findViewById(R.id.dialog_collect_button_scanning);
		ScanningButton.setOnClickListener(l);
		UpdateButton = (Button) findViewById(R.id.dialog_collect_button_update);
		UpdateButton.setOnClickListener(l);
		UpdateButton.setEnabled(false);
		resultView = (TextView) findViewById(R.id.dialog_collect_textview_result);
		resultView.setText("scan init");

		intent = getIntent();
		pointID = intent.getIntExtra("id", -1);
		pointType = intent.getIntExtra("type", -1);
		if (pointType == StaticValue.TYPE_MEASURE) {
			measureType = intent.getIntExtra("point", -1);

			wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
			if (wifimanager.isWifiEnabled() == false)
				wifimanager.setWifiEnabled(true);

		} else {
			ScanningButton.setVisibility(View.GONE);
			UpdateButton.setVisibility(View.GONE);
			resultView.setVisibility(View.GONE);
		}

	}

	private void getResult() {
		mScanResult = wifimanager.getScanResults();
		ArrayList<ScanResult> mResult = new ArrayList<ScanResult>();
		for (ScanResult res : mScanResult) {
			mResult.add(res);
			resultView.setText((count + 1) + "/25 (scanning....)" + "\n");
		}
		if (count > 4)
			intent.putParcelableArrayListExtra("result" + (count - 5), mResult);
		if (count < 25) {
			count++;
		} else {
			unregisterReceiver(mReceiver);
			resultView.setText("scan complete" + "\n");
			UpdateButton.setEnabled(true);
			ScanningButton.setEnabled(true);
		}
	}

	public void initWIFIScan() {
		// init WIFISCAN
		count = 0;
		final IntentFilter filter = new IntentFilter(
				WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(mReceiver, filter);
		wifimanager.startScan();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				getResult();
				wifimanager.startScan(); // for refresh
			} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
			}
		}
	};

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == DeleteButton) {
				intent.putExtra("command", StaticValue.COMMAND_DELETE);
				setResult(RESULT_OK, intent);
				finish();
			}
			if (v == ScanningButton) {
				ScanningButton.setEnabled(false);
				UpdateButton.setEnabled(false);
				initWIFIScan();
				resultView.setText("scan init");
			}
			if (v == UpdateButton) {
				intent.putExtra("command", StaticValue.COMMAND_UPDATE);
				setResult(RESULT_OK, intent);
				finish();
			}
		}

	};
}
