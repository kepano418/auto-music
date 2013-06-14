package net.kepano.tools;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;

public class tools {
	public static int get_screen_width(Context ctx) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		return metrics.widthPixels;
	}

	public static boolean isVersionICSorBetter() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;

		//if API is ICS
		if (currentapiVersion >= 14) {
			return true;
		} else {
			return false;
		}

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
