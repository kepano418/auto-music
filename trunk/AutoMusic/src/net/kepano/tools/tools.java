package net.kepano.tools;

import android.content.Context;
import android.util.DisplayMetrics;

public class tools {
	public static int get_screen_width(Context ctx) {
		DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
		return metrics.widthPixels;
	}

}
