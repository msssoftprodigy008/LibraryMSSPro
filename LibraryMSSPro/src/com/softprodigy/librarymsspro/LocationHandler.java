package com.softprodigy.librarymsspro;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHandler {

	
	
	public class LocationInfo{
//		public int lat=0;
//		public int lon=0;
		public Location location;
//		public final int GPS_PROVIDER=1;
//		public final int NETWORK_PROVIDER=2;
		
		public boolean networkProviderEnabled;
		public boolean GPSProviderEnabled;
	}
	
	
	
	public LocationHandler(Context context,LocationResultListener locationResultListener)
	{
		getLocation(context, locationResultListener);
	}
	
	
	
	
	//////////////////location handler
	
	Timer timer1;
	LocationManager lm;
	LocationResultListener locationResult;
	LocationInfo mLocationInfo=new LocationInfo();
//	boolean gps_enabled=false;
//	boolean network_enabled=false;
//public Location net_loc=null, gps_loc=null;
	private boolean getLocation(Context context, LocationResultListener result)
	{
		//I use LocationResult callback class to pass location value from MyLocation to user code.
		locationResult=result;
		if(lm==null)
			lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		//exceptions will be thrown if provider is not permitted.
		try{mLocationInfo.GPSProviderEnabled=lm.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
		try{mLocationInfo.networkProviderEnabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}

		//don't start listeners if no provider is enabled
		if(!mLocationInfo.GPSProviderEnabled && !mLocationInfo.networkProviderEnabled)
			return false;
		if(mLocationInfo.GPSProviderEnabled)
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
		if(mLocationInfo.networkProviderEnabled)
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
		timer1=new Timer();
		timer1.schedule(new GetLastLocation(), 0);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener()
	{
		public void onLocationChanged(Location location) {
			timer1.cancel();
			mLocationInfo.location=location;
			locationResult.gotLocation(mLocationInfo);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerNetwork);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			timer1.cancel();
			mLocationInfo.location=location;
			locationResult.gotLocation(mLocationInfo);
			lm.removeUpdates(this);
			lm.removeUpdates(locationListenerGps);
		}
		public void onProviderDisabled(String provider) {}
		public void onProviderEnabled(String provider) {}
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
	

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {
			lm.removeUpdates(locationListenerGps);
			lm.removeUpdates(locationListenerNetwork);

			Location gps_loc=null,net_loc=null;
			if(mLocationInfo.GPSProviderEnabled)
				gps_loc=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(mLocationInfo.networkProviderEnabled)
				net_loc=lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
System.out.println("NETWORK ENABLED OR NOT ..."+net_loc);
			//if there are both values use the latest one
			if(gps_loc!=null && net_loc!=null){
				System.out.println("*******gps_loc.getTime()>net_loc.getTime()************"+(gps_loc.getTime()>net_loc.getTime())); 
				if(gps_loc.getTime()>net_loc.getTime())
				{
					mLocationInfo.location=gps_loc;
					locationResult.gotLocation(mLocationInfo);
				}
				else
				{
					mLocationInfo.location=net_loc;
					locationResult.gotLocation(mLocationInfo);
				}
				return;
			}

			if(gps_loc!=null){
				mLocationInfo.location=gps_loc;
				locationResult.gotLocation(mLocationInfo);
//				locationResult.gotLocation(gps_loc);
				return;
			}
			if(net_loc!=null){
					mLocationInfo.location=net_loc;
					locationResult.gotLocation(mLocationInfo);
				
				return;
			}
			locationResult.gotLocation(null);
		}
	}

	public static interface LocationResultListener{
		public abstract void gotLocation(LocationInfo locationInfo);
	}
}
