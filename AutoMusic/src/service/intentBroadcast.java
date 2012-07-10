package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class intentBroadcast extends BroadcastReceiver {
	private final String STATE = "state";
	private Intent i;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		if (b.getInt(STATE) == 1){
			Intent i; 
			i = new Intent("com.android.music.musicservicecommand"); 
			i.putExtra("command", "play");
			context.sendBroadcast(i); 
			
			//i = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
			//i.putExtra("command", "play");
			//i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			//context.startActivity(i);
		}
	}
	
}
