package com.eiri.wifidb_uploader;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class WiFiDemo extends Activity implements OnClickListener {
	private static final String TAG = "WiFiDemo";
	Intent ScanServiceIntent;

	Switch ScanSwitch;
	TextView textStatus;
	Button buttonScan;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
	       .detectNetwork() // or .detectAll() for all detectable problems
	       .penaltyDialog()  //show a dialog
	       .permitNetwork() //permit Network access 
	       .build());

		// Setup UI
		
		ScanSwitch = (Switch) findViewById(R.id.ScanSwitch);
		ScanSwitch.setOnClickListener(this);
		textStatus = (TextView) findViewById(R.id.textStatus);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);
		
		ScanServiceIntent =new Intent(this, ScanService.class);

	}

	public void onClick(View src) {
		switch (src.getId()) {
		    case R.id.ScanSwitch:
		    	Log.d(TAG, "ScanSwitch Pressed");
		      	ScanSwitch = (Switch) findViewById(R.id.ScanSwitch);
		      	if (ScanSwitch.isChecked()){
		      		Log.d(TAG, "Start Scan");
		      		startService(ScanServiceIntent);
		      		ScanSwitch.setChecked(true);
		      	} else {
		      		Log.d(TAG, "Stop Scan");
		      		stopService(ScanServiceIntent);
		      		ScanSwitch.setChecked(false);
		        }
		      	break;
		    case R.id.buttonScan:
		    	Log.d(TAG, "buttonScan Pressed");
		    	stopService(new Intent(this, ScanService.class));
		    	break;
	    }

	}
}