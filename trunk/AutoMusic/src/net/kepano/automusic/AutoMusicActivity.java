package net.kepano.automusic;

import service.detectJack;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class AutoMusicActivity extends Activity {
	static ToggleButton tb;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// startService(new Intent(this, detectJack.class));
		setContentView(R.layout.main);

		tb = (ToggleButton) findViewById(R.id.serviceToggle);
		tb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				serviceControl(tb.isChecked());
			}
		});

		tb.setChecked(isServiceRunning());
	}

	@Override
	protected void onDestroy() {
		//stopService(new Intent(this, service.detectJack.class));
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
}