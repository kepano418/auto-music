package net.kepano.automusic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.kepano.bluetooth.bluetoothObjects;
import net.kepano.customlist.CustomListAdapter;
import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import service.detectJack;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

public class AutoMusicActivity extends Activity {

	private ListView lv;
	
	private DBAdapter database;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		database = new DBAdapter(this);
		database.open();

		initServiceToggleBox();
		initOnBootCheckBox();
		initWiredCheckBox();
		initBTCheckBox();
		setBluetoothList();
	}

	@Override
	protected void onDestroy() {
		// stopService(new Intent(this, service.detectJack.class));
		database.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		((ToggleButton) findViewById(R.id.serviceToggle)).setChecked(isServiceRunning());
		super.onResume();
	}

	private boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (detectJack.class.getName().equals(
					service.service.getClassName())) {

				return true;
			}
		}
		return false;
	}

	private void serviceControl(boolean newState) {
		if (newState) {
			// new state is on so we turn it off
			startService(new Intent(this, detectJack.class));
		} else {
			// new state is off and we want it on
			stopService(new Intent(this, detectJack.class));
		}
	}

	private void initServiceToggleBox() {
		ToggleButton tb = (ToggleButton) findViewById(R.id.serviceToggle);
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				serviceControl(((ToggleButton) v).isChecked());
			}
		});
		tb.setChecked(isServiceRunning());
	}

	private void initOnBootCheckBox() {
		ToggleButton tb = (ToggleButton) findViewById(R.id.checkBox_OnBoot);

		Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		else if(c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME, DataHandler.OPTION_START_ON_BOOT, "false");
		
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean stats = ((ToggleButton) v).isChecked();
				if (stats) {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_START_ON_BOOT,
							"true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_START_ON_BOOT,
							"false");
				}

			}
		});
	}
	
	private void initWiredCheckBox(){
		ToggleButton tb = (ToggleButton) findViewById(R.id.toggleWired);

		Cursor c = database.getOption(DataHandler.OPTION_WIRED);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		else if(c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME, DataHandler.OPTION_WIRED, "false");			

		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean stats = ((ToggleButton) v).isChecked();
				if (stats) {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_WIRED,
							"true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_WIRED,
							"false");
				}

			}
		});
	}
	
	private void initBTCheckBox(){
		ToggleButton tb = (ToggleButton) findViewById(R.id.toggleBluetooth);

		Cursor c = database.getOption(DataHandler.OPTION_BT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		else if(c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME, DataHandler.OPTION_BT, "false");	
		
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_BT,
							"true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME, DataHandler.OPTION_BT,
							"false");
				}

			}
		});
	}

	
	private void setBluetoothList(){
		lv = (ListView) findViewById(R.id.bluetoothList);
		
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

		ArrayList<bluetoothObjects> s = new ArrayList<bluetoothObjects>();
		for(BluetoothDevice bt : pairedDevices)
		   s.add(new bluetoothObjects(bt));

		lv.setAdapter(new CustomListAdapter(this, android.R.layout.simple_list_item_1,  s));

	}
	
}