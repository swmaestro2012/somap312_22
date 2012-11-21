package com.softwaremaestro.indoormap.acivity;


import com.softwaremaestro.indoormap.R;
import com.softwaremaestro.indoormap.util.StaticValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class editDialog extends Activity {
	int pointID;
	int pointType;


	Button DeleteButton;
	Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_edit);

		DeleteButton = (Button) findViewById(R.id.dialog_edit_button_delete);
		DeleteButton.setOnClickListener(l);
		
		intent = getIntent();
	}

	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v == DeleteButton) {
				intent.putExtra("command", StaticValue.COMMAND_DELETE);
				setResult(RESULT_OK, intent);
				finish();
			}
		}

	};
}
