package service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

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
			if (detectJack.ACTION_HANDSET.equals(intent.getAction())) {
				Cursor c = database.getOption(DataHandler.OPTION_WIRED);
				c.moveToFirst();
				if (c.getCount() == 1
						&& c.getString(
								c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					Bundle b = intent.getExtras();
					if (b.getInt(HANDSET_STATE) == 1) {

						Log.e(TAG, "head phones attached");

						sendIt(context);

						/*
						 * try { synchronized (this) { this.wait(3000); } if
						 * (!detectIfRunning(context))
						 * context.getApplicationContext() .sendBroadcast(i); }
						 * catch (InterruptedException e) { e.printStackTrace();
						 * }
						 */
					}
				}

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

						sendIt(context);

						// context.getApplicationContext().sendBroadcast(i);
						try {
							synchronized (this) {
								this.wait(3000);
							}
							if (!detectIfRunning(context))
								sendIt(context);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}

			} else if (detectJack.ACTION_ICS_BLUETOOTH.equals(intent
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
						Log.e(TAG, "head phones attached");

						sendIt(context);

						// context.getApplicationContext().sendBroadcast(i);
						try {
							synchronized (this) {
								this.wait(3000);
							}
							if (!detectIfRunning(context))
								sendIt(context);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
			}
		}
		database.close();
	}

	private boolean detectIfRunning(Context c) {
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
	}

	private void sendIt(final Context context) {
//		 Intent i;
//		 i = new Intent("com.android.music.musicservicecommand");
//		 i.putExtra("command", "play");
//		
//		 Log.e(TAG, "sending command 'Play'");
//		 context.getApplicationContext().sendBroadcast(i);
//		 
		new Thread(new Runnable() {
		    public void run() {
		        try {
		        	 AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);    

		 		    while (!mAudioManager.isMusicActive()) {

		 		    Intent i = new Intent("com.android.music.musicservicecommand");

		 		    i.putExtra("command", "play");
		 		    Log.e(TAG, "sending command 'Play'");
		 		    context.getApplicationContext().sendBroadcast(i);
		 		    Log.e(TAG, "'Play' sent");
		 		    Thread.sleep(3000);
		 		    }   
		            
		        } catch (InterruptedException e) {
		            e.printStackTrace();
		        }
		       
		    }
		}).start();
	}

}
