package net.kepano.automusic;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import service.detectJack;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class AutoMusicActivity extends FragmentActivity  {

	private static DBAdapter database;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DBAdapter(this);
		database.open();
		
		setContentView(R.layout.main);
		



	}

	@Override
	protected void onDestroy() {
		// stopService(new Intent(this, service.detectJack.class));
		database.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		//((ToggleButton) findViewById(R.id.serviceToggle))
		//		.setChecked(isServiceRunning());
		super.onResume();
	}

	public boolean isServiceRunning() {
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

	public void serviceControl(boolean newState) {
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

	
	public static DBAdapter getDB(){
		return database;
	}
	
	
	/*private void initTestCase() {
		Button tb = (Button) findViewById(R.id.testButton);


		//on click change choice
		tb.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				keyCheck kc = new keyCheck();
				Toast.makeText(getApplicationContext(), kc.isProInstalled(getApplicationContext()) + "", Toast.LENGTH_SHORT).show();
			}
		});
	}*/

}