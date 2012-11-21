package com.softwaremaestro.Indoornavigation.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import kr.softwaremaestro.indoor.engine.Localization;
import kr.softwaremaestro.indoor.engine.WiFiRadioMap;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;

import com.softwaremaestro.Indoornavigation.R;
import com.softwaremaestro.Indoornavigation.Util.DraggableImageView;
import com.softwaremaestro.Indoornavigation.Util.MyLocationOverlayView;
import com.softwaremaestro.Indoornavigation.Util.StaticValue;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class MapActivity extends Activity {
	Intent intent;
	DraggableImageView DimageView;
	FrameLayout map_frame;
	Bitmap[] mapArray;
	boolean bSelectMyLocationOnMap;
	Button buttonShowMyLocation;
	Button buttonSpinner;
	Button buttonFloor1;
	Button buttonFloor2;
	Localization mLocalization;
	WiFiRadioMap mWrm;
	WifiManager wifimanager;
	List<ScanResult> mScanResult;
	List<Accesspoint> apList;
	List<Point> pathList;
	MyLocationOverlayView mLocationOverlay;
	Handler imageHandler;
	boolean bScanstate;
	boolean bValidPosition;
	boolean bSetDestination;
	boolean bSetModeManualPosition;
	boolean bNowNavigating;
	Point Destination;
	Point postSource;
	Point Source;
	Point preSource;
	int zOrder;
	Handler scanHandler;
	Runnable scanRunnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		bSelectMyLocationOnMap = false;

		DimageView = (DraggableImageView) findViewById(R.id.activity_map_draggableimageview);
		map_frame = (FrameLayout) findViewById(R.id.activity_map_framelayout);

		initMapBitmap();

		try {
			mWrm = WiFiRadioMap.getinstance(getApplicationContext());

			apList = mWrm.getAccesspoints();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mLocalization = new Localization(mWrm);
		bSetDestination = false;
		bSetModeManualPosition = false;
		bNowNavigating = false;
		bScanstate = false;

		setImageHandler();
		Bitmap bit = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
		mLocationOverlay = new MyLocationOverlayView(this);
		mLocationOverlay.setLayoutParams(DimageView.getLayoutParams());
		mLocationOverlay.setImageBitmap(bit);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		DimageView.setDmetric(metrics);
		((MyLocationOverlayView) mLocationOverlay).setScale(metrics.density);
		map_frame.addView(mLocationOverlay);
		buttonShowMyLocation = (Button) findViewById(R.id.activity_map_button_mylocation);
		buttonShowMyLocation.setOnClickListener(l);
		buttonSpinner = (Button) findViewById(R.id.activity_map_button_spinner);
		buttonSpinner.setOnClickListener(l);
		buttonFloor1 = (Button) findViewById(R.id.activity_map_button_floor1);
		buttonFloor1.setOnClickListener(l);
		buttonFloor2 = (Button) findViewById(R.id.activity_map_button_floor2);
		buttonFloor2.setOnClickListener(l);

		intent = getIntent();
		int calltype = intent.getIntExtra(StaticValue.KEY_CALL_TYPE, -1);
		if (calltype == StaticValue.TYPE_SELECT_DESTINATION) {
			int POIID = intent.getIntExtra(StaticValue.KEY_POI_ID, -1);
			double X = intent.getDoubleExtra(StaticValue.KEY_POI_X, -1);
			double Y = intent.getDoubleExtra(StaticValue.KEY_POI_Y, -1);
			int Z = intent.getIntExtra(StaticValue.KEY_POI_Z, -1);
			Destination = new Point(POIID, X, Y, (double) Z);
			((MyLocationOverlayView) mLocationOverlay)
					.setDestination(Destination);
			bSetDestination = true;
			DimageView.setHandler(imageHandler);
			changeFloor(Z);
			DimageView.focusToPoint(Destination);
		} else {
			DimageView.setHandler(imageHandler);
			changeFloor(2);
		}
		setScanHandler();
	}

	public void setScanHandler() {
		scanHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (bScanstate) {
					getScanResult();
					wifimanager.startScan();
				}
			}
		};
		scanRunnable = new Runnable() {

			@Override
			public void run() {
				scanHandler.sendEmptyMessage(1);
				if (bScanstate)
					scanHandler.postDelayed(scanRunnable, 1000);
			}
		};
	}

	public void setImageHandler() {
		imageHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case StaticValue.MSG_SYNC_IMAGEVIEW:
					Matrix m = DimageView.getImageMatrix();
					mLocationOverlay.setImageMatrix(m);
					mLocationOverlay.invalidate();
					DimageView.invalidate();
					break;
				case StaticValue.MSG_INIT_IMAGEVIEW:
					int[] info = (int[]) msg.obj;
					((MyLocationOverlayView) mLocationOverlay).setScale(info);
					break;
				case StaticValue.MSG_CLICK_VIEW:
					Bundle bundle = msg.getData();
					float xPos = bundle.getFloat("X");
					float yPos = bundle.getFloat("Y");
					callbackClickView(xPos, yPos);
					break;
				}
			}
		};
	}

	private void callbackClickView(float x, float y) {
		spinnerDropDownCancle();
		if (bSetDestination && bSetModeManualPosition && !bNowNavigating) {
			Source = new Point(-1, (double) x, (double) y, (double) zOrder);
			((MyLocationOverlayView) mLocationOverlay).setMyLocation(Source);
			startActivityPreviewWay();
			DimageView.setEnabled(false);
		}
	}

	public void spinnerDropDownCancle() {
		buttonFloor1.setVisibility(View.INVISIBLE);
		buttonFloor1.setEnabled(false);
		buttonFloor2.setVisibility(View.INVISIBLE);
		buttonFloor2.setEnabled(false);
	}

	public void spinnerDropDown() {
		buttonFloor1.setVisibility(View.VISIBLE);
		buttonFloor1.setEnabled(true);
		buttonFloor2.setVisibility(View.VISIBLE);
		buttonFloor2.setEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		spinnerDropDownCancle();
		wifimanager = (WifiManager) getSystemService(WIFI_SERVICE);
		if (wifimanager.isWifiEnabled() == false)
			wifimanager.setWifiEnabled(true);
		if (!bSetModeManualPosition)
			startWIFIScan();
		bValidPosition = false;
	}

	private void initMapBitmap() {
		mapArray = new Bitmap[2];
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inScaled = false;
		option.inPurgeable = true;
		mapArray[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.map_1, option);
		mapArray[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.map_2, option);
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == buttonSpinner) {
				if (buttonFloor1.getVisibility() == View.VISIBLE)
					spinnerDropDownCancle();
				else
					spinnerDropDown();
			}
			if (v == buttonFloor1) {
				changeFloor(1);
				spinnerDropDownCancle();
			}
			if (v == buttonFloor2) {
				changeFloor(2);
				spinnerDropDownCancle();
			}
			if (v == buttonShowMyLocation) {
				if (bNowNavigating)
					return;
				if (!bValidPosition) {
					startActivity(new Intent(MapActivity.this,
							InvalidPositionDialog.class));
					return;
				}
				if (!bScanstate)
					startWIFIScan();
				if (Source == null) {
				} else {
					changeFloor(Source.getZ().intValue());
					DimageView.focusToPoint(Source);
				}

			}
		}

	};

	public void changeFloor(int floor) {
		if (zOrder != floor) {
			buttonFloor1.setTextColor(Color.WHITE);
			buttonFloor2.setTextColor(Color.WHITE);
			if (floor == 1) {
				buttonSpinner.setText(buttonFloor1.getText().toString());
				buttonFloor1.setTextColor(Color.YELLOW);
			} else {
				buttonSpinner.setText(buttonFloor2.getText().toString());
				buttonFloor2.setTextColor(Color.YELLOW);
			}

			setzOrder(floor);
			DimageView.setImageBitmap(mapArray[zOrder - 1]);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == StaticValue.REQUEST_MYLOCATIONTYPE) {
			if (resultCode == RESULT_OK) {
				bSetModeManualPosition = true;
			} else if (resultCode == RESULT_CANCELED) {
				bSetDestination = false;
			}
		}
		if (requestCode == StaticValue.REQUEST_PREVIEW_WAY) {
			if (resultCode == RESULT_OK) {
				if (!bSetModeManualPosition) {
					bNowNavigating = true;
					DimageView.setEnabled(false);
				} else {
					bNowNavigating = true;
					DimageView.setEnabled(false);
					pathList = mLocalization.getShortestPath(Source,
							Destination);
					moveHandler.postDelayed(r, 1000);

				}
			} else if (resultCode == RESULT_CANCELED) {
				bSetDestination = false;
				((MyLocationOverlayView) mLocationOverlay).setNullDestination();
				((MyLocationOverlayView) mLocationOverlay).setNullMyLocation();
				((MyLocationOverlayView) mLocationOverlay).invalidate();
				bSetModeManualPosition = false;
				bNowNavigating = false;
				DimageView.setEnabled(true);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapArray[0].recycle();
		mapArray[1].recycle();
		recycleImageView(DimageView);
		recycleImageView(mLocationOverlay);

	}

	private static void recycleImageView(ImageView iv) {
		Drawable d = iv.getDrawable();
		Bitmap b = ((BitmapDrawable) d).getBitmap();
		b.recycle();
		d.setCallback(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (bScanstate) {
			stopWIFIScan();
		}
		bValidPosition = false;

	}

	public void stopWIFIScan() {
		if (bScanstate) {
			bScanstate = false;
			// unregisterReceiver(mReceiver);
		}
	}

	public void startWIFIScan() {
		// init WIFISCAN
		if (!bScanstate) {
			bScanstate = true;
			scanHandler.postDelayed(scanRunnable, 1000);
			/*
			 * final IntentFilter filter = new IntentFilter(
			 * WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			 * filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
			 * registerReceiver(mReceiver, filter);
			 */
			wifimanager.startScan();
		}
	}

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
		((MyLocationOverlayView) mLocationOverlay).setzOrder(zOrder);
	}

	public void getScanResult() {
		mScanResult = wifimanager.getScanResults();

		Set<ApSet> fingerprint = new HashSet<ApSet>();
		for (ScanResult scan : mScanResult) {
			Accesspoint findap = null;
			for (Accesspoint ap : apList) {
				if (ap.getBssid().equals(scan.BSSID)) {
					findap = ap;
					break;
				}
			}
			if (findap == null)
				continue;
			ApSet temp = new ApSet(null, findap.getId(), scan.level);
			fingerprint.add(temp);
		}
		Point p = mLocalization.getPosition(fingerprint);

		fingerprint.clear();
		if (p == null) {
			((MyLocationOverlayView) mLocationOverlay).setNullMyLocation();
			Log.e("Source", "null");
			bValidPosition = false;
			if (bNowNavigating) {

			} else {
				if (bSetDestination && !bSetModeManualPosition) {
					stopWIFIScan();
					startActivityForResult(new Intent(MapActivity.this,
							SelectMyLocationType.class),
							StaticValue.REQUEST_MYLOCATIONTYPE);
				}
			}

		} else {
			bValidPosition = true;
			Source = p;
			((MyLocationOverlayView) mLocationOverlay).setMyLocation(Source);

			Log.e("Source", Source.toString());

			if (bNowNavigating) {
				List<Point> pathList = mLocalization
						.getShortestPath(Destination);
				if (pathList != null) {

					for (Point path : pathList)
						Log.e("path", path.toString());
					((MyLocationOverlayView) mLocationOverlay)
							.setPathList(pathList);
					DimageView.focusToPoint(Source);
				}
			} else {
				mLocationOverlay.invalidate();
				if (bSetDestination && !bSetModeManualPosition) {
					stopWIFIScan();
					startActivityPreviewWay();
				}
			}
		}
	}

	Handler moveHandler = new Handler() {
		public void handleMessage(Message msg) {
			bSetDestination = false;
			((MyLocationOverlayView) mLocationOverlay).setNullDestination();
			((MyLocationOverlayView) mLocationOverlay).setNullMyLocation();
			((MyLocationOverlayView) mLocationOverlay).invalidate();
			bSetModeManualPosition = false;
			bNowNavigating = false;
			DimageView.setEnabled(true);
			Toast.makeText(MapActivity.this, "안내종료", Toast.LENGTH_SHORT).show();
		};
	};
	Runnable r = new Runnable() {

		@Override
		public void run() {
			if (pathList != null) {
				for (Point path : pathList)
					Log.e("path", path.toString());
				((MyLocationOverlayView) mLocationOverlay)
						.setPathList(pathList);
				if(pathList.size()==1){
					moveHandler.sendEmptyMessage(1);
					return;
				}
				if (pathList.size() > 1) {
					Point cur = pathList.get(0);
					Point next = pathList.get(1);
					if(cur.getX().equals(next.getX())&&cur.getY().equals(next.getY())){
						pathList.remove(0);
						moveHandler.postDelayed(r, 1000);
						return;
					}
					Random rand = new Random();
					int move = rand.nextInt(40);
					move -=10;
					if (Math.abs(cur.getX() - next.getX()) > Math.abs(cur
							.getY() - next.getY())) {
						if (Math.abs(cur.getX() - next.getX()) < move)
							cur.setX(next.getX());
						else
							cur.setX(cur.getX() - move
									* (cur.getX() - next.getX())
									/ Math.abs(cur.getX() - next.getX()));
					} else {
						if (Math.abs(cur.getY() - next.getY()) < move)
							cur.setY(next.getY());
						else
							cur.setY(cur.getY() - move
									* (cur.getY() - next.getY())
									/ Math.abs(cur.getY() - next.getY()));
					}
					pathList.set(0, cur);
					Source = cur;
					mLocationOverlay.setMyLocation(Source);
					DimageView.focusToPoint(Source);
					moveHandler.postDelayed(r, 1000);
				}
			}
		}
	};

	public void moveSource() {
		r.run();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				getScanResult();
				wifimanager.startScan(); // for refresh
			} else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
				sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
			}

		}
	};

	public void startActivityPreviewWay() {
		Intent intent = new Intent(MapActivity.this, PreviewWayActivity.class);
		if (Source.getId() == null)
			intent.putExtra(StaticValue.KEY_SRC_ID, -1);
		else
			intent.putExtra(StaticValue.KEY_SRC_ID, Source.getId().intValue());
		intent.putExtra(StaticValue.KEY_SRC_X, Source.getX().doubleValue());
		intent.putExtra(StaticValue.KEY_SRC_Y, Source.getY().doubleValue());
		intent.putExtra(StaticValue.KEY_SRC_Z, Source.getZ().doubleValue());
		intent.putExtra(StaticValue.KEY_DST_ID, Destination.getId().intValue());
		intent.putExtra(StaticValue.KEY_DST_X, Destination.getX().doubleValue());
		intent.putExtra(StaticValue.KEY_DST_Y, Destination.getY().doubleValue());
		intent.putExtra(StaticValue.KEY_DST_Z, Destination.getZ().doubleValue());
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mapArray[0].compress(CompressFormat.PNG, 100, stream);
		byte[] img1 = stream.toByteArray();
		intent.putExtra("img1", img1);
		stream.reset();
		mapArray[1].compress(CompressFormat.PNG, 100, stream);
		byte[] img2 = stream.toByteArray();
		intent.putExtra("img2", img2);
		startActivityForResult(intent, StaticValue.REQUEST_PREVIEW_WAY);
	}

}
