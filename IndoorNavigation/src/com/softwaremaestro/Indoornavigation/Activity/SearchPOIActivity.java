package com.softwaremaestro.Indoornavigation.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kr.softwaremaestro.indoor.engine.WiFiRadioMap;
import kr.softwaremaestro.indoor.wrm.vo.PointOfInterest;

import com.softwaremaestro.Indoornavigation.R;
import com.softwaremaestro.Indoornavigation.Util.SqliteUtil;
import com.softwaremaestro.Indoornavigation.Util.StaticValue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchPOIActivity extends Activity {
	ListView poiListView;
	poiListViewAdapter adapter;
	ArrayList<PointOfInterest> POIList;
	List<PointOfInterest> tPOIList;
	View buttonSearch;
	WiFiRadioMap wrm;
	EditText searchPOIName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_searchpoi);

		try {
			wrm = WiFiRadioMap.getinstance(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tPOIList = null;
		POIList = new ArrayList<PointOfInterest>();
		if (wrm != null) {
			tPOIList = wrm.getPointOfInterests();
			for (PointOfInterest poi : tPOIList)
				POIList.add(poi);
		}
		poiListView = (ListView) findViewById(R.id.activity_searchpoi_listview);
		adapter = new poiListViewAdapter(this, this,
				android.R.layout.simple_list_item_1, POIList);
		poiListView.setAdapter(adapter);
		poiListView.setOnItemClickListener(listener);
		buttonSearch = (View) findViewById(R.id.activity_searchpoi_button_search);
		buttonSearch.setOnClickListener(l);
		searchPOIName = (EditText) findViewById(R.id.activity_searchpoi_edittext_poiname);
	}

	OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(SearchPOIActivity.this,
					MapActivity.class);
			PointOfInterest poi = POIList.get(arg2);
			intent.putExtra(StaticValue.KEY_POI_ID, poi.getId().intValue());
			intent.putExtra(StaticValue.KEY_POI_X, poi.getX().doubleValue());
			intent.putExtra(StaticValue.KEY_POI_Y, poi.getY().doubleValue());
			intent.putExtra(StaticValue.KEY_POI_Z, poi.getZ().intValue());
			intent.putExtra(StaticValue.KEY_CALL_TYPE,
					StaticValue.TYPE_SELECT_DESTINATION);
			SqliteUtil su = new SqliteUtil(SearchPOIActivity.this);
			if (!su.isExist(poi.getId().intValue())) {
				String query = new String()
						.format("insert into recentSearchList (id,name,x,y,z) values (%d,\"%s\",%f,%f,%f);",
								poi.getId().intValue(), poi.getName(), poi
										.getX().doubleValue(), poi.getY()
										.doubleValue(), poi.getZ()
										.doubleValue());
				su.executeQuery(query);
			}
			startActivity(intent);
			finish();
		}
	};

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (buttonSearch == v) {
				POIList.clear();
				if (tPOIList != null) {
					for (PointOfInterest poi : tPOIList)
						if (searchPOIName.getText().toString().equals("")
								|| poi.getName().equals(
										searchPOIName.getText().toString()))
							POIList.add(poi);
					adapter.notifyDataSetChanged();
				}
			}
		}
	};

	public class poiListViewAdapter extends ArrayAdapter<PointOfInterest> {
		private Context context;
		private ArrayList<PointOfInterest> items;
		private Activity activity;
		private LayoutInflater inflater = null;
		private int ResourceId;

		public poiListViewAdapter(Activity a, Context context,
				int textViewResourceId, ArrayList<PointOfInterest> items) {
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

			PointOfInterest item = items.get(position);

			TextView tv = (TextView) v;
			tv.setText(item.getName());

			return v;
		}
	}

}
