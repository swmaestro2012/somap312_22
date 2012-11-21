package com.softwaremaestro.indoormap.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceUtil {
	public static void setAppPreferences(Activity context, String key, String value)
	  {
	    SharedPreferences pref = null;
	    pref = context.getSharedPreferences("IndoorMap", 0);
	    SharedPreferences.Editor prefEditor = pref.edit();
	    prefEditor.putString(key, value);
	    
	    prefEditor.commit();
	  }
	  public static String getAppPreferences(Activity context, String key)
	  {
	    String returnValue = null;
	    SharedPreferences pref = null;
	    pref = context.getSharedPreferences("IndoorMap", Context.MODE_PRIVATE);
	    returnValue = pref.getString(key, "");
	    return returnValue;
	  }
	}
