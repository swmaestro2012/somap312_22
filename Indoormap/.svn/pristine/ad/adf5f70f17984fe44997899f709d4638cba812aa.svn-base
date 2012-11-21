package com.softwaremaestro.indoormap.acivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.softwaremaestro.indoor.wrm.service.MappingService;
import kr.softwaremaestro.indoor.wrm.vo.Floor;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaremaestro.indoormap.R;
import com.softwaremaestro.indoormap.util.PreferenceUtil;

public class selectBuildingActivity extends Activity {

	ListView buildingListView;
	ArrayList<Floor> FloorList;
	final int KEY_DELETE = 1;
	final int KEY_SELECT = 2;
	final int KEY_COLLECT = 3;
	final int REQUEST_CREATE_FLOOR = 100;
	Button addButton;
	buildingListAdapter adapter;
	Intent intent;
	int id;
	MappingService svc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_building);
		svc = new MappingService(PreferenceUtil.getAppPreferences(this, "IP"));
		FloorInit();
		buildingListView = (ListView) findViewById(R.id.activity_select_building_listview);
		adapter = new buildingListAdapter(this, this,
				R.layout.adapterview_selectbuilding_floor_list, FloorList);
		buildingListView.setAdapter(adapter);

		addButton = (Button) findViewById(R.id.activity_select_building_button_add_floor);
		addButton.setOnClickListener(l);

	}

	public void FloorInit() {
		id = 0;
		FloorList = new ArrayList<Floor>();
		Floor[] floor_arr = null;
		try {
			Log.e("DB service", "Service : " + svc.toString());
			Log.e("DB service", "callgetallfloor");
			floor_arr = svc.getAllFloors();
			Log.e("DB service", "Service : " + floor_arr);
			if (floor_arr == null)
				return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (floor_arr != null) {
			for (Floor f : floor_arr) {
				FloorList.add(f);
				Log.e("fd", f.toString());
				id = f.getId().intValue();
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CREATE_FLOOR) {
				this.intent = intent;
				createThreadAndDialog();
			}
		}
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == addButton) {
				Intent intent = new Intent(selectBuildingActivity.this,
						createFloorDialog.class);
				startActivityForResult(intent, REQUEST_CREATE_FLOOR);
			} else {
				Tag t = (Tag) v.getTag(R.id.floorlistkey);
				switch (t.type) {
				case KEY_DELETE:
					try {
						svc.removeFloorById(FloorList.get(t.count).getId()
								.intValue());

						svc.updateIntoDB();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					FloorList.remove(t.count);
					adapter.notifyDataSetChanged();
					break;
				case KEY_SELECT:
					Intent intent = new Intent(selectBuildingActivity.this,
							workMappingActivity.class);
					//
					Floor f = FloorList.get(t.count);
					intent.putExtra("img", f.getImage());
					Log.e("fd", "aa" + f.getImage());
					intent.putExtra("z", f.getZ());
					intent.putExtra("scale", f.getImageScale());
					//
					startActivity(intent);
					break;
				case KEY_COLLECT:
					Intent c_intent = new Intent(selectBuildingActivity.this,
							collectActivity.class);
					//
					Floor cf = FloorList.get(t.count);
					c_intent.putExtra("img", cf.getImage());
					c_intent.putExtra("z", cf.getZ());
					c_intent.putExtra("scale", cf.getImageScale());
					//
					startActivity(c_intent);
					break;
				}
			}
		}
	};

	public class buildingListAdapter extends ArrayAdapter<Floor> {
		private Context context;
		private ArrayList<Floor> items;
		private Activity activity;
		private LayoutInflater inflater = null;
		private int ResourceId;

		public buildingListAdapter(Activity a, Context context,
				int textViewResourceId, ArrayList<Floor> items) {
			super(context, textViewResourceId, items);
			activity = a;
			this.items = items;
			this.context = context;
			ResourceId = textViewResourceId;
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = ((Activity) context).getLayoutInflater();
				v = vi.inflate(ResourceId, null);
			}

			Floor item = items.get(position);

			TextView name = (TextView) v
					.findViewById(R.id.adapterview_select_building_floor_list_title);
			name.setText(item.getName());
			Button delete = (Button) v
					.findViewById(R.id.adapterview_select_building_floor_list_delete);
			delete.setTag(R.id.floorlistkey, new Tag(KEY_DELETE, position));
			Button select = (Button) v
					.findViewById(R.id.adapterview_select_building_floor_list_select);
			select.setTag(R.id.floorlistkey, new Tag(KEY_SELECT, position));
			Button collect = (Button) v
					.findViewById(R.id.adapterview_select_building_floor_list_collect);
			collect.setTag(R.id.floorlistkey, new Tag(KEY_COLLECT, position));
			delete.setOnClickListener(l);
			collect.setOnClickListener(l);
			select.setOnClickListener(l);

			return v;
		}
	}

	public class Tag {
		int type;
		int count;

		Tag(int t, int c) {
			type = t;
			count = c;
		}
	}

	private ProgressDialog loadingDialog;

	void createThreadAndDialog() {
		loadingDialog = ProgressDialog.show(selectBuildingActivity.this, "",
				"Wating...  ", true, false);

		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					String imgUrl = intent.getStringExtra("imgUrl");
					HttpURLConnection conn = (HttpURLConnection) new URL(imgUrl)
							.openConnection();
					InputStream in = conn.getInputStream();
					Bitmap bit = BitmapFactory.decodeStream(in);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					bit.compress(CompressFormat.PNG, 100, out);
					byte[] img = out.toByteArray();
					String name = intent.getStringExtra("name");
					int imgWidth = bit.getWidth();
					int imgHeight = bit.getHeight();
					String imgOriginX = intent.getStringExtra("imgOriginX");
					String imgOriginY = intent.getStringExtra("imgOriginY");
					String zOrder = intent.getStringExtra("zOrder");
					String imgScale = intent.getStringExtra("imgScale");
					Floor f = new Floor(++id, name, img, imgWidth, imgHeight,
							Integer.parseInt(imgOriginX), Integer
									.parseInt(imgOriginY), Double
									.parseDouble(imgScale), Double
									.parseDouble(zOrder));
					FloorList.add(f);
					svc.addFloor(f);
					svc.updateIntoDB();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			loadingDialog.dismiss();
			adapter.notifyDataSetChanged();
		}
	};
}
