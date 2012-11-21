package com.softwaremaestro.Indoornavigation.Activity;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;

import kr.softwaremaestro.indoor.engine.Localization;
import kr.softwaremaestro.indoor.engine.WiFiRadioMap;
import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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

import com.softwaremaestro.Indoornavigation.R;
import com.softwaremaestro.Indoornavigation.Util.DraggableImageView;
import com.softwaremaestro.Indoornavigation.Util.MyLocationOverlayView;
import com.softwaremaestro.Indoornavigation.Util.StaticValue;

public class PreviewWayActivity extends Activity {
	Intent intent;
	Localization mLocalization;
	WiFiRadioMap mWrm;
	Point Source;
	Point Destination;
	List<Point> pathList;
	FrameLayout map_frame;
	DraggableImageView DimageView;
	MyLocationOverlayView mLocationOverlay;
	int zOrder;
	Handler imageHandler;
	Bitmap[] bitmaparr;
	Button buttonOK;
	Button buttonCancle;
	Button buttonSpinner;
	Button buttonFloor1;
	Button buttonFloor2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);

		DimageView = (DraggableImageView) findViewById(R.id.activity_preview_draggableimageview);
		map_frame = (FrameLayout) findViewById(R.id.activity_preview_framelayout);

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

		buttonOK = (Button) findViewById(R.id.activity_preview_button_ok);
		buttonCancle = (Button) findViewById(R.id.activity_preview_button_cancle);
		buttonOK.setOnClickListener(l);
		buttonCancle.setOnClickListener(l);
		buttonSpinner = (Button) findViewById(R.id.activity_preview_button_spinner);
		buttonSpinner.setOnClickListener(l);
		buttonFloor1 = (Button) findViewById(R.id.activity_preview_button_floor1);
		buttonFloor1.setOnClickListener(l);
		buttonFloor2 = (Button) findViewById(R.id.activity_preview_button_floor2);
		buttonFloor2.setOnClickListener(l);

		intent = getIntent();

		byte[] img1 = intent.getByteArrayExtra("img1");
		byte[] img2 = intent.getByteArrayExtra("img2");

		bitmaparr = new Bitmap[2];
		bitmaparr[0] = BitmapFactory.decodeByteArray(img1, 0, img1.length);
		bitmaparr[1] = BitmapFactory.decodeByteArray(img2, 0, img2.length);

		int srcID = intent.getIntExtra(StaticValue.KEY_SRC_ID, -1);
		double srcX = intent.getDoubleExtra(StaticValue.KEY_SRC_X, -1);
		double srcY = intent.getDoubleExtra(StaticValue.KEY_SRC_Y, -1);
		double srcZ = intent.getDoubleExtra(StaticValue.KEY_SRC_Z, -1);

		DimageView.setHandler(imageHandler);
		changeFloor((int) srcZ);

		Source = new Point(srcID, srcX, srcY, srcZ);
		mLocationOverlay.setMyLocation(Source);

		int dstID = intent.getIntExtra(StaticValue.KEY_DST_ID, -1);
		double dstX = intent.getDoubleExtra(StaticValue.KEY_DST_X, -1);
		double dstY = intent.getDoubleExtra(StaticValue.KEY_DST_Y, -1);
		double dstZ = intent.getDoubleExtra(StaticValue.KEY_DST_Z, -1);

		Destination = new Point(dstID, dstX, dstY, dstZ);
		mLocationOverlay.setDestination(Destination);

		try {
			mWrm = WiFiRadioMap.getinstance(getApplicationContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mLocalization = new Localization(mWrm);

		pathList = mLocalization.getShortestPath(Source, Destination);
		mLocationOverlay.setPathList(pathList);


	}

	public void changeFloor(int floor) {
		if (zOrder != floor) {
			buttonFloor1.setTextColor(Color.WHITE);
			buttonFloor2.setTextColor(Color.WHITE);
			if(floor==1){
				buttonSpinner.setText(buttonFloor1.getText().toString());
				buttonFloor1.setTextColor(Color.YELLOW);
			}
			else{
				buttonSpinner.setText(buttonFloor2.getText().toString());
				buttonFloor2.setTextColor(Color.YELLOW);
			}
			setzOrder(floor);
			DimageView.setImageBitmap(bitmaparr[zOrder - 1]);
		}
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
			if (v == buttonOK) {
				setResult(RESULT_OK);
				finish();
			} else if (v == buttonCancle) {
				setResult(RESULT_CANCELED);
				finish();
			}
		}
	};

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
		DimageView.focusToPoint(midPoint(Source, Destination));
	}

	private Point midPoint(Point source, Point destination) {
		return new Point(-1,
				(double) ((source.getX().floatValue() + destination.getX()
						.floatValue()) / 2.0f),
				(double) ((source.getY().floatValue() + destination.getY()
						.floatValue()) / 2.0f), source.getZ());
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
					spinnerDropDownCancle();
					break;

				}
			}
		};
	}

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
		mLocationOverlay.setzOrder(zOrder);
	}
}
