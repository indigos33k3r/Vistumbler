package com.eiri.wifidb_uploader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WifiDB_Uploader";

 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_GPS_TABLE = "CREATE TABLE GPS(" 
        		+ "GPSID INTEGER PRIMARY KEY,"
        		+ "Latitude REAL,"
        		+ "Longitude REAL,"
                + "NumOfSats INTEGER," 
                + "Accuracy REAL," 
                + "Alt REAL," 
                + "Speed REAL," 
                + "TrackAngle REAL," 
                + "DateTime TEXT"
                + ")";
        db.execSQL(CREATE_GPS_TABLE); 
        
        String CREATE_AP_TABLE = "CREATE TABLE AP(" 
        		+ "ApID INTEGER PRIMARY KEY,"
        		+ "BSSID TEXT,"
        		+ "SSID TEXT,"
                + "CHAN INTEGER," 
                + "AUTH TEXT," 
                + "ENCR TEXT," 
                + "SECTYPE TEXT," 
                + "NETTYPE TEXT," 
                + "RADTYPE TEXT," 
                + "BTX TEXT," 
                + "OTX TEXT,"
                + "HighGpsID INTEGER,"
                + "FirstGpsID INTEGER,"
                + "LastGpsID INTEGER,"
                + "MANU TEXT,"
                + "LABEL TEXT,"
                + "Signal INTEGER,"
                + "HighSignal INTEGER,"
                + "RSSI INTEGER,"
                + "HighRSSI INTEGER,"
                + "Active INTEGER,"
                + "CountryCode TEXT,"
                + "CountryName TEXT,"
                + "AdminCode TEXT,"
                + "AdminName TEXT,"
                + "Admin2Name TEXT"
                + ")";
        db.execSQL(CREATE_AP_TABLE);
        
        String CREATE_HIST_TABLE = "CREATE TABLE HIST(" 
        		+ "HistID INTEGER PRIMARY KEY,"
        		+ "ApID REAL,"
        		+ "GpsID REAL,"
                + "Signal INTEGER," 
                + "RSSI REAL," 
                + "DateTime TEXT,"
                + "UploadCode INTEGER"
                + ")";
        db.execSQL(CREATE_HIST_TABLE);
        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS GPS");
        db.execSQL("DROP TABLE IF EXISTS AP");
        db.execSQL("DROP TABLE IF EXISTS HIST");
 
        // Create tables again
        onCreate(db);
    }

    long addGPS(Double Latitude, Double Longitude, Integer NumOfSats, float Accuracy, double Alt, float Speed, float TrackAngle, String DateTime) {
        SQLiteDatabase db = this.getWritableDatabase();         
 
        ContentValues values = new ContentValues();
        values.put("Latitude", Latitude);
        values.put("Longitude", Longitude);
        values.put("NumOfSats", NumOfSats);
        values.put("Accuracy", Accuracy);
        values.put("Alt", Alt);
        values.put("Speed", Speed);
        values.put("TrackAngle", TrackAngle);
        values.put("DateTime", DateTime);
        
        // Inserting Row
        long id = db.insert("GPS", null, values);
        db.close(); // Closing database connection
        
        return id;
    }
    
    long addAP(long GpsID, String BSSID, String SSID, Integer frequency, String capabilities, Integer level, String DateTime) {
	    String Found_AUTH = "";
	    String Found_ENCR = "";
	    Integer Found_SecType = 0;
        Integer chan = 0;
        String radio = "";
	    String nt = "";
	    
	    Integer dBmMaxSignal = -30;
	    Integer dBmDissociationSignal = -85;
	    Integer Found_Signal = Math.round(100 - 80 * (dBmMaxSignal - level) / (dBmMaxSignal - dBmDissociationSignal));
	    if (Found_Signal < 0) Found_Signal = 0;
	    //convert to vistumbler format data
	    if(capabilities.contains("WPA2-PSK-CCMP") || capabilities.contains("WPA2-PSK-TKIP+CCMP"))
        {	
	    	Found_AUTH = "WPA2-Personal";
            Found_ENCR = "CCMP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA-PSK-CCMP") || capabilities.contains("WPA-PSK-TKIP+CCMP"))
        {	
        	Found_AUTH = "WPA-Personal";
            Found_ENCR = "CCMP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA2-EAP-CCMP") || capabilities.contains("WPA2-EAP-TKIP+CCMP"))
        {	
        	Found_AUTH = "WPA2-Enterprise";
            Found_ENCR = "CCMP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA-EAP-CCMP") || capabilities.contains("WPA-EAP-TKIP+CCMP"))
        {
        	Found_AUTH = "WPA-Enterprise";
            Found_ENCR = "CCMP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA2-PSK-TKIP"))
        {	
        	Found_AUTH = "WPA2-Personal";
            Found_ENCR = "TKIP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA-PSK-TKIP"))
        {	
        	Found_AUTH = "WPA-Personal";
            Found_ENCR = "TKIP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA2-EAP-TKIP"))
        {	
        	Found_AUTH = "WPA2-Enterprise";
            Found_ENCR = "TKIP";
            Found_SecType = 3;
        }else if(capabilities.contains("WPA-EAP-TKIP"))
        {	
        	Found_AUTH = "WPA-Enterprise";
            Found_ENCR = "TKIP";
            Found_SecType = 3;
        }else if(capabilities.contains("WEP"))
        {	
        	Found_AUTH = "Open";
            Found_ENCR = "WEP";
            Found_SecType = 2;
        }else
        {	
        	Found_AUTH = "Open";
            Found_ENCR = "None";
            Found_SecType = 1;
        }
        if(capabilities.contains("IBSS"))
        {
            nt = "Ad-Hoc";
        }else
        {
            nt = "Infrastructure";
        }
        
        switch(frequency)
        {
            case 2412:
                chan = 1;
                radio = "802.11g";
            break;
            case 2417:
                chan = 2;
                radio = "802.11g";
            break;
            case 2422:
                chan = 3;
                radio = "802.11g";
            break;
            case 2427:
                chan = 4;
                radio = "802.11g";
            break;
            case 2432:
                chan = 5;
                radio = "802.11g";
            break;
            case 2437:
                chan = 6;
                radio = "802.11g";
            break;
            case 2442:
                chan = 7;
                radio = "802.11g";
            break;
            case 2447:
                chan = 8;
                radio = "802.11g";
            break;
            case 2452:
                chan = 9;
                radio = "802.11g";
            break;
            case 2457:
                chan = 10;
                radio = "802.11g";
            break;
            case 2462:
                chan = 11;
                radio = "802.11g";
            break;
            case 2467:
                chan = 12;
                radio = "802.11g";
            break;
            case 2472:
                chan = 13;
                radio = "802.11g";
            break;
            case 2484:
                chan = 14;
                radio = "802.11g";
            break;
            case 5180:
            	chan = 36;
            	radio = "802.11n";
        	break;
            case 5200:
            	chan = 40;
            	radio = "802.11n";
        	break;
            case 5220:
            	chan = 44;
            	radio = "802.11n";
        	break;
            case 5240:
            	chan = 48;
            	radio = "802.11n";
        	break;
            case 5260:
            	chan = 52;
            	radio = "802.11n";
        	break;
            case 5280:
            	chan = 56;
            	radio = "802.11n";
        	break;
            case 5300:
            	chan = 60;
            	radio = "802.11n";
        	break;
            case 5320:
            	chan = 64;
            	radio = "802.11n";
        	break;
            case 5500:
            	chan = 100;
            	radio = "802.11n";
        	break;
            case 5520:
            	chan = 104;
            	radio = "802.11n";
        	break;
            case 5540:
            	chan = 108;
            	radio = "802.11n";
        	break;
            case 5560:
            	chan = 112;
            	radio = "802.11n";
        	break;
            case 5580:
            	chan = 116;
            	radio = "802.11n";
        	break;
            case 5600:
            	chan = 120;
            	radio = "802.11n";
        	break;
            case 5620:
            	chan = 124;
            	radio = "802.11n";
        	break;
            case 5640:
            	chan = 128;
            	radio = "802.11n";
        	break;
            case 5660:
            	chan = 132;
            	radio = "802.11n";
        	break;
            case 5680:
            	chan = 136;
            	radio = "802.11n";
        	break;
            case 5700:
            	chan = 140;
            	radio = "802.11n";
        	break;
            case 5745:
            	chan = 149;
            	radio = "802.11n";
        	break;
            case 5765:
            	chan = 153;
            	radio = "802.11n";
        	break;
            case 5785:
            	chan = 157;
            	radio = "802.11n";
        	break;
            case 5805:
            	chan = 161;
            	radio = "802.11n";
        	break;
            case 5825:
            	chan = 165;
            	radio = "802.11n";
        	break;
            default:
                chan = 6;
                radio = "802.11g";
            break;
        }    
        long apid = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql;
        sql = "SELECT ApID, HighGpsID, FirstGpsID, LastGpsID, Active, SECTYPE, HighSignal, HighRSSI FROM AP WHERE BSSID='" + BSSID + "' And SSID='" + SSID + "' And CHAN=" + chan + " And AUTH='" + Found_AUTH + "' And ENCR='" + Found_ENCR + "' And RADTYPE='" + radio + "' limit 1";
        Cursor data = db.rawQuery(sql, null);
        if (data.moveToFirst()) {
        	long Found_ApID = data.getLong(data.getColumnIndex("ApID"));
        	//long Found_HighGpsID = data.getLong(data.getColumnIndex("HighGpsID"));
        	long Found_FirstGpsID = data.getLong(data.getColumnIndex("FirstGpsID"));
        	long Found_LastGpsID = data.getLong(data.getColumnIndex("LastGpsID"));
        	//Integer Found_Active = data.getInt(data.getColumnIndex("Active"));
        	//Integer Found_SECTYPE = data.getInt(data.getColumnIndex("SECTYPE"));
        	Integer Found_HighSignal = Integer.parseInt(data.getString(data.getColumnIndex("HighSignal")));
        	Integer Found_HighRSSI = Integer.parseInt(data.getString(data.getColumnIndex("HighRSSI")));
        	apid = Found_ApID;
        	
        	sql = "select GpsID, DateTime from Hist WHERE ApID=" + Found_ApID + " And DateTime>'" + DateTime + "' ORDER BY DateTime DESC LIMIT 1";
        	Cursor lgiddata = db.rawQuery(sql, null);
        	long ExpLastGpsID;
			//String ExpLastDateTime;
			if (lgiddata.moveToFirst()) {
				ExpLastGpsID = lgiddata.getInt(lgiddata.getColumnIndex("GpsID"));
				//ExpLastDateTime = lgiddata.getString(lgiddata.getColumnIndex("DateTime"));
        	} else {
				ExpLastGpsID = GpsID;
				//ExpLastDateTime = DateTime;
        	}
			
        	sql = "select GpsID, DateTime from Hist WHERE ApID=" + Found_ApID + " And DateTime<'" + DateTime + "' ORDER BY DateTime ASC LIMIT 1";
        	Cursor fgiddata = db.rawQuery(sql, null);
        	long ExpFirstGpsID;
			//String ExpFirstDateTime;
			if (fgiddata.moveToFirst()) {
				ExpFirstGpsID = fgiddata.getInt(fgiddata.getColumnIndex("GpsID"));
				//ExpFirstDateTime = fgiddata.getString(fgiddata.getColumnIndex("DateTime"));
        	} else {
        		ExpFirstGpsID = GpsID;
        		//ExpFirstDateTime = DateTime;
        	}
			
			if (ExpLastGpsID!=Found_LastGpsID){
				sql = "UPDATE AP SET LastGpsID=" + ExpLastGpsID + " WHERE ApID=" + Found_ApID;
				db.execSQL(sql);
			}
			if (ExpFirstGpsID!=Found_FirstGpsID){
				sql = "UPDATE AP SET FirstGpsID=" + ExpFirstGpsID + " WHERE ApID=" + Found_ApID;
				db.execSQL(sql);
			}
			sql = "UPDATE AP SET Signal=" + Found_Signal + ", RSSI=" + level + " WHERE ApID=" + Found_ApID;
			db.execSQL(sql);
			
			Log.d("WifiDB", "Found_Signal: " + Found_Signal.toString()
        			+ "Found_HighSignal: " + Found_HighSignal.toString()
        			+ "level: " + level.toString()
        			+ "Found_HighRSSI: " + Found_HighRSSI.toString());
			
			if (Found_Signal > Found_HighSignal){
				sql = "UPDATE AP SET HighSignal=" + Found_Signal + " WHERE ApID=" + Found_ApID;
				db.execSQL(sql);
			}
			if (level > Found_HighRSSI){
				sql = "UPDATE AP SET HighRSSI=" + level + " WHERE ApID=" + Found_ApID;
				db.execSQL(sql);
			}
        } else {
        	
            ContentValues values = new ContentValues();
            values.put("BSSID", BSSID);
            values.put("SSID", SSID);
            values.put("CHAN", chan);
            values.put("AUTH", Found_AUTH);
            values.put("ENCR", Found_ENCR);
            values.put("SECTYPE", Found_SecType);
            values.put("NETTYPE", nt);
            values.put("RADTYPE", radio);
            values.put("BTX", "");
            values.put("OTX", "");
            values.put("HighGpsID", 0);
            values.put("FirstGpsID", GpsID);
            values.put("LastGpsID", GpsID);
            values.put("MANU", "");
            values.put("LABEL", "");
            values.put("Signal", Found_Signal);
            values.put("HighSignal", Found_Signal);
            values.put("RSSI", level);
            values.put("HighRSSI", level);
            values.put("Active", 1);
            values.put("CountryCode", "");
            values.put("CountryName", "");
            values.put("AdminCode", "");
            values.put("AdminName", "");
            values.put("Admin2Name", "");
            apid = db.insert("AP", null, values);
            
            
        }
        db.close(); // Closing database connection
		return apid;
    }

    long addHist(long ApID, long GpsID, Integer RSSI, String DateTime) {
    	long histid = -1;
	    int dBmMaxSignal = -30;
	    int dBmDissociationSignal = -85;
	    int Signal = Math.round(100 - 80 * (dBmMaxSignal - RSSI) / (dBmMaxSignal - dBmDissociationSignal));
	    if (Signal < 0) Signal = 0;   	
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT HistID FROM Hist WHERE ApID=" + ApID + " And GpsID=" + GpsID + " And Signal=" + Signal + " And RSSI=" + RSSI + " And DateTime='" + DateTime + "' LIMIT 1";
        Cursor data = db.rawQuery(sql, null);
        if (data.moveToFirst()) { 	    	
        	histid = Long.parseLong(data.getString(data.getColumnIndex("HistID")));
        }else{
        	ContentValues values = new ContentValues();
        	values.put("ApID", ApID);
        	values.put("GpsID", GpsID);
        	values.put("Signal", Signal);
        	values.put("RSSI", RSSI);
        	values.put("DateTime", DateTime);
        	values.put("UploadCode", 0);
            histid = db.insert("HIST", null, values);
        }
        db.close();
        return histid;
    }
    
    int UploadToWifiDB(String WifiDbApiURL, String WifiDbApiUser, String WifiDbApiPass) {
    	int uploaded = 0;
    	
    	
    	SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT GpsID, ApID, Signal, RSSI FROM Hist WHERE UploadCode=0 LIMIT 1";
        Cursor data = db.rawQuery(sql, null);
        while (data.moveToNext()) {
        	long Found_GpsID = data.getLong(data.getColumnIndex("GpsID"));
        	long Found_ApID = data.getLong(data.getColumnIndex("ApID"));
        	Integer Found_Signal = data.getInt(data.getColumnIndex("Signal"));
        	Integer Found_RSSI = data.getInt(data.getColumnIndex("RSSI"));
            String gpssql = "SELECT Latitude, Longitude, NumOfSats, Alt, Speed, TrackAngle, DateTime FROM GPS WHERE GpsID=" + Found_GpsID + " LIMIT 1";
            Cursor gpsdata = db.rawQuery(gpssql, null);
            if (gpsdata.moveToFirst()) {
            	
            	Double Found_Latitude = gpsdata.getDouble(gpsdata.getColumnIndex("Latitude"));
            	Double Found_Longitude = gpsdata.getDouble(gpsdata.getColumnIndex("Longitude"));
            	Integer Found_NumOfSats = gpsdata.getInt(gpsdata.getColumnIndex("NumOfSats"));
            	Double Found_Alt = gpsdata.getDouble(gpsdata.getColumnIndex("Alt"));
            	float Found_Speed = gpsdata.getFloat(gpsdata.getColumnIndex("Speed"));
            	float Found_TrackAngle = gpsdata.getFloat(gpsdata.getColumnIndex("TrackAngle"));
            	String Found_DateTime = gpsdata.getString(gpsdata.getColumnIndex("DateTime"));
            	
                String apsql = "SELECT BSSID, SSID, CHAN, AUTH, ENCR, SECTYPE, NETTYPE, RADTYPE, LABEL FROM AP WHERE ApID=" + Found_ApID + " LIMIT 1";
                Cursor apdata = db.rawQuery(apsql, null);
                
                if (apdata.moveToFirst()) { 
                	String Found_BSSID = apdata.getString(apdata.getColumnIndex("BSSID"));
                	String Found_SSID = apdata.getString(apdata.getColumnIndex("SSID"));
                	Integer Found_CHAN = apdata.getInt(apdata.getColumnIndex("CHAN"));
                	String Found_AUTH = apdata.getString(apdata.getColumnIndex("AUTH"));
                	String Found_ENCR = apdata.getString(apdata.getColumnIndex("ENCR"));
                	Integer Found_SECTYPE = apdata.getInt(apdata.getColumnIndex("SECTYPE"));
                	String Found_NETTYPE = apdata.getString(apdata.getColumnIndex("NETTYPE"));
                	String Found_RADTYPE = apdata.getString(apdata.getColumnIndex("RADTYPE"));
                	String Found_LABEL = apdata.getString(apdata.getColumnIndex("LABEL"));
                	
                	WifiDB.postLiveData(WifiDbApiURL, WifiDbApiUser, WifiDbApiPass, Found_SSID, Found_BSSID, Found_RADTYPE, Found_AUTH, Found_ENCR, Found_LABEL, Found_NETTYPE, Found_LABEL, Found_SECTYPE, Found_CHAN, Found_Signal, Found_RSSI, Found_Latitude, Found_Longitude, Found_NumOfSats, Found_Alt, Found_Speed, Found_TrackAngle, Found_DateTime);
                	
                	sql = "UPDATE Hist SET UploadCode=1";
    				db.execSQL(sql);
                }
            }
        }
        db.close();
		return uploaded;
    }
}