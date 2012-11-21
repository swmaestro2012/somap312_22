package com.softwaremaestro.indoormap.acivity;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.Accesspoint;
import kr.softwaremaestro.indoor.wrm.vo.ApSet;
import kr.softwaremaestro.indoor.wrm.vo.BaseMeasurepoint;
import kr.softwaremaestro.indoor.wrm.vo.Fingerprint;
import kr.softwaremaestro.indoor.wrm.vo.Measurepoint;
import kr.softwaremaestro.indoor.wrm.vo.MeasurepointLink;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;
import kr.softwaremaestro.indoor.wrm.vo.Waypoint;
import kr.softwaremaestro.indoor.wrm.vo.WaypointLink;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.softwaremaestro.indoormap.R;
import com.softwaremaestro.indoormap.util.DraggableImageView;
import com.softwaremaestro.indoormap.util.GridOverlayView;
import com.softwaremaestro.indoormap.util.MeasurePointOverlayView;
import com.softwaremaestro.indoormap.util.PreferenceUtil;
import com.softwaremaestro.indoormap.util.MeasurePointOverlayView.MeasurePointType;
import com.softwaremaestro.indoormap.util.POIOverlayView;
import com.softwaremaestro.indoormap.util.StaticValue;
import com.softwaremaestro.indoormap.util.WayPointOverlayView;

public class workMappingActivity extends Activity {

	SlidingDrawer drawer;
	LinearLayout menulayer;
	DraggableImageView DimageView;
	Button[] arr_button_menu;
	ImageView[] overlay;
	CheckBox measureCheckBox;
	CheckBox poiCheckBox;
	CheckBox wayCheckBox;
	Button UpdateButton;
	FrameLayout map_frame;
	Handler handler;
	Handler inputhandler;
	ArrayList<ScanResult> mScanResult;
	final int INDEX_OF_MENU_MP = 0;
	final int INDEX_OF_MENU_BMP = 1;
	final int INDEX_OF_MENU_ML = 2;
	final int INDEX_OF_MENU_POI = 3;
	final int INDEX_OF_MENU_WPS = 4;
	final int INDEX_OF_MENU_WP = 5;
	final int INDEX_OF_MENU_WL = 6;
	final int INDEX_OF_MENU_BACK = 7;
	private boolean bDrawBmp;
	private boolean bDrawMl;
	private boolean bDrawPOI;
	private boolean bDrawWp;
	private boolean bDrawWl;
	private boolean bDrawLink;
	private byte[] buildingImg;
	private Double z;
	private Double scale;
	String POIName;
	String MeasureDist;
	xy savedPoint;
	xy curPoint;
	MappingService svc;

	final String[] arr_str_menu = { "MeasureP", "B-MeasureP", "MeasureLink",
			"P-of-Interest", "WayPs", "WayP", "WayLink", "BACK" };
	int width;
	int height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_work_mapping_activity);
		//
		Intent recieve = getIntent();
		buildingImg = recieve.getByteArrayExtra("img");
		z = recieve.getDoubleExtra("z", 0);
		scale = recieve.getDoubleExtra("scale", 0.05);
		savedPoint = new xy();
		curPoint = new xy();
		//
		getWindowSize();
		drawer = (SlidingDrawer) findViewById(R.id.activity_work_mapping_drawer);
		menulayer = (LinearLayout) findViewById(R.id.activity_work_mapping_draggable_containmenubuttonlayout);
		map_frame = (FrameLayout) findViewById(R.id.activity_work_mapping_draggable_frame);
		DimageView = (DraggableImageView) findViewById(R.id.activity_work_mapping_draggable_imageview);
		//
		BitmapFactory.Options option = new BitmapFactory.Options();
		option.inScaled = false;
		//

		Bitmap backgroudImage = getMapBitmap(option);
		createOverlayView(backgroudImage.getWidth(),
				backgroudImage.getHeight(), backgroudImage);
		setImageHandler();
		DimageView.setHandler(handler);
		DimageView.setDmetric(getDispaymetric());
		DimageView.setImageBitmap(backgroudImage);
		arr_button_menu = new Button[8];

		measureCheckBox = (CheckBox) findViewById(R.id.activity_work_mapping_drawer_check_measure);
		measureCheckBox.setOnCheckedChangeListener(checkboxListener);
		measureCheckBox.setTag(1);
		measureCheckBox.setChecked(true);
		poiCheckBox = (CheckBox) findViewById(R.id.activity_work_mapping_drawer_check_poi);
		poiCheckBox.setOnCheckedChangeListener(checkboxListener);
		poiCheckBox.setTag(2);
		poiCheckBox.setChecked(true);
		wayCheckBox = (CheckBox) findViewById(R.id.activity_work_mapping_drawer_check_way);
		wayCheckBox.setOnCheckedChangeListener(checkboxListener);
		wayCheckBox.setTag(3);
		wayCheckBox.setChecked(true);
		UpdateButton = (Button) findViewById(R.id.activity_work_mapping_drawer_button_update);
		UpdateButton.setOnClickListener(l);

		createPOIInputDialog();
		createMeasureDialog();
		setInputHandler();

		init();

	}

	public Builder createPOIInputDialog() {
		Builder POIInputDialog;
		final EditText input = new EditText(workMappingActivity.this);
		input.setText("");
		POIInputDialog = new AlertDialog.Builder(workMappingActivity.this);
		POIInputDialog.setView(input);
		POIInputDialog.setTitle("POI 생성");
		POIInputDialog.setMessage("생성될 POI의 이름을 입력하세요");
		POIInputDialog.setPositiveButton("확인",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						POIName = input.getText().toString();
						inputhandler.sendEmptyMessage(StaticValue.MSG_ADD_POI);
					}
				});

		return POIInputDialog;

	}

	public Builder createMeasureDialog() {
		Builder MeasureDialog;
		final EditText input = new EditText(workMappingActivity.this);
		input.setText("");
		MeasureDialog = new AlertDialog.Builder(workMappingActivity.this);
		MeasureDialog.setView(input);
		MeasureDialog.setTitle("Measure 포인트 생성");
		MeasureDialog.setMessage("Measure 포인트의 간격을 입력하세요 (실제 m)");
		MeasureDialog.setPositiveButton("확인",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						MeasureDist = input.getText().toString();
						inputhandler
								.sendEmptyMessage(StaticValue.MSG_ADD_MEASURELINK);
					}
				});

		return MeasureDialog;
	}

	public void init() {
		drawInit();
		svc = new MappingService(PreferenceUtil.getAppPreferences(this, "IP"));

		setZorder();
		//
		ArrayList<BaseMeasurepoint> bmpList = new ArrayList<BaseMeasurepoint>();
		ArrayList<Measurepoint> mpList = new ArrayList<Measurepoint>();
		ArrayList<MeasurepointLink> mpLinkList = new ArrayList<MeasurepointLink>();
		ArrayList<Waypoint> wpList = new ArrayList<Waypoint>();
		ArrayList<WaypointLink> wpLinkList = new ArrayList<WaypointLink>();
		ArrayList<PointOfInterest> poiList = new ArrayList<PointOfInterest>();
		try {
			BaseMeasurepoint[] bmp = svc.getAllBaseMeasurepoints();
			Measurepoint[] mp = svc.getAllMeasurepoints();
			MeasurepointLink[] mpl = svc.getAllMeasurepointLinks();
			Waypoint[] wp = svc.getAllWaypoints();
			WaypointLink[] wpl = svc.getAllWaypointLinks();
			PointOfInterest[] poi = svc.getAllPointOfInterests();
			if (poi != null) {
				for (PointOfInterest temp : poi) {
					poiList.add(temp);
				}
				((POIOverlayView) overlay[2]).setPointOfInterestList(poiList);
			}
			if (wpl != null) {
				for (WaypointLink temp : wpl) {
					wpLinkList.add(temp);
				}
				((WayPointOverlayView) overlay[3])
						.setWaypointLinkList(wpLinkList);
			}
			if (wp != null) {
				for (Waypoint temp : wp) {
					wpList.add(temp);
				}
				((WayPointOverlayView) overlay[3]).setWaypointList(wpList);
			}
			if (mpl != null) {
				for (MeasurepointLink temp : mpl) {
					mpLinkList.add(temp);
				}
				((MeasurePointOverlayView) overlay[1])
						.setMeasurepointLinkList(mpLinkList);
			}
			if (mp != null) {
				for (Measurepoint temp : mp) {
					mpList.add(temp);
				}
				((MeasurePointOverlayView) overlay[1])
						.setMeasurepointList(mpList);
			}
			if (bmp != null) {
				for (BaseMeasurepoint temp : bmp) {
					bmpList.add(temp);
				}
				((MeasurePointOverlayView) overlay[1])
						.setBaseMeasurepointList(bmpList);
			}
			updateList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//

		menu_botton_init();
	}

	public void setZorder() {
		((MeasurePointOverlayView) overlay[1]).setzOrder(z.intValue());
		((POIOverlayView) overlay[2]).setzOrder(z.intValue());
		((WayPointOverlayView) overlay[3]).setzOrder(z.intValue());
	}

	public void drawInit() {
		bDrawBmp = false;
		bDrawMl = false;
		bDrawPOI = false;
		bDrawWp = false;
		bDrawWl = false;
		bDrawLink = false;
	}

	public void setImageHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case StaticValue.MSG_SYNC_IMAGEVIEW:
					Matrix m = DimageView.getImageMatrix();
					for (int i = 0; i < 4; i++) {
						overlay[i].setImageMatrix(m);
						overlay[i].invalidate();
					}
					DimageView.invalidate();
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

	public void setInputHandler() {
		inputhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case StaticValue.MSG_ADD_MEASURELINK:
					((MeasurePointOverlayView) overlay[1]).addMeasureLink(
							savedPoint.x, savedPoint.y, curPoint.x, curPoint.y,
							Float.parseFloat(MeasureDist) / scale.floatValue(),
							z.floatValue());
					break;
				case StaticValue.MSG_ADD_POI:
					((POIOverlayView) overlay[2]).addPointOfInterest(
							curPoint.x, curPoint.y, z.floatValue(), POIName);
					break;
				}
			}
		};
	}

	public DisplayMetrics getDispaymetric() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		return metrics;
	}

	public Bitmap getMapBitmap(BitmapFactory.Options option) {
		return BitmapFactory.decodeByteArray(buildingImg, 0,
				buildingImg.length, option);
	}

	public void createOverlayView(int w, int h, Bitmap backgroudImage) {
		overlay = new ImageView[4];
		Bitmap bit = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
		overlay[0] = new GridOverlayView(this);
		overlay[1] = new MeasurePointOverlayView(this);
		overlay[2] = new POIOverlayView(this);
		overlay[3] = new WayPointOverlayView(this);
		for (int i = 0; i < 4; i++) {
			overlay[i].setLayoutParams(DimageView.getLayoutParams());
			overlay[i].setBackgroundColor(Color.TRANSPARENT);
			overlay[i].setScaleType(ScaleType.MATRIX);
			overlay[i].setImageBitmap(bit);
			map_frame.addView(overlay[i]);

		}
	}

	public void getWindowSize() {
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(getApplicationContext().WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
	}

	private void menu_botton_init() {
		for (int i = 0; i < arr_button_menu.length; i++) {
			arr_button_menu[i] = new Button(this);
			arr_button_menu[i].setPadding(10, 10, 10, 10);
			arr_button_menu[i].setWidth((width - 40) / 3);
			arr_button_menu[i].setText(arr_str_menu[i]);
			arr_button_menu[i].setOnClickListener(l);
		}
		menu_change(INDEX_OF_MENU_MP, INDEX_OF_MENU_POI, INDEX_OF_MENU_WPS);
	}

	public void menu_change(int m1, int m2, int m3) {
		menulayer.removeAllViews();
		menulayer.addView(arr_button_menu[m1]);
		menulayer.addView(arr_button_menu[m2]);
		menulayer.addView(arr_button_menu[m3]);
	}

	private void callbackClickView(float x, float y) {

		curPoint.x = (int) x / StaticValue.GRID_SIZE * StaticValue.GRID_SIZE
				+ StaticValue.GRID_SIZE / 2;
		curPoint.y = (int) y / StaticValue.GRID_SIZE * StaticValue.GRID_SIZE
				+ StaticValue.GRID_SIZE / 2;

		if (bDrawBmp) {
			boolean res = ((MeasurePointOverlayView) overlay[1])
					.addBaseMeasurePoint(curPoint.x, curPoint.y, z.floatValue());
			if (res) {
				drawInit();
				menu_change(INDEX_OF_MENU_MP, INDEX_OF_MENU_POI,
						INDEX_OF_MENU_WPS);
			} else
				Toast.makeText(workMappingActivity.this,
						"Click Another Point - Already Exist",
						Toast.LENGTH_SHORT).show();

		} else if (bDrawMl) {
			if (((MeasurePointOverlayView) overlay[1]).isExistPoint(curPoint.x,
					curPoint.y,z.floatValue())) {
				if (bDrawLink) {
					drawInit();
					createMeasureDialog().show();
				} else {
					bDrawLink = true;
					savedPoint.x = curPoint.x;
					savedPoint.y = curPoint.y;
					Toast.makeText(workMappingActivity.this,
							"Click BaseMeasurePoint(end)", Toast.LENGTH_SHORT)
							.show();
				}
			} else
				Toast.makeText(workMappingActivity.this,
						"Click BaseMeasurePoint(start)", Toast.LENGTH_SHORT)
						.show();

		} else if (bDrawPOI) {
			boolean res = ((POIOverlayView) overlay[2]).isExistPoint(
					curPoint.x, curPoint.y);
			if (!res) {
				drawInit();
				createPOIInputDialog().show();
				menu_change(INDEX_OF_MENU_MP, INDEX_OF_MENU_POI,
						INDEX_OF_MENU_WPS);
			} else
				Toast.makeText(workMappingActivity.this,
						"Click Another Point - Already Exist",
						Toast.LENGTH_SHORT).show();
		} else if (bDrawWp) {
			boolean res = ((WayPointOverlayView) overlay[3]).addWayPoint(
					curPoint.x, curPoint.y, z.floatValue());
			if (res) {
				drawInit();
				menu_change(INDEX_OF_MENU_MP, INDEX_OF_MENU_POI,
						INDEX_OF_MENU_WPS);
			} else
				Toast.makeText(workMappingActivity.this,
						"Click Another Point - Already Exist",
						Toast.LENGTH_SHORT).show();
		} else if (bDrawWl) {
			if (((WayPointOverlayView) overlay[3]).isExistPoint(curPoint.x,
					curPoint.y)) {
				if (bDrawLink) {
					drawInit();
					((WayPointOverlayView) overlay[3]).addWayLink(savedPoint.x,
							savedPoint.y, curPoint.x, curPoint.y);

				} else {
					bDrawLink = true;
					savedPoint.x = curPoint.x;
					savedPoint.y = curPoint.y;
					Toast.makeText(workMappingActivity.this,
							"Click WayPoint(end)", Toast.LENGTH_SHORT).show();
				}
			} else
				Toast.makeText(workMappingActivity.this,
						"Click WayPoint(start)", Toast.LENGTH_SHORT).show();
		} else {
			if (measureCheckBox.isChecked()
					&& ((MeasurePointOverlayView) overlay[1]).isExistPoint(
							curPoint.x, curPoint.y,z.floatValue())) {
				MeasurePointType id = ((MeasurePointOverlayView) overlay[1])
						.getPointID(curPoint.x, curPoint.y);
				if (id != null) {
					Intent intent = new Intent(workMappingActivity.this,
							editDialog.class);
					intent.putExtra("id", id.getId());
					intent.putExtra("type", StaticValue.TYPE_MEASURE);
					intent.putExtra("point", id.getType());
					startActivityForResult(intent,
							StaticValue.REQUEST_POINT_EDIT);
				}
			} else if (poiCheckBox.isChecked()
					&& ((POIOverlayView) overlay[2]).isExistPoint(curPoint.x,
							curPoint.y)) {
				int id = ((POIOverlayView) overlay[2]).getPointID(curPoint.x,
						curPoint.y);
				if (id != -1) {
					Intent intent = new Intent(workMappingActivity.this,
							editDialog.class);
					intent.putExtra("id", id);
					intent.putExtra("type", StaticValue.TYPE_POI);
					startActivityForResult(intent,
							StaticValue.REQUEST_POINT_EDIT);
				}
			} else if (wayCheckBox.isChecked()
					&& ((WayPointOverlayView) overlay[3]).isExistPoint(
							curPoint.x, curPoint.y)) {
				int id = ((WayPointOverlayView) overlay[3]).getPointID(
						curPoint.x, curPoint.y);
				if (id != -1) {
					Intent intent = new Intent(workMappingActivity.this,
							editDialog.class);
					intent.putExtra("id", id);
					intent.putExtra("type", StaticValue.TYPE_WAY);
					startActivityForResult(intent,
							StaticValue.REQUEST_POINT_EDIT);
				}
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == StaticValue.REQUEST_POINT_EDIT) {
				int id = intent.getIntExtra("id", -1);
				int type = intent.getIntExtra("type", -1);
				int command = intent.getIntExtra("command", -1);
				switch (type) {
				case StaticValue.TYPE_MEASURE:
					if (command == StaticValue.COMMAND_DELETE) {
						((MeasurePointOverlayView) overlay[1]).removePoint(id,
								intent.getIntExtra("point", 1));
					}
					break;
				case StaticValue.TYPE_POI:
					if (command == StaticValue.COMMAND_DELETE) {
						((POIOverlayView) overlay[2]).removePoint(id);
					}
					break;
				case StaticValue.TYPE_WAY:
					if (command == StaticValue.COMMAND_DELETE) {
						((WayPointOverlayView) overlay[3]).removePoint(id);
					}
					break;
				default:
					break;
				}
			}
		}
	}

	OnCheckedChangeListener checkboxListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked)
				overlay[Integer.parseInt(buttonView.getTag().toString())]
						.setVisibility(View.VISIBLE);
			else
				overlay[Integer.parseInt(buttonView.getTag().toString())]
						.setVisibility(View.INVISIBLE);
		}
	};

	private ProgressDialog loadingDialog;

	void updateDBDialog() {
		loadingDialog = ProgressDialog.show(workMappingActivity.this, "",
				"Update...  ", true, false);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				svc.updateIntoDB();
				updateHandler.sendEmptyMessage(0);
			}
		});
		thread.start();
	}
	private Handler updateHandler = new Handler() {
		public void handleMessage(Message msg) {
			updateList();
			loadingDialog.dismiss();

		}

	};

	public void updateList() {
		((MeasurePointOverlayView) overlay[1]).updateList();
		((POIOverlayView) overlay[2]).updateList();
		((WayPointOverlayView) overlay[3]).updateList();
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == UpdateButton) {
				updateDBDialog();
			}else {
				int i;
				for (i = 0; i < arr_button_menu.length; i++)
					if (arr_button_menu[i] == v)
						break;
				if (arr_button_menu.length == i)
					return;
				switch (i) {
				case INDEX_OF_MENU_MP:
					menu_change(INDEX_OF_MENU_BMP, INDEX_OF_MENU_ML,
							INDEX_OF_MENU_BACK);
					break;
				case INDEX_OF_MENU_BMP:
					drawInit();
					bDrawBmp = true;
					Toast.makeText(workMappingActivity.this,
							"Click BMP Draw Point ", Toast.LENGTH_SHORT).show();
					break;
				case INDEX_OF_MENU_ML:
					drawInit();
					bDrawMl = true;
					Toast.makeText(workMappingActivity.this,
							"Click M-LINK Draw 2-Point ", Toast.LENGTH_SHORT)
							.show();
					break;
				case INDEX_OF_MENU_POI:
					drawInit();
					bDrawPOI = true;
					Toast.makeText(workMappingActivity.this,
							"Click POI Draw Point ", Toast.LENGTH_SHORT).show();
					break;
				case INDEX_OF_MENU_WPS:
					menu_change(INDEX_OF_MENU_WP, INDEX_OF_MENU_WL,
							INDEX_OF_MENU_BACK);
					break;
				case INDEX_OF_MENU_WP:
					drawInit();
					bDrawWp = true;
					Toast.makeText(workMappingActivity.this,
							"Click WayPoint Draw Point ", Toast.LENGTH_SHORT)
							.show();
					break;
				case INDEX_OF_MENU_WL:
					drawInit();
					bDrawWl = true;
					Toast.makeText(workMappingActivity.this,
							"Click W-LINK Draw 2-Point ", Toast.LENGTH_SHORT)
							.show();
					break;
				case INDEX_OF_MENU_BACK:
					menu_change(INDEX_OF_MENU_MP, INDEX_OF_MENU_POI,
							INDEX_OF_MENU_WPS);
					drawInit();
					break;
				}
			}
		}
	};

	public class xy {
		float x;
		float y;
	}

}
