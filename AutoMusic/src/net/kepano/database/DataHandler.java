package net.kepano.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler extends SQLiteOpenHelper {

	// Tables and Columns
	public static final String TABLE_M_NAME = "auto_music";
	public static final String TABLE_COL_M_OPTION = "option";
	public static final String TABLE_COL_M_VALUE = "value";

	public static final String TABLE_BT_NAME = "bt";
	public static final String TABLE_COL_B_ADDR = "address";

	// Options
	public static final String OPTION_START_ON_BOOT = "onBoot";
	public static final String OPTION_WIRED = "wired";
	public static final String OPTION_BT = "bt";

	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "auto_music_app";

	// database creation
	private static final String DATABASE_CREATE_MAIN = "CREATE TABLE "
			+ TABLE_M_NAME + " (" + TABLE_COL_M_OPTION + " text primary key, "
			+ TABLE_COL_M_VALUE + " text not null);";
	private static final String DATABASE_CREATE_BT = "CREATE TABLE "
			+ TABLE_BT_NAME + " (" + TABLE_COL_B_ADDR + " text primary key);";

	// database removal
	private static final String DATABASE_DROP_MAIN = "DROP TABLE "
			+ TABLE_M_NAME;
	private static final String DATABASE_DROP_BT = "DROP TABLE "
			+ TABLE_BT_NAME;
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_MAIN);
		database.execSQL(DATABASE_CREATE_BT);
	}

	public DataHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("kepano", "Upgrading data base from " + oldVersion + " to "
				+ newVersion + "\nWARNING this drops the table");
		try {
			db.execSQL(DATABASE_DROP_MAIN);
		} catch (Exception e) {
		}
		try {
		db.execSQL(DATABASE_DROP_BT);
		} catch (Exception e) {}
		onCreate(db);
	}

}
