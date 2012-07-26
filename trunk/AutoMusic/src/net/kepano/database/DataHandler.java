package net.kepano.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHandler extends SQLiteOpenHelper {

	// Table and Columns
	public static final String TABLE_NAME = "auto_music";
	public static final String TABLE_COL_OPTION = "option";
	public static final String TABLE_COL_VALUE = "value";

	// Options
	public static final String OPTION_START_ON_BOOT = "onBoot";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "auto_music_app";

	// database creation
	private static final String DATABASE_CREATE = "CREATE TABLE auto_music ("
			+ TABLE_COL_OPTION + " text primary key, " + TABLE_COL_VALUE
			+ " text not null);";
	private static final String DATABASE_ON_CREATE = "INSERT INTO "
			+ TABLE_NAME + " ('" + TABLE_COL_OPTION + "', '" + TABLE_COL_VALUE
			+ "') VALUES ('" + OPTION_START_ON_BOOT + "', 'true')";

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_ON_CREATE);
	}

	public DataHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("kepano", "Upgrading data base from " + oldVersion + " to "
				+ newVersion + "\nWARNING this drops the table");
		// probably never use this
	}

}
