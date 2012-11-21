package com.softwaremaestro.Indoornavigation.Util;

import java.util.ArrayList;
import java.util.List;

import com.softwaremaestro.Indoornavigation.R;

import kr.softwaremaestro.indoor.wrm.vo.Point;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class MyLocationOverlayView extends ImageView {

	Point myLocation;
	Point Destination;

	Point nullPoint;
	int zOrder;
	float scale;
	Bitmap source;
	public void setSource(Bitmap source) {
		this.source = source;
	}

	Bitmap destination;

	List<Point> pathList;

	public void setPathList(List<Point> pathList) {
		this.pathList = pathList;
	}

	public void setDestination(Point destination) {
		Destination = destination;
	}

	public void setScale(int[] info) {
		this.scale = (float) info[2] / (float) info[0];
	}

	public void setScale(float d) {
		this.scale = d;
	}

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
	}

	public void setMyLocation(Point myLocation) {
		this.myLocation = myLocation;
	}

	public MyLocationOverlayView(Context context) {
		this(context, null);
	}

	public MyLocationOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public MyLocationOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setNullMyLocation();
		setNullDestination();
		nullPoint = new Point(-1, 0.0, 0.0, 0.0);
		pathList = new ArrayList<Point>();
		source = BitmapFactory.decodeResource(getResources(), R.drawable.user);
		destination = BitmapFactory.decodeResource(getResources(), R.drawable.favorite);
		setBackgroundColor(Color.TRANSPARENT);
		setScaleType(ScaleType.MATRIX);
	}

	public void setNullDestination() {
		Destination = new Point(-1, 0.0, 0.0, 0.0);
	}

	public void setNullMyLocation() {
		myLocation = new Point(-1, 0.0, 0.0, 0.0);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
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
		if (pathList != null)
			if (pathList.size() > 1) {
				for (int i = 0; i < pathList.size() - 1; i++) {
					Point start = pathList.get(i);
					Point end = pathList.get(i + 1);
					if (start.getZ().intValue() != zOrder
							|| end.getZ().intValue() != zOrder)
						continue;
					paint.setColor(Color.GRAY);
					paint.setAlpha(0xff);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					paint.setStrokeWidth(10.0f);
					c.drawLine((float) (value[2] + start.getX() * value[0]
							* scale), (float) (value[5] + start.getY()
							* value[0] * scale), (float) (value[2] + end.getX()
							* value[0] * scale), (float) (value[5] + end.getY()
							* value[0] * scale), paint);
					paint.setColor(Color.GRAY);
					paint.setAlpha(0xff);
					paint.setStyle(Paint.Style.FILL);
					c.drawRect(
							(float) (value[2] + start.getX() * value[0] * scale - 5.0f),
							(float) (value[5] + start.getY() * value[0] * scale - 5.0f),
							(float) (value[2] + start.getX() * value[0] * scale + 5.0f),
							(float) (value[5] + start.getY() * value[0] * scale + 5.0f),
							paint);
				}
			}
		paint.setColor(Color.RED);
		paint.setAlpha(0xff);
		if (!myLocation.equals(nullPoint)
				&& zOrder == myLocation.getZ().intValue()){
			Rect r = new Rect((int) (value[2] + myLocation.getX() * value[0] * scale - StaticValue.SIZE_ICON),
					(int) (value[5] + myLocation.getY() * value[0] * scale - StaticValue.SIZE_ICON),
					(int) (value[2] + myLocation.getX() * value[0] * scale + StaticValue.SIZE_ICON),
					(int) (value[5] + myLocation.getY() * value[0] * scale + StaticValue.SIZE_ICON));
			c.drawBitmap(source, null, r, paint);
		}
		if (!Destination.equals(nullPoint)
				&& zOrder == Destination.getZ().intValue()){
			Rect r = new Rect((int) (value[2] + Destination.getX() * value[0] * scale - StaticValue.SIZE_ICON),
					(int) (value[5] + Destination.getY() * value[0] * scale - StaticValue.SIZE_ICON),
					(int) (value[2] + Destination.getX() * value[0] * scale + StaticValue.SIZE_ICON),
					(int) (value[5] + Destination.getY() * value[0] * scale + StaticValue.SIZE_ICON));
			c.drawBitmap(destination, null, r, paint);
		}
		
	}

}