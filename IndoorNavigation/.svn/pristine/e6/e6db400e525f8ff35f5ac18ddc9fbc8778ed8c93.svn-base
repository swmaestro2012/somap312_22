package com.softwaremaestro.Indoornavigation.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.softwaremaestro.Indoornavigation.R;

public class MenuActivity extends Activity {
	View buttonMymenu;
	View butoonMap;
	View buttonSearchPOI;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        
        buttonMymenu = (View)findViewById(R.id.activity_mainmenu_button_mymenu);
        butoonMap = (View)findViewById(R.id.activity_mainmenu_button_map);
        buttonSearchPOI = (View)findViewById(R.id.activity_mainmenu_button_searchpoi);
        buttonMymenu.setOnClickListener(l);
        butoonMap.setOnClickListener(l);
        buttonSearchPOI.setOnClickListener(l);
    }
    
    OnClickListener l = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v == buttonMymenu)
				startActivity(new Intent(MenuActivity.this, MyMenuActivity.class));
			else if(v == butoonMap)
				startActivity(new Intent(MenuActivity.this, MapActivity.class));
			else if(v == buttonSearchPOI)
				startActivity(new Intent(MenuActivity.this, SearchPOIActivity.class));
			
		}
	};
}
