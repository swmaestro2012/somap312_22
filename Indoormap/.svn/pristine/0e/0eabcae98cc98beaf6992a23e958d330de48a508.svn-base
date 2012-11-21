package com.softwaremaestro.indoormap.util;

import java.sql.SQLException;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.widget.ImageView;

public class WayPointOverlayView extends ImageView {

	ArrayList<Waypoint> WaypointList;
	ArrayList<WaypointLink> WaypointLinkList;
	ArrayList<Waypoint> updateWaypointList;
	ArrayList<WaypointLink> updateWaypointLinkList;
	int wayPointID;
	int wayLinkID;
	MappingService svc;
	int zOrder;

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
	}


	public WayPointOverlayView(Context context) {
		this(context, null);
	}

	public WayPointOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public WayPointOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		svc = new MappingService(PreferenceUtil.getAppPreferences((Activity)context, "IP"));
		wayPointID = 0;
		wayLinkID = 0;
		WaypointList = new ArrayList<Waypoint>();
		WaypointLinkList = new ArrayList<WaypointLink>();
		updateWaypointList = new ArrayList<Waypoint>();
		updateWaypointLinkList = new ArrayList<WaypointLink>();
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
		paint.setColor(Color.GREEN);
		paint.setAlpha(50);
		for (Waypoint wp : WaypointList) {
			if (wp.getZ().intValue() == zOrder) {
				if (updateWaypointList.contains(wp))
					paint.setAlpha(250);
				c.drawCircle((float) (value[2] + wp.getX() * value[0]
						), (float) (value[5] + wp.getY()
						* value[0] ), StaticValue.POINT_RADIUS
						* value[0] , paint);
				paint.setAlpha(50);
			}
		}
		paint.setColor(Color.GREEN);
		paint.setAlpha(50);
		for (WaypointLink wl : WaypointLinkList) {
			Waypoint start = null, end = null;
			boolean searchFlag = false;
			for (Waypoint wp : WaypointList) {
				if (searchFlag)
					break;
				if (wp.getZ().intValue() != zOrder)
					continue;
				if (wp.getId().equals(wl.getEndPointId()))
					end = wp;
				if (wp.getId().equals(wl.getStartPointId()))
					start = wp;
				if (start != null && end != null)
					searchFlag = true;
			}
			if (searchFlag) {
				if (updateWaypointLinkList.contains(wl))
					paint.setAlpha(250);
				c.drawLine(
						(float) (value[2] + start.getX() * value[0]
								),
						(float) (value[5] + start.getY() * value[0]
								),
						(float) (value[2] + end.getX() * value[0] ),
						(float) (value[5] + end.getY() * value[0] ),
						paint);
				paint.setAlpha(50);
			}
		}
	}

	public boolean addWayPoint(float x, float y, float z) {
		for (Waypoint wp : WaypointList)
			if (wp.getX().floatValue() == x && wp.getY().floatValue() == y)
				return false;
		Waypoint wp = new Waypoint(++wayPointID, (double) x, (double) y,
				(double) z);
		try {
			//svc.update = true;
			svc.addWaypoint(wp);
			//svc.update = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WaypointList.add(wp);
		return true;
	}
	
	public void updateList(){
		updateWaypointLinkList.clear();
		updateWaypointList.clear();
		for(Waypoint wp : WaypointList)
			updateWaypointList.add(wp);
		for(WaypointLink wl : WaypointLinkList)
			updateWaypointLinkList.add(wl);
		invalidate();
	}

	public boolean isExistPoint(float x, float y) {
		for (Waypoint wp : WaypointList)
			if (wp.getX().floatValue() == x && wp.getY().floatValue() == y)
				return true;
		return false;
	}

	public void removePoint(int id) {
		// TODO Auto-generated method stub
		Waypoint findwp = null;
		for (Waypoint wp : WaypointList)
			if (wp.getId().intValue() == id)
				findwp = wp;
		if (findwp != null) {
			for (int i = 0; i < WaypointLinkList.size();) {
				WaypointLink link = WaypointLinkList.get(i);
				if (link.getEndPointId().intValue() == id
						|| link.getStartPointId().intValue() == id)
					removeLink(link);
				else
					i++;
			}
			try {
				//svc.update = true;
				svc.removeWaypointById(findwp.getId());
				//svc.update = false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WaypointList.remove(findwp);
		}
		invalidate();
	}

	private void removeLink(WaypointLink link) {
		// TODO Auto-generated method stub
		try {
			//svc.update = true;
			svc.removeWaypointLinkById(link.getId());
			//svc.update = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WaypointLinkList.remove(link);
	}

	public boolean addWayLink(float sx, float sy, float ex, float ey) {
		Waypoint start = null;
		Waypoint end = null;
		for (Waypoint wp : WaypointList) {
			if (wp.getX().floatValue() == sx && wp.getY().floatValue() == sy)
				start = wp;
			if (wp.getX().floatValue() == ex && wp.getY().floatValue() == ey)
				end = wp;
		}
		if (start != null && end != null) {
			WaypointLink wpl = new WaypointLink(++wayLinkID, start.getId(),
					end.getId());
			try {
				//svc.update = true;
				svc.addWaypointLink(wpl);
				//svc.update = false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			WaypointLinkList.add(wpl);
		}
		invalidate();
		return true;
	}

	public ArrayList<Waypoint> getWaypointList() {
		return WaypointList;
	}

	public void setWaypointList(ArrayList<Waypoint> waypointList) {
		WaypointList = waypointList;
		if (WaypointList.size() > 0)
			wayPointID = WaypointList.get(WaypointList.size() - 1).getId()
					.intValue();
	}

	public ArrayList<WaypointLink> getWaypointLinkList() {
		return WaypointLinkList;
	}

	public void setWaypointLinkList(ArrayList<WaypointLink> waypointLinkList) {
		WaypointLinkList = waypointLinkList;
		if (WaypointLinkList.size() > 0)
			wayLinkID = WaypointLinkList.get(WaypointLinkList.size() - 1)
					.getId().intValue();
	}

	public int getPointID(float x, float y) {
		// TODO Auto-generated method stub
		for (Waypoint wp : WaypointList)
			if (wp.getX().floatValue() == x && wp.getY().floatValue() == y)
				return wp.getId().intValue();
		return -1;
	}

}