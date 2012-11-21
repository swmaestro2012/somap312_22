package com.softwaremaestro.indoormap.acivity;

import com.softwaremaestro.indoormap.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class createFloorDialog extends Activity {
	
	EditText name;
	EditText imgUrl;
	EditText imgOriginX;
	EditText imgOriginY;
	EditText imgScale;
	EditText zOrder;
	Button commit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_create_floor);
		 name = (EditText)findViewById(R.id.dialog_create_floor_name);
		 imgUrl=(EditText)findViewById(R.id.dialog_create_floor_url);
		 imgOriginX=(EditText)findViewById(R.id.dialog_create_floor_origin_x);
		 imgOriginY=(EditText)findViewById(R.id.dialog_create_floor_origin_y);
		 imgScale=(EditText)findViewById(R.id.dialog_create_floor_image_scale);
		 zOrder=(EditText)findViewById(R.id.dialog_create_floor_zorder);
		 commit = (Button)findViewById(R.id.dialog_create_floor_button_commit);
		 commit.setOnClickListener(l);
	}
	
	OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("name",name.getText().toString());
			intent.putExtra("imgOriginX",imgOriginX.getText().toString());
			intent.putExtra("imgOriginY",imgOriginY.getText().toString());
			intent.putExtra("zOrder",zOrder.getText().toString());
			intent.putExtra("imgScale",imgScale.getText().toString());
			intent.putExtra("imgUrl",imgUrl.getText().toString());
			setResult(RESULT_OK,intent);
			finish();
		}
	};
}
