package net.kepano.licenseCheck;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class keyCheck {
	
	public String hasLicenseKey(Context context){
		try{
			PackageManager manager = context.getPackageManager();
			PackageInfo appInfo = manager.getPackageInfo("net.kepano.automusic.pro", PackageManager.GET_SIGNATURES);
			
			//now test if the first signature equials your debug key.
			boolean shouldUseTestServer = appInfo.signatures[0].toCharsString().equals("YOUR_DEBUG_KEY");
			return appInfo.signatures[0].toCharsString();
		} catch (NameNotFoundException e){
			
		}
		return "N/A";
	}
	public static boolean isProInstalled(Context context) {
	    PackageManager manager = context.getPackageManager();
	    if (manager.checkSignatures(context.getPackageName(), "net.kepano.automusic.pro")
	        == PackageManager.SIGNATURE_MATCH) {
	        //Pro key installed, and signatures match
	        return true;
	    }
	    return false;
	}
}
