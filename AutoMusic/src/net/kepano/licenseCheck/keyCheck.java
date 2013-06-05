package net.kepano.licenseCheck;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class keyCheck {
	
	public boolean hasLicenseKey(Context context){
		try{
			PackageManager manager = context.getPackageManager();
			PackageInfo appInfo = manager.getPackageInfo("YOUR_PACKAGE_NAME", PackageManager.GET_SIGNATURES);
			
			//now test if the first signature equials your debug key.
			boolean shouldUseTestServer = appInfo.signatures[0].toCharsString().equals("YOUR_DEBUG_KEY");
			return shouldUseTestServer;
		} catch (NameNotFoundException e){
			
		}
		return false;
	}

}
