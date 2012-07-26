package net.kepano.automusic;

import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import service.detectJack;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class AutoMusicActivity extends Activity {

	private ToggleButton tb;
	private CheckBox cb;

	private DBAdapter database;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		database = new DBAdapter(this);
		database.open();

		initToggleBox();
		initCheckBox();

	}

	@Override
	protected void onDestroy() {
		// stopService(new Intent(this, service.detectJack.class));
		database.close();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		tb.setChecked(isServiceRunning());
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

	private void initToggleBox() {
		tb = (ToggleButton) findViewById(R.id.serviceToggle);
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				serviceControl(tb.isChecked());
			}
		});
		tb.setChecked(isServiceRunning());
	}

	private void initCheckBox() {
		cb = (CheckBox) findViewById(R.id.checkBox_OnBoot);

		Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_VALUE))
						.equals("true"))
			cb.setChecked(true);

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					database.updateOption(DataHandler.OPTION_START_ON_BOOT,
							"true");
				} else {
					database.updateOption(DataHandler.OPTION_START_ON_BOOT,
							"false");
				}

			}
		});

	}
}