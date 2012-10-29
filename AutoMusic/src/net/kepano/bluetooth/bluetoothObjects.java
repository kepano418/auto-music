package net.kepano.bluetooth;

import android.bluetooth.BluetoothDevice;

public class bluetoothObjects {
	private String name;
	private String addr;
	private int state;
	private boolean autoStart = false;
	
	public bluetoothObjects(BluetoothDevice bt){
		this.name = bt.getName();
		this.addr = bt.getAddress();
		this.state = bt.getBondState();
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getAddr(){
		return this.addr;
	}
	
	public int getState(){
		return state;
	}
	
	public boolean getAutoStart(){
		return autoStart;
	}
	
	public void setAutoStart(boolean value){
		this.autoStart = value;
	}
}
