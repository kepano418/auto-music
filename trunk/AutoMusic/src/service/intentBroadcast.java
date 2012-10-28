package service;

import java.util.Set;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
			Bundle b = intent.getExtras();
			Intent i;
			if (detectJack.ACTION_HANDSET.equals(intent.getAction())) {
				Cursor c = database.getOption(DataHandler.OPTION_WIRED);
				c.moveToFirst();
				if (c.getCount() == 1
						&& c.getString(
								c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					if (b.getInt(HANDSET_STATE) == 1) {
						Log.e(TAG, "head phones attached");
						i = new Intent("com.android.music.musicservicecommand");
						i.putExtra("command", "play");
						context.getApplicationContext().sendBroadcast(i);
						// context.sendBroadcast(i);
					}
				}

			} else if (detectJack.ACTION_BLUETOOTH.equals(intent.getAction())) {
				Cursor c = database.getOption(DataHandler.OPTION_BT);
				c.moveToFirst();
				if (c.getCount() == 1
						&& c.getString(
								c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
								.equals("true")) {
					//TODO Need another test for the BT Device itself
					BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
							.getDefaultAdapter();
					if (mBluetoothAdapter != null) {
						Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
								.getBondedDevices();
						if (pairedDevices.size() != 0) {
							// the device has paired devices
							for (BluetoothDevice device : pairedDevices) {
								Log.i(TAG, "device name: " + device.getName());
								Log.i(TAG,
										"device address: "
												+ device.getAddress());
							}
						} else {
							// no paired devices
							Log.i(TAG, "no paired devices");
						}
					}

				}
			}

		}
		database.close();
	}

}
