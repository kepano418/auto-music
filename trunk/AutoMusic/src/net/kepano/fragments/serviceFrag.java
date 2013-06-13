package net.kepano.fragments;

import service.detectJack;
import net.kepano.automusic.AutoMusicActivity;
import net.kepano.automusic.R;
import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ToggleButton;

public class serviceFrag extends Fragment {
	private ListView lv;
	private static DBAdapter database;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	database = AutoMusicActivity.getDB();
    	
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.servicelayout, container, false);
		initServiceToggleBox(view);
		initOnBootCheckBox(view);
		return view;
    }
    
	public boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getActivity().getSystemService(getActivity().ACTIVITY_SERVICE);
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
			getActivity().startService(new Intent(getActivity(), detectJack.class));
		} else {
			// new state is off and we want it on
			getActivity().stopService(new Intent(getActivity(), detectJack.class));
		}
	}

	private void initServiceToggleBox(View view) {
		ToggleButton tb = (ToggleButton) view.findViewById(R.id.serviceToggle);

		// set click to enable or disable service
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				serviceControl(((ToggleButton) v).isChecked());
			}
		});
		tb.setChecked(isServiceRunning());
	}

	// give the application ability to auto start service on boot
	private void initOnBootCheckBox(View view) {
		ToggleButton tb = (ToggleButton) view.findViewById(R.id.checkBox_OnBoot);

		Cursor c = database.getOption(DataHandler.OPTION_START_ON_BOOT);
		c.moveToFirst();
		if (c.getCount() == 1
				&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
						.equals("true"))
			tb.setChecked(true);
		else if (c.getCount() == 0)
			database.insertData(DataHandler.TABLE_M_NAME,
					DataHandler.OPTION_START_ON_BOOT, "false");

		// gives the ability to turn auto start on boot on or off
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
}