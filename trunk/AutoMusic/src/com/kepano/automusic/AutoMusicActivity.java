package com.kepano.automusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AutoMusicActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		startService(new Intent(this, service.detectJack.class));
        setContentView(R.layout.main);
    }

	@Override
	protected void onDestroy() {
		//stopService(new Intent(this, service.detectJack.class));
		super.onDestroy();
	}
    
    
}