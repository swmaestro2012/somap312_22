package com.softwaremaestro.indoormap.util;

import java.sql.SQLException;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class updateOverlayView extends ImageView {

	ArrayList<Fingerprint> fingerPrintList;

	public updateOverlayView(Context context) {
		this(context, null);
	}

	public updateOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public updateOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		fingerPrintList = new ArrayList<Fingerprint>();
		setBackgroundColor(Color.TRANSPARENT);
		setScaleType(ScaleType.MATRIX);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	public void onDraw(Canvas c) {
		super.onDraw(c);
		float[] value = new float[9];
		getImageMatrix().getValues(value);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setAlpha(100);
		Log.e("test",""+fingerPrintList.size());
		for (Fingerprint fp : fingerPrintList) {
			c.drawCircle((float) (value[2] + fp.getX() * value[0]),
					(float) (value[5] + fp.getY() * value[0]),
					StaticValue.POINT_RADIUS * 0.7f * value[0], paint);

		}
	}

	public ArrayList<Fingerprint> getFingerPrintList() {
		return fingerPrintList;
	}

	public void setFingerPrintList(ArrayList<Fingerprint> fingerPrintList) {
		this.fingerPrintList = fingerPrintList;
	}

}
