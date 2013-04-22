package com.eiri.wifidb_uploader;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ScanService extends Service {
	private static final String TAG = "WiFiDB_ScanService";
	private static Timer timer;
	private Context ctx;
	static WifiManager wifi;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		ctx = this; 
		Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");   		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show(); 	
		// Stop Timer
		if(timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
				
		// Stop WiFi
		wifi = null;
						
		// Stop GpS
		MyLocation my_location = new MyLocation();
		my_location.destroy(this, null);			
		my_location = null;
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		timer = new Timer();
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				
		//Setup GpS
		MyLocation my_location = new MyLocation();
		my_location.init(this, null);			
		wifi.startScan();
		timer.scheduleAtFixedRate(new mainTask(), 0, 5000);
	}
	
	private class mainTask extends TimerTask
    { 
        public void run() 
        {
        	// Get Prefs
        	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        	String WifiDb_ApiURL = sharedPrefs.getString("wifidb_upload_api_url", "https://api.wifidb.net/");
        	String WifiDb_Username = sharedPrefs.getString("wifidb_username", "Anonymous"); 
        	String WifiDb_ApiKey = sharedPrefs.getString("wifidb_upload_api_url", "");     	
        	String WifiDb_SID = "1";
	    		    
        	// Get Location
        	Location location = MyLocation.getLocation(ctx);
        	final Double latitude = location.getLatitude();
        	final Double longitude = location.getLongitude();
        	Integer sats = MyLocation.getGpsStatus(ctx);      	
        	Log.d(TAG, "LAT: " + latitude + "LONG: " + longitude + "SATS: " + sats);
    	    	    
        	// Get Wifi Info
        	List<ScanResult> results = ScanService.wifi.getScanResults();
        	for (ScanResult result : results) {    	    	    
            	 	Log.d(TAG, "onReceive() http post");
                  	WifiDB post = new WifiDB();
    	    	    String Label = "";
    	   	    	post.postLiveData(WifiDb_ApiURL, WifiDb_Username, WifiDb_ApiKey, WifiDb_SID, result.SSID, result.BSSID, result.capabilities, result.frequency, result.level, latitude, longitude, Label);
     	    }
        }
    }	
}