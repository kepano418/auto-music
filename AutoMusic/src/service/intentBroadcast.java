package service;

import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class intentBroadcast extends BroadcastReceiver {
	private final String STATE = "state";
	private Intent i;
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle b = intent.getExtras();
		if (b.getInt(STATE) == 1){
			i = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
			//i.setAction("play_l");
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}

}
