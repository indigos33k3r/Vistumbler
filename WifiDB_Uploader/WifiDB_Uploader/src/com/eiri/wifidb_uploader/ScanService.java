package com.eiri.wifidb_uploader;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ScanService extends IntentService{
	private static final String TAG = "ScanService";
	WifiManager wifi;
	BroadcastReceiver receiver;
	
	// No-arg constructor is required
	public ScanService() {
	   super("ScanService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
	   Log.d(TAG, "Scan Service started");
	   
		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Register Broadcast Receiver
		if (receiver == null)
			receiver = new WiFiScanReceiver(this);

		registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		
		wifi.startScan();
	}
	
	public void onStop() {
		if(receiver != null) unregisterReceiver(receiver);
	}
	public void onDestroy(){
		if(receiver != null) unregisterReceiver(receiver);	
	}
}