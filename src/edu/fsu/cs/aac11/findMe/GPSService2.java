package edu.fsu.cs.aac11.findMe;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class GPSService2 extends Service {

	Handler mMainThreadHandler = null;
	String finalMessage = "Hi, ";
	String theAddress;
	double lat, lon;
	String[] numbers;
	Timer myTimer = new Timer();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
	
		Log.i("GPSSERVICE", "This part is working");
		// TODO Auto-generated method stub
		Bundle b = new Bundle();
  	  	b = intent.getExtras();
  	  	//String[] numbers = b.getStringArray("phones");
  	  	numbers = b.getStringArray("phones");
  	  	finalMessage = b.getString("finalMessage");
		
//  	  myTimer = new Timer();
//      myTimer.schedule(new TimerTask() {          
//          @Override
//          public void run() {
//        	  
        	  getLocation();
//          }           
//      }, 0, 15000); //30000 = 15 Seconds Interval.
//  	  	
  	  return Service.START_STICKY;
	}	
	
	
	public void getLocation() {
		boolean foundLocation = false;
	    // Get the location manager
	    LocationManager locationManager = (LocationManager) 
	            getSystemService(LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    String bestProvider = locationManager.getBestProvider(criteria, false);
	    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    LocationListener loc_listener = new LocationListener() {

	        public void onLocationChanged(Location l) {}

	        public void onProviderEnabled(String p) {}

	        public void onProviderDisabled(String p) {}

	        public void onStatusChanged(String p, int status, Bundle extras) {}
	    };
	    locationManager
	            .requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc_listener);
	    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    try {
	        lat = location.getLatitude();
	        lon = location.getLongitude();
	        foundLocation = true;
	        
	    } catch (NullPointerException e) {
	        lat = -1.0;
	        lon = -1.0;
	        foundLocation = false;
	    }
	    Log.i("GPSSERVICE from new function", "Latitude is: " + String.valueOf(lat) +"Longitud is: " + String.valueOf(lon));
	    
	    
	    			Geocoder geo = new Geocoder(getApplicationContext());
	    		    List<Address> addresses = null;
	    		    		try {
	    						addresses = geo.getFromLocation(lat, 
	    								lon, 1);
	    						foundLocation = true;
	    						
	    					} catch (IOException e) {
	    						// TODO Auto-generated catch block
	    						e.printStackTrace();
	    						foundLocation = false;
	    					}  
	    			if (foundLocation == true)
	    			{
	    				Log.i("giving the address with geolocation", "location is: " + addresses.get(0).getAddressLine(0) + ", "+
	    				addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea() + " " + addresses.get(0).getPostalCode()+ ", " + addresses.get(0).getCountryName() );
	    				finalMessage = finalMessage +  "-My current address is" + addresses.get(0).getAddressLine(0) + ", "+
	    				addresses.get(0).getLocality() + " " + addresses.get(0).getAdminArea() + " " + addresses.get(0).getPostalCode()+ ", " + addresses.get(0).getCountryName();
	
	    				Log.i("GPSSERVICE", finalMessage);
	    				stopSelf();
	    			}
	    			else
	    			{
	    				finalMessage = finalMessage + "My current human readablelocation is currently unavailable, but my estimate coordinates is "
	    				+ "Latitude is: " + String.valueOf(lat) +"Longitud is: " + String.valueOf(lon);
	    				Log.i("GPSSERVICE", finalMessage);
	    				stopSelf();
	    			}
	    
	    
	}
	
	public void onDestroy() {
		
		SmsManager smsManager1 = SmsManager.getDefault();
        for (int i = 0 ; i < numbers.length ; i++)
        {
        	Log.i("GPSSERVICE", numbers[i]);
       	 //smsManager1.sendTextMessage(numbers[i],null, finalMessage ,null, null);
        }
        super.onDestroy();
        Log.i("GPSSERVICE", "Super on Destroy is working");
        stopSelf();
	}

	
}
