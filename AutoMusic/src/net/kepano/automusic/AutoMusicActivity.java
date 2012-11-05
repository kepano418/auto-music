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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
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

		//Initialize the data
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
		((ToggleButton) findViewById(R.id.serviceToggle))
				.setChecked(isServiceRunning());
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
		
		//set click to enable or disable service
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				serviceControl(((ToggleButton) v).isChecked());
			}
		});
		tb.setChecked(isServiceRunning());
	}

	//give the application ability to auto start service on boot
	private void initOnBootCheckBox() {
		ToggleButton tb = (ToggleButton) findViewById(R.id.checkBox_OnBoot);

		Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		else if (c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME,
					DataHandler.OPTION_START_ON_BOOT, "false");

		//gives the ability to turn auto start on boot on or off
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (((ToggleButton) v).isChecked()) {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_START_ON_BOOT, "true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_START_ON_BOOT, "false");
				}

			}
		});
	}

	//allows the user to have it work on a wired headset
	private void initWiredCheckBox() {
		ToggleButton tb = (ToggleButton) findViewById(R.id.toggleWired);

		Cursor c = database.getOption(DataHandler.OPTION_WIRED);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		
		//data is not in there
		//this is handled on first initialize
		else if (c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME,
					DataHandler.OPTION_WIRED, "false");

		//on click change choice
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean stats = ((ToggleButton) v).isChecked();
				if (stats) {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_WIRED, "true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_WIRED, "false");
				}

			}
		});
	}

	private void initBTCheckBox() {
		ToggleButton tb = (ToggleButton) findViewById(R.id.toggleBluetooth);

		Cursor c = database.getOption(DataHandler.OPTION_BT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		
		//data is not in there
		//this is handled on first initialize
		else if (c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME,
					DataHandler.OPTION_BT, "false");

		//on click change choice
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_BT, "true");
				} else {
					database.updateOption(DataHandler.TABLE_M_NAME,
							DataHandler.OPTION_BT, "false");
				}

			}
		});
	}

	private void setBluetoothList() {
		lv = (ListView) findViewById(R.id.bluetoothList);

		//gets a list of all bluetooth enabled devices
		//on the phone
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();

		ArrayList<bluetoothObjects> s = new ArrayList<bluetoothObjects>();
		for (BluetoothDevice bt : pairedDevices) {
			bluetoothObjects bto = new bluetoothObjects(bt);

			//see if bluetooth device is already
			//enabled for auto start
			Cursor c = database.getBT(bt.getAddress());
			c.moveToFirst();
			if (c.getCount() == 1
					&& bt.getAddress()
							.equals(c.getString(c
									.getColumnIndex(DataHandler.TABLE_COL_B_ADDR))))
				bto.setAutoStart(true);
			s.add(bto);
		}
		final CustomListAdapter cAdapter = new CustomListAdapter(this,
				android.R.layout.simple_list_item_1, s); 
		lv.setAdapter(cAdapter);

		//set onclick to change the row
		//also redraw the list so show change
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				bluetoothObjects o = (bluetoothObjects) lv.getItemAtPosition(position);
				if(o.getAutoStart()){
					o.setAutoStart(false);
					database.removeData(DataHandler.TABLE_BT_NAME, o.getAddr(), "");
				}
				else{
					o.setAutoStart(true);
					database.insertData(DataHandler.TABLE_BT_NAME, o.getAddr(), "");
				}
				cAdapter.notifyDataSetChanged();
			}
		});

	}

}