package net.kepano.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	private Context context;
	private SQLiteDatabase db;
	private DataHandler dbHandler;

	public DBAdapter(Context context) {
		this.context = context;
	}

	public DBAdapter open() throws SQLException {
		dbHandler = new DataHandler(context);
		db = dbHandler.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHandler.close();
	}

	public void updateOption(String option, String value) {
		String sql = "UPDATE " + DataHandler.TABLE_NAME + " SET "
				+ DataHandler.TABLE_COL_VALUE + "='" + value + "' WHERE "
				+ DataHandler.TABLE_COL_OPTION + "='" + option + "'";
		db.execSQL(sql);
	}

	public boolean deleteValues() {
		return true;
	}

	public void insertData(String option, String value) {
		String sql = "INSERT INTO " + DataHandler.TABLE_NAME + "("
				+ DataHandler.TABLE_COL_OPTION + ", "
				+ DataHandler.TABLE_COL_VALUE + ") VALUES ('" + option + "', "
				+ value + ");";
		db.execSQL(sql);
	}

	public void clearTable() {
		db.execSQL("DELETE FROM locks;");
	}

	public Cursor getOption(String option) {
		return db.query(DataHandler.TABLE_NAME,
				new String[] { DataHandler.TABLE_COL_VALUE },
				DataHandler.TABLE_COL_OPTION + "='" + option + "'", null, null,
				null, null);
	}
}
