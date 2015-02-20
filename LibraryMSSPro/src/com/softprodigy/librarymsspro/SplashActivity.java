package com.softprodigy.librarymsspro;


import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {
int splahTime=5000;
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Thread splash_thread = new Thread() {
			@Override
			public void run() {
				try {
					synchronized (this) {
						wait(splahTime);
					}
				} catch (InterruptedException e) {
				} finally {
					
					onSplashStop();
				}
			}
		};

		splash_thread.start();
		
		
	}
	
	
	public void setSplashTime(int splahTime)
	{
		this.splahTime=splahTime;
	}
	
	
	public void onSplashStop()
	{
		
	}
}
