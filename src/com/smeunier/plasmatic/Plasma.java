package com.smeunier.plasmatic;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Plasma extends Activity {
	private PlasmaView plasmaView;
	private StartAppAd startAppAd = new StartAppAd(this);
	
	public static final String PREFS_NAME = "PlasmaticPrefs";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "106210200", "206038414");
        UpdateViews();
    }
    
	@Override
	public void onBackPressed() {
		startAppAd.onBackPressed();
	    super.onBackPressed();
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    startAppAd.onResume();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
  	}

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {    
    	switch (item.getItemId()) {
		    case R.id.menu_save:
		        //Save
				File path = new File(Environment.getExternalStorageDirectory() + File.separator + "plasmatic");
				try{
					path.mkdirs();
				}
				catch(SecurityException e){
					Toast.makeText(this, "Unable to write on the sd card", Toast.LENGTH_LONG).show();
					return true;
				}
				Date date = new Date();
				String timestamp = Integer.toString(date.getYear()) + "-"+ Integer.toString(date.getDate()) + "-" + Integer.toString(date.getDay()) + "-" + Integer.toString(date.getHours()) + Integer.toString(date.getMinutes()) + Integer.toString(date.getSeconds()); 
				File file = new File(Environment.getExternalStorageDirectory() + File.separator + "plasmatic" + File.separator + timestamp + ".png");
				try {
					file.createNewFile();
				} catch (IOException e) {
					Toast.makeText(this, "Unable to write file", Toast.LENGTH_LONG).show();
					return true;
				}
				plasmaView.SaveImage(file);
		        break;
		    case R.id.menu_settings:
		    	//Settings
				Intent intent = new Intent(Plasma.this, PlasmaSettings.class);
				Plasma.this.startActivity(intent);

		        break;
		    case R.id.menu_refresh:
		    	UpdateViews();

		        break;
		    case R.id.menu_about:
	        	AboutDialog about = new AboutDialog(this);
	        	about.setTitle("About this app");
	        	about.show();
	        	break;
	    }
	    return true;
    }
    
	
    public void UpdateViews() {
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	display.getSize(size);

        setContentView(R.layout.main_activity_portrait);

    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        LinearLayout layout = (LinearLayout)findViewById(R.id.plasmacontainer);

        plasmaView = new PlasmaView(this, size.x, size.y, settings.getString("plasmaType", "Plasma"), Float.valueOf(settings.getString("roughness", "2")).floatValue(), 0);
        layout.addView(plasmaView);
    
    }
}