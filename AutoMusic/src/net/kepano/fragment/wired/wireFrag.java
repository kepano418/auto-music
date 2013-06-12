package net.kepano.fragment.wired;

import net.kepano.automusic.AutoMusicActivity;
import net.kepano.automusic.R;
import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class wireFrag extends Fragment{
	
	private static DBAdapter database;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	database = AutoMusicActivity.getDB();
    	
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.wiredlayout, container, false);
		initWiredCheckBox(view);
		return view;
    }
	
	//allows the user to have it work on a wired headset
		private void initWiredCheckBox(View view) {
			ToggleButton toggleWired = (ToggleButton) view.findViewById(R.id.toggleWired);
			ToggleButton toggleWiredOut = (ToggleButton) view.findViewById(R.id.toggleWiredOut);

			Cursor c = database.getOption(DataHandler.OPTION_WIRED);
			c.moveToFirst();
			if (c.getCount() == 1
					&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
							.equals("true"))
				toggleWired.setChecked(true);
			
			//data is not in there
			//this is handled on first initialize
			else if (c.getCount() == 0)
				database.insertData(DataHandler.TABLE_M_NAME,
						DataHandler.OPTION_WIRED, "false");

			//on click change choice
			toggleWired.setOnClickListener(new OnClickListener() {
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
			
			c = database.getOption(DataHandler.OPTION_STOP);
			c.moveToFirst();
			if (c.getCount() == 1
					&& c.getString(c.getColumnIndex(DataHandler.TABLE_COL_M_VALUE))
							.equals("true"))
				toggleWiredOut.setChecked(true);
			
			//data is not in there
			//this is handled on first initialize
			else if (c.getCount() == 0)
				database.insertData(DataHandler.TABLE_M_NAME,
						DataHandler.OPTION_STOP, "false");

			//on click change choice
			toggleWiredOut.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean stats = ((ToggleButton) v).isChecked();
					if (stats) {
						database.updateOption(DataHandler.TABLE_M_NAME,
								DataHandler.OPTION_STOP, "true");
					} else {
						database.updateOption(DataHandler.TABLE_M_NAME,
								DataHandler.OPTION_STOP, "false");
					}

				}
			});
		}

	
	
    

}
