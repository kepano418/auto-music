package service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class detectJack extends Service {
	public static final String ACTION_HANDSET = "android.intent.action.HEADSET_PLUG";
	public static final String ACTION_BLUETOOTH = "android.bluetooth.headset.action.STATE_CHANGED";
	
	private intentBroadcast listen;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	} 

	@Override
	public void onCreate() {
		final IntentFilter theFilter1 = new IntentFilter(); 
		final IntentFilter theFilter2 = new IntentFilter(); 
        theFilter1.addAction(ACTION_HANDSET);
        theFilter2.addAction(ACTION_BLUETOOTH);
        listen = new intentBroadcast();
        this.registerReceiver(listen, theFilter1);
        this.registerReceiver(listen, theFilter2);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		this.unregisterReceiver(listen);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {}
}

