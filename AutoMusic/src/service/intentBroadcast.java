package service;

import java.util.List;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import net.kepano.tools.tools;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;

public class intentBroadcast extends BroadcastReceiver {
	private final String TAG = "kepano";
	private final String HANDSET_STATE = "state";
	private final String BLUETOOTH_PREV_STATE = "android.bluetooth.headset.extra.PREVIOUS_STATE";
	private final String BLUETOOTH_DEVICE = "android.bluetooth.device.extra.DEVICE";
	private final String BLUETOOTH_STATE = "android.bluetooth.headset.extra.STATE";
	private DBAdapter database;

	@Override
	public void onReceive(Context context, Intent intent) {
		database = new DBAdapter(context);
		database.open();
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
			c.moveToFirst();
			if (c.getCount() == 1
					&& c.getString(
							c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
							.equals("true"))
				context.startService(new Intent(context, detectJack.class));

			database.close();
		} else {
			// ///////////////////////////////////////////
			// Below is the Wired portion of detection
			// PRE ICS and ICS
			// ///////////////////////////////////////////
			if (detectJack.ACTION_HANDSET.equals(intent.getAction())) {
				Cursor c1 = database.getOption(DataHandler.OPTION_WIRED);
				c1.moveToFirst();
				Cursor c2 = database.getOption(DataHandler.OPTION_WIRED);
				c2.moveToFirst();
				if (c1.getCount() == 1
						&& c1.getString(
								c1.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					Bundle b = intent.getExtras();
					if (b.getInt(HANDSET_STATE) == 1) {

						Log.e(TAG, "head phones attached");

						sendStartMusic(context);
					}
				} else if (c2.getCount() == 0
						&& c2.getString(
								c2.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					sendPauseMusic(context);

				}

				// ///////////////////////////////////////////
				// Below is the Bluetooth portion of detection
				// PRE ICS
				// ///////////////////////////////////////////
			} else if (detectJack.ACTION_BLUETOOTH.equals(intent.getAction())) {
				Cursor c = database.getOption(DataHandler.OPTION_BT);
				c.moveToFirst();
				if (c.getCount() == 1
						&& c.getString(
								c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					Bundle b = intent.getExtras();
					Object o = b.get(BLUETOOTH_DEVICE);
					c = database.getBT(o.toString());
					c.moveToFirst();
					if (b.getInt(BLUETOOTH_STATE) == 2
							&& c.getCount() == 1
							&& c.getString(
									c.getColumnIndex(DataHandler.TABLE_COL_B_ADDR))
									.equals(o.toString())) {
						Log.e(TAG, "head phones attached");

						sendStartMusic(context);

					}
				}

				// ///////////////////////////////////////////
				// Below is the Bluetooth portion of detection
				// ICS
				// ///////////////////////////////////////////
			} 
			
			if (tools.isVersionICSorBetter() && detectJack.ACTION_ICS_BLUETOOTH.equals(intent
					.getAction())) {
				Cursor c = database.getOption(DataHandler.OPTION_BT);
				c.moveToFirst();
				if (c.getCount() == 1
						&& c.getString(
								c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					Bundle b = intent.getExtras();
					Object o = b.get(BLUETOOTH_DEVICE);
					c = database.getBT(o.toString());
					c.moveToFirst();
					if (c.getCount() == 1
							&& c.getString(
									c.getColumnIndex(DataHandler.TABLE_COL_B_ADDR))
									.equals(o.toString())) {
						Log.e(TAG, "head phones disconected");

						sendStartMusic(context);

					}
				}
			}
		}
		database.close();
	}

	/*private boolean detectIfRunning(Context c) {
		ActivityManager activityManager = (ActivityManager) c
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> procInfos = activityManager
				.getRunningAppProcesses();
		for (int i = 0; i < procInfos.size(); i++) {
			if (procInfos.get(i).processName.equals("com.android.music")) {
				return true;
			}
		}
		return false;
	}*/

	private void sendStartMusic(final Context context) {
		new Thread(new Runnable() {
			public void run() {
				try {
					AudioManager mAudioManager = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);

					while (!mAudioManager.isMusicActive()) {

						Intent i = new Intent(
								"com.android.music.musicservicecommand");

						i.putExtra("command", "play");
						Log.e(TAG, "sending command 'Play'");
						context.getApplicationContext().sendBroadcast(i);
						Log.e(TAG, "'Play' sent");
						Thread.sleep(5000);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void sendPauseMusic(final Context context) {
		Intent i = new Intent("com.android.music.musicservicecommand");

		i.putExtra("command", "pause");
		Log.e(TAG, "sending command 'Pause'");
		context.getApplicationContext().sendBroadcast(i);
	}

}
