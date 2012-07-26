package service;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

public class intentBroadcast extends BroadcastReceiver {
	private final String STATE = "state";
	private Intent i;
	private DBAdapter database;

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			database = new DBAdapter(context);
			database.open();
			Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
			c.moveToFirst();
			if (c.getCount() == 1 && c.getString(0).equals("true")) {
				context.startService(new Intent(context, detectJack.class));
			}
			database.close();
		} else {
			Bundle b = intent.getExtras();
			if (b.getInt(STATE) == 1) {
				Log.e("kepano", "head phones attached");
				Intent i;
				i = new Intent("com.android.music.musicservicecommand");
				i.putExtra("command", "play");
				context.getApplicationContext().sendBroadcast(i);
				// context.sendBroadcast(i);
			}
		}
	}
}
