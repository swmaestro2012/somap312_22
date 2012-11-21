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
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.softwaremaestro.indoormap.util.MeasurePointOverlayView.MeasurePointType;
import com.softwaremaestro.indoormap.util.POIOverlayView;
import com.softwaremaestro.indoormap.util.PreferenceUtil;
import com.softwaremaestro.indoormap.util.StaticValue;
import com.softwaremaestro.indoormap.util.WayPointOverlayView;
import com.softwaremaestro.indoormap.util.updateOverlayView;

public class collectActivity extends Activity {

	DraggableImageView DimageView;
	ImageView[] overlay;
	Button UpdateButton;
	FrameLayout map_frame;
	Handler handler;
	ArrayList<Accesspoint> apList;
	ArrayList<ApSet> apSetList;
	ArrayList<Fingerprint> fingerPrintList;
	ArrayList<Accesspoint> copyapList;
	ArrayList<ApSet> copyapSetList;
	ArrayList<Fingerprint> copyfingerPrintList;
	ArrayList<ScanResult> mScanResult;
	private byte[] buildingImg;
	private Double z;
	xy curPoint;
	int apListID;
	int fingerPrintListID;
	MappingService svc;

	final String[] arr_str_menu = { "MeasureP", "B-MeasureP", "MeasureLink",
			"P-of-Interest", "WayPs", "WayP", "WayLink", "BACK" };
	int width;
	int height;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
		//
		Intent recieve = getIntent();
		buildingImg = recieve.getByteArrayExtra("img");
		z = recieve.getDoubleExtra("z", 0);
		curPoint = new xy();
		//
		getWindowSize();
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

		UpdateButton = (Button) findViewById(R.id.activity_collect_button_update);
		UpdateButton.setOnClickListener(l);

		init();

	}

	public void init() {
		svc = new MappingService(PreferenceUtil.getAppPreferences(this, "IP"));

		setZorder();
		//
		apListID = 0;
		fingerPrintListID = 0;
		apList = new ArrayList<Accesspoint>();
		apSetList = new ArrayList<ApSet>();
		fingerPrintList = new ArrayList<Fingerprint>();
		copyapList = new ArrayList<Accesspoint>();
		copyapSetList = new ArrayList<ApSet>();
		copyfingerPrintList = new ArrayList<Fingerprint>();
		ArrayList<BaseMeasurepoint> bmpList = new ArrayList<BaseMeasurepoint>();
		ArrayList<Measurepoint> mpList = new ArrayList<Measurepoint>();
		ArrayList<MeasurepointLink> mpLinkList = new ArrayList<MeasurepointLink>();
		try {
			getCollectionList();
			BaseMeasurepoint[] bmp = svc.getAllBaseMeasurepoints();
			Measurepoint[] mp = svc.getAllMeasurepoints();
			MeasurepointLink[] mpl = svc.getAllMeasurepointLinks();
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
			((MeasurePointOverlayView) overlay[1]).updateList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
	}

	public void getCollectionList() throws SQLException {
		apList.clear();
		apSetList.clear();
		fingerPrintList.clear();
		copyapList.clear();
		copyapSetList.clear();
		copyfingerPrintList.clear();

		Accesspoint[] ap = svc.getAllAccesspoints();
		ApSet[] apset = svc.getAllApSets();
		Fingerprint[] finger = svc.getAllFingerprints();
		if (ap != null)
			for (Accesspoint temp : ap) {
				apList.add(temp);
				copyapList.add(temp);
				apListID = temp.getId().intValue();
			}
		if (apset != null)
			for (ApSet temp : apset) {
				apSetList.add(temp);
				copyapSetList.add(temp);
			}
		if (finger != null)
			for (Fingerprint temp : finger) {
				fingerPrintList.add(temp);
				copyfingerPrintList.add(temp);
				fingerPrintListID = temp.getId().intValue();
			}
		((updateOverlayView) overlay[2]).setFingerPrintList(fingerPrintList);
		((updateOverlayView) overlay[2]).invalidate();
	}

	public void setZorder() {
		((MeasurePointOverlayView) overlay[1]).setzOrder(z.intValue());
	}

	public void setImageHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case StaticValue.MSG_SYNC_IMAGEVIEW:
					Matrix m = DimageView.getImageMatrix();
					for (int i = 0; i < 3; i++) {
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
		overlay = new ImageView[3];
		Bitmap bit = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
		overlay[0] = new GridOverlayView(this);
		overlay[1] = new MeasurePointOverlayView(this);
		overlay[2] = new updateOverlayView(this);
		for (int i = 0; i < 3; i++) {
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

	private void callbackClickView(float x, float y) {

		curPoint.x = (int) x / StaticValue.GRID_SIZE * StaticValue.GRID_SIZE
				+ StaticValue.GRID_SIZE / 2;
		curPoint.y = (int) y / StaticValue.GRID_SIZE * StaticValue.GRID_SIZE
				+ StaticValue.GRID_SIZE / 2;

		if (((MeasurePointOverlayView) overlay[1]).isExistPoint(curPoint.x,
				curPoint.y, z.floatValue())) {
			MeasurePointType id = ((MeasurePointOverlayView) overlay[1])
					.getPointID(curPoint.x, curPoint.y);
			if (id != null) {
				Intent intent = new Intent(collectActivity.this,
						collectDialog.class);
				intent.putExtra("id", id.getId());
				intent.putExtra("type", StaticValue.TYPE_MEASURE);
				intent.putExtra("point", id.getType());
				startActivityForResult(intent, StaticValue.REQUEST_POINT_EDIT);
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
						Double X;
						Double Y;
						Double Z;
						if (intent.getIntExtra("point", 1) == 1) {
							BaseMeasurepoint bmp = ((MeasurePointOverlayView) overlay[1])
									.getBaseMeasurePoint(id);
							X = bmp.getX();
							Y = bmp.getY();
							Z = bmp.getZ();
						} else {
							Measurepoint mp = ((MeasurePointOverlayView) overlay[1])
									.getMeasurePoint(id);
							X = mp.getX();
							Y = mp.getY();
							Z = mp.getZ();
						}
						removeFingerPrint(X, Y, Z);
					} else if (command == StaticValue.COMMAND_UPDATE) {
						Double X;
						Double Y;
						Double Z;
						if (intent.getIntExtra("point", 1) == 1) {
							BaseMeasurepoint bmp = ((MeasurePointOverlayView) overlay[1])
									.getBaseMeasurePoint(id);
							X = bmp.getX();
							Y = bmp.getY();
							Z = bmp.getZ();
						} else {
							Measurepoint mp = ((MeasurePointOverlayView) overlay[1])
									.getMeasurePoint(id);
							X = mp.getX();
							Y = mp.getY();
							Z = mp.getZ();
						}
						for (int i = 0; i < 20; i++) {
							mScanResult = intent
									.getParcelableArrayListExtra("result" + i);
							addNewFingerPrint(X, Y, Z);
						}

					}
					break;
				default:
					break;
				}
			}
		}
	}

	private int addNewFingerPrint(Double X, Double Y, Double Z) {
		// TODO Auto-generated method stub
		int fingerID = 0;
		fingerID = ++fingerPrintListID;
		Fingerprint fingerPrint = new Fingerprint(fingerID, new Timestamp(
				new Date().getTime()), X, Y, Z);
		fingerPrintList.add(fingerPrint);
		for (ScanResult temp : mScanResult) {
			int apID = getApID(temp.BSSID);
			if (apID == -1) {
				apID = ++apListID;
				Accesspoint acc = new Accesspoint(apID, temp.BSSID, temp.SSID,
						temp.capabilities, temp.frequency);
				apList.add(acc);
			} else {
				ApSet aps = new ApSet(fingerID, apID, temp.level);
				apSetList.add(aps);
			}
		}
		((updateOverlayView) overlay[2]).setFingerPrintList(fingerPrintList);
		((updateOverlayView) overlay[2]).invalidate();
		return fingerID;
	}

	private int getApID(String bSSID) {
		// TODO Auto-generated method stub
		for (Accesspoint ap : apList) {
			if (ap.getBssid().equals(bSSID))
				return ap.getId().intValue();
		}
		return -1;
	}

	private void removeFingerPrint(Double X, Double Y, Double Z) {
		for (int i = 0; i < fingerPrintList.size();) {
			Fingerprint fp = fingerPrintList.get(i);
			if (fp.getX().equals(X) && fp.getY().equals(Y)
					&& fp.getZ().equals(Z)) {
				for (int j = 0; j < apSetList.size();) {
					if (fp.getId().intValue() == apSetList.get(j)
							.getFingerprintId().intValue()) {
						ApSet aps = apSetList.get(j);
						apSetList.remove(j);
					} else
						j++;
				}
				fingerPrintList.remove(i);
			} else
				i++;
		}
		((updateOverlayView) overlay[2]).setFingerPrintList(fingerPrintList);
		((updateOverlayView) overlay[2]).invalidate();
	}

	private ProgressDialog updateDialog;

	void updatefingerPrintsDialog() {
		updateDialog = ProgressDialog.show(collectActivity.this, "",
				"Update...  ", true, false);
		updateDialog.setCancelable(false);
		updateDialog.setMax(100);
		updateDialog.setMessage("0 % uploded");
		updateDialog.show();

		Thread thread = new Thread(new Runnable() {
			public void run() {
				svc.setUpdate(true);
				for (int i = 0; i < copyfingerPrintList.size();) {
					Fingerprint fp = copyfingerPrintList.get(i);
					if (fingerPrintList.contains(fp)) {
						fingerPrintList.remove(fp);
						copyfingerPrintList.remove(i);
					} else
						i++;
				}
				for (int i = 0; i < copyapSetList.size();) {
					ApSet as = copyapSetList.get(i);
					if (apSetList.contains(as)) {
						apSetList.remove(as);
						copyapSetList.remove(i);
					} else
						i++;
				}
				for (int i = 0; i < copyapList.size();) {
					Accesspoint ap = copyapList.get(i);
					if (apList.contains(ap)) {
						apList.remove(ap);
						copyapList.remove(i);
					} else {

						i++;
					}
				}
				int sum = fingerPrintList.size() + apSetList.size()
						+ copyfingerPrintList.size() + apList.size();
				int progress = 0;
				try {
					for (int i=0; i<copyfingerPrintList.size();) {
						Fingerprint fp = copyfingerPrintList.get(i);
						svc.removeApSetByFingerprintId(fp.getId());
						svc.removeFingerprintById(fp.getId());
						copyfingerPrintList.remove(i);
						progress++;
						Message msg = updatefingerPrintsHandler.obtainMessage();
						msg.arg1 = sum;
						msg.arg2 = progress;
						updatefingerPrintsHandler.sendMessage(msg);
					}
					for (int i=0;i<apList.size();) {
						Accesspoint ap = apList.get(i);
						ap.setId(null);
						svc.addAccesspoint(ap);
						apList.remove(i);
						progress++;
						Message msg = updatefingerPrintsHandler.obtainMessage();
						msg.arg1 = sum;
						msg.arg2 = progress;
						updatefingerPrintsHandler.sendMessage(msg);
					}
					for (int j=0; j<fingerPrintList.size();) {
						Fingerprint fp = fingerPrintList.get(j);
						
						Integer tempfinger = fp.getId();
						fp.setId(null);
						Integer finger = svc.addFingerprint(fp);
						fingerPrintList.remove(j);
						progress++;
						Message msg = updatefingerPrintsHandler.obtainMessage();
						msg.arg1 = sum;
						msg.arg2 = progress;
						updatefingerPrintsHandler.sendMessage(msg);
						for (int i = 0; i < apSetList.size();) {
							ApSet as = apSetList.get(i);
							if (as.getFingerprintId().equals(tempfinger)) {
								as.setFingerprintId(finger);
								svc.addApSet(as);
								apSetList.remove(i);
								progress++;
								Message submsg = updatefingerPrintsHandler
										.obtainMessage();
								submsg.arg1 = sum;
								submsg.arg2 = progress;
								updatefingerPrintsHandler.sendMessage(submsg);
							} else {i++;
							}
						}
					}
					Message submsg = updatefingerPrintsHandler.obtainMessage();
					submsg.what = 10;
					updatefingerPrintsHandler.sendMessage(submsg);
					svc.setUpdate(false);

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message submsg = updatefingerPrintsHandler.obtainMessage();
					submsg.what = 11;
					updatefingerPrintsHandler.sendMessage(submsg);
					svc.setUpdate(false);
				}
			}
		});
		thread.start();
	}

	private Handler updatefingerPrintsHandler = new Handler() {
		public void handleMessage(Message msg) {
			if(msg.what==11){
				updateDialog.dismiss();
				Toast.makeText(collectActivity.this, "update fail retry!", Toast.LENGTH_SHORT).show();
			}
			updateDialog
					.setMessage((int) ((float) msg.arg2 / (float) msg.arg1 * 100)
							+ "% uploded");
			if (msg.arg1 == msg.arg2 || msg.what == 10) {
				try {
					getCollectionList();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateDialog.dismiss();
			}

		}

	};

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == UpdateButton) {
				updatefingerPrintsDialog();
			}
		}
	};

	public class xy {
		float x;
		float y;
	}

}
