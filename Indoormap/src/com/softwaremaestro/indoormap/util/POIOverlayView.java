package com.softwaremaestro.indoormap.util;

import java.sql.SQLException;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class POIOverlayView extends ImageView {

	ArrayList<PointOfInterest> PointOfInterestList;
	ArrayList<PointOfInterest> updatePointOfInterestList;
	int POIID;
	MappingService svc;
	int zOrder;

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
	}


	public POIOverlayView(Context context) {
		this(context, null);
	}

	public POIOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public POIOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		svc = new MappingService(PreferenceUtil.getAppPreferences((Activity)context, "IP"));
		POIID = 0;
		PointOfInterestList = new ArrayList<PointOfInterest>();
		updatePointOfInterestList = new ArrayList<PointOfInterest>();
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
		paint.setAlpha(50);
		for (PointOfInterest poi : PointOfInterestList) {
			if (poi.getZ().intValue() == zOrder) {
				if (updatePointOfInterestList.contains(poi))
					paint.setAlpha(250);
				c.drawCircle((float) (value[2] + poi.getX() * value[0]
						), (float) (value[5] + poi.getY()
						* value[0] ), StaticValue.POINT_RADIUS
						* value[0] , paint);
				paint.setAlpha(50);
			}
		}
	}

	public boolean isExistPoint(float x, float y) {
		for (PointOfInterest poi : PointOfInterestList)
			if (poi.getX().floatValue() == x && poi.getY().floatValue() == y)
				return true;
		return false;
	}

	public void removePoint(int id) {
		// TODO Auto-generated method stub
		PointOfInterest findpoi = null;
		for (PointOfInterest poi : PointOfInterestList)
			if (poi.getId().intValue() == id)
				findpoi = poi;
		if (findpoi != null) {
			try {
				//svc.update = true;
				svc.removePointOfInterestById(findpoi.getId());
				//svc.update = false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PointOfInterestList.remove(findpoi);
		}

		invalidate();
	}
	
	public void updateList(){
		updatePointOfInterestList.clear();
		for(PointOfInterest poi : PointOfInterestList)
			updatePointOfInterestList.add(poi);
		invalidate();
	}

	public boolean addPointOfInterest(float x, float y, float z, String name) {
		for (PointOfInterest poi : PointOfInterestList)
			if (poi.getX().floatValue() == x && poi.getY().floatValue() == y)
				return false;
		PointOfInterest poi = new PointOfInterest(++POIID, (double) x,
				(double) y, (double) z, name);
		try {
			//svc.update = true;
			svc.addPointOfInterest(poi);
			//svc.update = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PointOfInterestList.add(poi);
		return true;
	}

	public ArrayList<PointOfInterest> getPointOfInterestList() {
		return PointOfInterestList;
	}

	public void setPointOfInterestList(
			ArrayList<PointOfInterest> pointOfInterestList) {
		PointOfInterestList = pointOfInterestList;
		if (PointOfInterestList.size() > 0)
			POIID = PointOfInterestList.get(PointOfInterestList.size() - 1)
					.getId().intValue();
	}

	public int getPointID(float x, float y) {
		// TODO Auto-generated method stub
		for (PointOfInterest poi : PointOfInterestList)
			if (poi.getX().floatValue() == x && poi.getY().floatValue() == y)
				return poi.getId().intValue();
		return -1;
	}
}