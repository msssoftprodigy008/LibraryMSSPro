package com.softprodigy.librarymsspro;

import android.content.Context;
import com.google.android.gcm.GCMRegistrar;

public class GCMManager {
	
	public static String getGcmToken(Context ctx,String projectId) {
String deviceGcmId="";
		try{
		GCMRegistrar.checkDevice(ctx);
		GCMRegistrar.checkManifest(ctx);
		String regId = GCMRegistrar.getRegistrationId(ctx);

		if (regId.equals("")) {
			GCMRegistrar.register(ctx, projectId);
			regId = GCMRegistrar.getRegistrationId(ctx);
			System.out.println("if_regID" + regId);
		} else {
			System.out
					.println("****************************already registered*********"
							+ GCMRegistrar.getRegistrationId(ctx));
		}
		deviceGcmId = regId;
		if (regId.equals("")) {
			GCMRegistrar.register(ctx, projectId);
			regId = GCMRegistrar.getRegistrationId(ctx);
			System.out.println("if_regID" + regId);

		}
		System.out
				.println("****************************regId*********" + regId);
		deviceGcmId = regId;
		}catch(Exception e){
			e.printStackTrace();
		}

		// GCMRegistrar.unregister(ctx);
return deviceGcmId;
	}
	
	
}
