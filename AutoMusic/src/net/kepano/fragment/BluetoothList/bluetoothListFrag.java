package net.kepano.fragment.BluetoothList;

import java.util.ArrayList;
import java.util.Set;

import net.kepano.automusic.AutoMusicActivity;
import net.kepano.automusic.R;
import net.kepano.bluetooth.bluetoothObjects;
import net.kepano.customlist.CustomListAdapter;
import net.kepano.database.DBAdapter;
import net.kepano.database.DataHandler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class bluetoothListFrag extends Fragment {
	private ListView lv;
	private static DBAdapter database;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
    	database = AutoMusicActivity.getDB();
    	
        // Inflate the layout for this fragment
    	View view = inflater.inflate(R.layout.bluetoothlistlayout, container, false);
		initBTCheckBox(view);
		setBluetoothList(view);
		return view;
    }
    
    private void initBTCheckBox(View view) {
		ToggleButton tb = (ToggleButton) view.findViewById(R.id.toggleBluetooth);

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

	private void setBluetoothList(View view) {
		lv = (ListView) view.findViewById(R.id.bluetoothList);

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
		final CustomListAdapter cAdapter = new CustomListAdapter(view.getContext(),
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

