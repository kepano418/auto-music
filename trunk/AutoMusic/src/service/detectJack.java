package service;

import net.kepano.tools.tools;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class detectJack extends Service {
	public static final String ACTION_HANDSET = "android.intent.action.HEADSET_PLUG";
	public static final String ACTION_BLUETOOTH = "android.bluetooth.headset.action.STATE_CHANGED";
	public static final String ACTION_ICS_BLUETOOTH = "android.bluetooth.device.action.ACL_CONNECTED";

	private intentBroadcast listen;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		listen = new intentBroadcast();

		if (tools.isVersionICSorBetter())
			this.registerReceiver(listen,
					new IntentFilter(ACTION_ICS_BLUETOOTH));
		else
			this.registerReceiver(listen, new IntentFilter(ACTION_BLUETOOTH));
		this.registerReceiver(listen, new IntentFilter(ACTION_HANDSET));

		super.onCreate();
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(listen);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {
	}
}
