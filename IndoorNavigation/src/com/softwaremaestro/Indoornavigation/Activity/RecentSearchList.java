package com.softwaremaestro.Indoornavigation.Activity;

import java.util.ArrayList;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecentSearchList extends Activity{
	Intent intent;
	ListView recentSearchListView;
	recentSearchListAdapter adapter;
	ArrayList<PointOfInterest> POIList;
	final int KEY_DELETE = 1;
	final int KEY_SELECT = 2;
	View buttonAllDelete;
	SqliteUtil util;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_recentsearchlist);
		
		intent = getIntent();
		
		util = new SqliteUtil(this);
		POIList = new ArrayList<PointOfInterest>();
		recentSearchListView = (ListView) findViewById(R.id.dialog_recentsearchlist_listview);
		adapter = new recentSearchListAdapter(this, this,
				R.layout.adapterview_recentsearchlist_item, POIList);
		recentSearchListView.setAdapter(adapter);
		buttonAllDelete = (View)findViewById(R.id.dialog_recentsearchlist_button_all_delete);
		buttonAllDelete.setOnClickListener(l);
		
		refreshList();
	}
	
	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == buttonAllDelete) {
				String query="delete from recentSearchList where id > 0;";
				util.executeQuery(query);
				refreshList();
			} else {
				Tag t = (Tag) v.getTag(R.id.recentsearchlistkey);
				switch (t.type) {
				case KEY_DELETE:
					String query=new String().format("delete from recentSearchList where id = %d;", POIList.get(t.count).getId().intValue());
					util.executeQuery(query);
					refreshList();
					break;
				case KEY_SELECT:
					intent.putExtra(StaticValue.KEY_POI_ID, POIList.get(t.count).getId().intValue());
					intent.putExtra(StaticValue.KEY_POI_X, POIList.get(t.count).getX().doubleValue());
					intent.putExtra(StaticValue.KEY_POI_Y, POIList.get(t.count).getY().doubleValue());
					intent.putExtra(StaticValue.KEY_POI_Z, POIList.get(t.count).getZ().intValue());
					intent.putExtra(StaticValue.KEY_CALL_TYPE, StaticValue.TYPE_SELECT_DESTINATION);
					setResult(RESULT_OK,intent);
					finish();
					break;
				}
			}
		}

		
	};
	
	private void refreshList() {
		ArrayList<PointOfInterest> temp = util.selectRecentPOIData();
		POIList.clear();
		for(PointOfInterest poi : temp)
			POIList.add(poi);
		adapter.notifyDataSetChanged();
	}
	
	public class recentSearchListAdapter extends ArrayAdapter<PointOfInterest> {
		private Context context;
		private ArrayList<PointOfInterest> items;
		private Activity activity;
		private LayoutInflater inflater = null;
		private int ResourceId;

		public recentSearchListAdapter(Activity a, Context context,
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

			TextView name = (TextView) v
					.findViewById(R.id.adapterview_recentsearchlist_item_title);
			name.setText(item.getName());
			name.setTag(R.id.recentsearchlistkey, new Tag(KEY_SELECT, position));
			Button delete = (Button) v
					.findViewById(R.id.adapterview_recentsearchlist_item_title_delete);
			delete.setTag(R.id.recentsearchlistkey, new Tag(KEY_DELETE, position));
			
			delete.setOnClickListener(l);
			name.setOnClickListener(l);
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

}
