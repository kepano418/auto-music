package service;
/*
* SMILParser
*
* Version 1.0
*
* Programmers: Jesse Louderback
*/


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class detectJack extends Service {
	private static final String ACTION = "android.intent.action.HEADSET_PLUG"; 
	private intentBroadcast listen;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		NotificationManager notificationMgr = 
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final IntentFilter theFilter = new IntentFilter(); 
        theFilter.addAction(ACTION);
        listen = new intentBroadcast();
		//listen.setNotificationMrg(notice);
		//listen.setContext();
        this.registerReceiver(listen, theFilter);
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

