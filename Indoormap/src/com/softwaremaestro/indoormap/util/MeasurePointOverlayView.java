package com.softwaremaestro.indoormap.util;

import java.sql.SQLException;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.widget.ImageView;

public class MeasurePointOverlayView extends ImageView {

	ArrayList<BaseMeasurepoint> BaseMeasurepointList;
	ArrayList<Measurepoint> MeasurepointList;
	ArrayList<MeasurepointLink> MeasurepointLinkList;
	ArrayList<BaseMeasurepoint> updateBaseMeasurepointList;
	ArrayList<Measurepoint> updateMeasurepointList;
	ArrayList<MeasurepointLink> updateMeasurepointLinkList;
	private int baseMeasurePointId;
	private int measurePointId;
	private int measureLinkId;
	MappingService svc;
	int zOrder;

	public void setzOrder(int zOrder) {
		this.zOrder = zOrder;
	}

	public MeasurePointOverlayView(Context context) {
		this(context, null);
	}

	public MeasurePointOverlayView(Context context, int height, int width) {
		this(context, null);
	}

	public MeasurePointOverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		svc = new MappingService(PreferenceUtil.getAppPreferences((Activity)context, "IP"));
		baseMeasurePointId = 0;
		measurePointId = 0;
		measureLinkId = 0;
		BaseMeasurepointList = new ArrayList<BaseMeasurepoint>();
		MeasurepointList = new ArrayList<Measurepoint>();
		MeasurepointLinkList = new ArrayList<MeasurepointLink>();
		updateBaseMeasurepointList = new ArrayList<BaseMeasurepoint>();
		updateMeasurepointList = new ArrayList<Measurepoint>();
		updateMeasurepointLinkList = new ArrayList<MeasurepointLink>();
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
		paint.setColor(Color.BLUE);
		paint.setAlpha(50);
		for (BaseMeasurepoint bmp : BaseMeasurepointList) {
			if (bmp.getZ().intValue() == zOrder) {
				if (updateBaseMeasurepointList.contains(bmp))
					paint.setAlpha(250);
				c.drawCircle((float) (value[2] + bmp.getX() * value[0]
						), (float) (value[5] + bmp.getY()
						* value[0] ), StaticValue.POINT_RADIUS
						* value[0] , paint);
				paint.setAlpha(50);
			}
		}
		paint.setColor(Color.BLUE);
		paint.setAlpha(50);
		for (MeasurepointLink ml : MeasurepointLinkList) {
			BaseMeasurepoint start = null, end = null;
			boolean searchFlag = false;
			for (BaseMeasurepoint bmp : BaseMeasurepointList) {
				if (searchFlag)
					break;
				if (bmp.getZ().intValue() != zOrder)
					continue;
				if (bmp.getId().equals(ml.getEndPointId()))
					end = bmp;
				if (bmp.getId().equals(ml.getStartPointId()))
					start = bmp;
				if (start != null && end != null)
					searchFlag = true;
			}
			if (searchFlag) {
				if (updateMeasurepointLinkList.contains(ml))
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
		paint.setColor(Color.CYAN);
		paint.setAlpha(50);
		for (Measurepoint mp : MeasurepointList) {
			if (mp.getZ().intValue() == zOrder) {
				if (updateMeasurepointList.contains(mp))
					paint.setAlpha(250);
				c.drawCircle((float) (value[2] + mp.getX() * value[0]
						), (float) (value[5] + mp.getY()
						* value[0] ),
						(StaticValue.POINT_RADIUS * 0.7f) * value[0]
								, paint);
				paint.setAlpha(50);
			}
		}

	}

	public boolean isExistPoint(float x, float y,float z) {

		for (BaseMeasurepoint bmp : BaseMeasurepointList)
			if (bmp.getX().floatValue() == x && bmp.getY().floatValue() == y&& bmp.getZ().floatValue() == z)
				return true;
		for (Measurepoint mp : MeasurepointList) {
			if(mp.getZ().floatValue()!=z)
				continue;
			float[] value = new float[9];
			getImageMatrix().getValues(value);
			float distx = x - mp.getX().floatValue();
			float disty = y - mp.getY().floatValue();
			float dist = FloatMath.sqrt(distx * distx + disty * disty);
			if (dist < StaticValue.POINT_RADIUS * 0.7f * value[0] )
				return true;
		}

		return false;
	}

	public MeasurePointType getPointID(float x, float y) {
		// TODO Auto-generated method stub
		for (BaseMeasurepoint bmp : BaseMeasurepointList)
			if (bmp.getX().floatValue() == x && bmp.getY().floatValue() == y)
				return new MeasurePointType(1, bmp.getId().intValue());
		for (Measurepoint mp : MeasurepointList) {
			float[] value = new float[9];
			getImageMatrix().getValues(value);
			float distx = x - mp.getX().floatValue();
			float disty = y - mp.getY().floatValue();
			float dist = FloatMath.sqrt(distx * distx + disty * disty);
			if (dist < StaticValue.POINT_RADIUS * 0.7f * value[0] )
				return new MeasurePointType(2, mp.getId().intValue());
		}
		return null;
	}
	
	public BaseMeasurepoint getBaseMeasurePoint(int id){
		for(BaseMeasurepoint bmp : BaseMeasurepointList)
			if(bmp.getId().intValue()==id)
				return bmp;
		return null;
	}
	
	public Measurepoint getMeasurePoint(int id){
		for(Measurepoint mp : MeasurepointList)
			if(mp.getId().intValue()==id)
				return mp;
		return null;
	}

	public boolean addMeasureLink(float sx, float sy, float ex, float ey,
			float d, float z) {
		BaseMeasurepoint start = null;
		BaseMeasurepoint end = null;
		for (BaseMeasurepoint bmp : BaseMeasurepointList) {
			if (bmp.getX().floatValue() == sx && bmp.getY().floatValue() == sy)
				start = bmp;
			if (bmp.getX().floatValue() == ex && bmp.getY().floatValue() == ey)
				end = bmp;
		}
		if (start != null && end != null) {
			MeasurepointLink mpl = new MeasurepointLink(measureLinkId + 1,
					start.getId(), end.getId());
			try {
				//svc.update = true;
				svc.addMeasurepointLink(mpl);
				//svc.update = false;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MeasurepointLinkList.add(mpl);
			float x = sx - ex;
			float y = sy - ey;
			float dist = FloatMath.sqrt(x * x + y * y);
			float measurepointcount = (float) (dist / d);
			float dx = x / (measurepointcount);
			float dy = y / (measurepointcount);
			for (int i = 1; i < measurepointcount; i++) {
				Measurepoint mp = new Measurepoint(++measurePointId,
						(double) (sx - dx * i), (double) (sy - dy * i),
						(double) z, measureLinkId + 1);
				try {
					//svc.update = true;
					svc.addMeasurepoint(mp);
					//svc.update = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MeasurepointList.add(mp);
			}
			measureLinkId++;
		}
		invalidate();
		return true;
	}

	public boolean addBaseMeasurePoint(float x, float y, float z) {
		for (BaseMeasurepoint bmp : BaseMeasurepointList)
			if (bmp.getX().floatValue() == x && bmp.getY().floatValue() == y)
				return false;
		BaseMeasurepoint bmp = new BaseMeasurepoint(++baseMeasurePointId,
				(double) x, (double) y, (double) z);
		try {
			//svc.update = true;
			svc.addBaseMeasurepoint(bmp);
			//svc.update = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BaseMeasurepointList.add(bmp);
		invalidate();
		return true;
	}

	public void removePoint(int id, int measuretype) {
		// TODO Auto-generated method stub
		BaseMeasurepoint findbmp = null;
		Measurepoint findmp = null;
		if (measuretype == 2) {
			for (Measurepoint mp : MeasurepointList)
				if (mp.getId().intValue() == id)
					findmp = mp;
			if (findmp != null) {
				try {
					//svc.update = true;
					svc.removeMeasurepointById(findmp.getId());
					//svc.update = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MeasurepointList.remove(findmp);
			}
		} else {
			for (BaseMeasurepoint bmp : BaseMeasurepointList)
				if (bmp.getId().intValue() == id)
					findbmp = bmp;
			if (findbmp != null) {
				for (int i = 0; i < MeasurepointLinkList.size();) {
					MeasurepointLink link = MeasurepointLinkList.get(i);
					if (link.getEndPointId().intValue() == id
							|| link.getStartPointId().intValue() == id)
						removeLink(link);
					else
						i++;
				}
				try {
					//svc.update = true;
					svc.removeBaseMeasurepointById(findbmp.getId());
					//svc.update = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BaseMeasurepointList.remove(findbmp);
			}
		}

		invalidate();
	}

	private void removeLink(MeasurepointLink link) {
		// TODO Auto-generated method stub
		for (int i = 0; i < MeasurepointList.size();) {
			Measurepoint point = MeasurepointList.get(i);
			if (point.getMpLinkId().intValue() == link.getId().intValue()) {
				try {
					//svc.update = true;
					svc.removeMeasurepointById(point.getId());
					//svc.update = false;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				MeasurepointList.remove(point);
			} else
				i++;
		}
		try {
			//svc.update = true;
			svc.removeMeasurepointLinkById(link.getId());
			//svc.update = false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MeasurepointLinkList.remove(link);
	}

	public ArrayList<BaseMeasurepoint> getBaseMeasurepointList() {
		return BaseMeasurepointList;
	}

	public void setBaseMeasurepointList(
			ArrayList<BaseMeasurepoint> baseMeasurepointList) {
		BaseMeasurepointList = baseMeasurepointList;
		if (BaseMeasurepointList.size() > 0)
			baseMeasurePointId = BaseMeasurepointList
					.get(BaseMeasurepointList.size() - 1).getId().intValue();
	}

	public ArrayList<Measurepoint> getMeasurepointList() {
		return MeasurepointList;
	}

	public void setMeasurepointList(ArrayList<Measurepoint> measurepointList) {
		MeasurepointList = measurepointList;
		if (MeasurepointList.size() > 0)
			measurePointId = MeasurepointList.get(MeasurepointList.size() - 1)
					.getId().intValue();
	}

	public ArrayList<MeasurepointLink> getMeasurepointLinkList() {
		return MeasurepointLinkList;
	}

	public void setMeasurepointLinkList(
			ArrayList<MeasurepointLink> measurepointLinkList) {
		MeasurepointLinkList = measurepointLinkList;
		if (MeasurepointLinkList.size() > 0)
			measureLinkId = MeasurepointLinkList
					.get(MeasurepointLinkList.size() - 1).getId().intValue();
	}
	
	public void updateList(){
		updateBaseMeasurepointList.clear();
		updateMeasurepointLinkList.clear();
		updateMeasurepointList.clear();
		for(BaseMeasurepoint bmp : BaseMeasurepointList)
			updateBaseMeasurepointList.add(bmp);
		for(Measurepoint mp : MeasurepointList)
			updateMeasurepointList.add(mp);
		for(MeasurepointLink ml : MeasurepointLinkList)
			updateMeasurepointLinkList.add(ml);
		invalidate();
	}

	public int getBaseMeasurePointId() {
		return baseMeasurePointId;
	}

	public void setBaseMeasurePointId(int baseMeasurePointId) {
		this.baseMeasurePointId = baseMeasurePointId;
	}

	public int getMeasurePointId() {
		return measurePointId;
	}

	public void setMeasurePointId(int measurePointId) {
		this.measurePointId = measurePointId;
	}

	public int getMeasureLinkId() {
		return measureLinkId;
	}

	public void setMeasureLinkId(int measureLinkId) {
		this.measureLinkId = measureLinkId;
	}

	public class MeasurePointType {
		int type;

		public int getType() {
			return type;
		}

		int id;

		public int getId() {
			return id;
		}

		public MeasurePointType(int t, int _id) {
			// TODO Auto-generated constructor stub
			type = t;
			id = _id;
		}
	}

}
