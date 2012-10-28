package net.kepano.customlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.kepano.automusic.R;
import net.kepano.bluetooth.bluetoothObjects;

;

public class CustomListAdapter extends ArrayAdapter<bluetoothObjects> {

	private ArrayList<bluetoothObjects> items;
	private Context ctx;
	 
	public CustomListAdapter(Context context, int textViewResourceId,
			ArrayList<bluetoothObjects> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.ctx = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.customlistitem, null);
		}
		bluetoothObjects o = items.get(position);
		if (o != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
			if (tt != null) {
				tt.setText("Name: " + o.getName());
			}
			if (bt != null) {
				bt.setText("Addr: " + o.getAddr());
			}
		}
		return v;
	}

}
