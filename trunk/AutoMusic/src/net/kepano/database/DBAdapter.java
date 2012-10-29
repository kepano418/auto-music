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

	public void updateOption(String table, String v1, String v2) {
		String sql = "";
		if (table.equals(DataHandler.TABLE_M_NAME))
			sql = "UPDATE " + DataHandler.TABLE_M_NAME + " SET "
					+ DataHandler.TABLE_COL_M_VALUE + "='" + v2 + "' WHERE "
					+ DataHandler.TABLE_COL_M_OPTION + "='" + v1 + "'";
		if (!sql.equals(""))
			db.execSQL(sql);
	}

	public boolean deleteValues() {
		return true;
	}

	public void insertData(String table, String v1, String v2) {
		String sql = "";
		if (table.equals(DataHandler.TABLE_M_NAME))
			sql = "INSERT INTO " + DataHandler.TABLE_M_NAME + "("
					+ DataHandler.TABLE_COL_M_OPTION + ", "
					+ DataHandler.TABLE_COL_M_VALUE + ") VALUES ('" + v1
					+ "', '" + v2 + "');";
		else if (table.equals(DataHandler.TABLE_BT_NAME))
			sql = "INSERT INTO " + DataHandler.TABLE_BT_NAME + " ("
					+ DataHandler.TABLE_COL_B_ADDR + ") VALUES ('" + v1 + "');";
		if (!sql.equals(""))
			db.execSQL(sql);
	}

	public void removeData(String table, String v1, String v2) {
		String sql = "";

		if (table.equals(DataHandler.TABLE_BT_NAME))
			sql = "DELETE FROM " + DataHandler.TABLE_BT_NAME + 
					" WHERE " + DataHandler.TABLE_COL_B_ADDR + "='" +
					v1 + "';";
		if (!sql.equals(""))
			db.execSQL(sql);
	}

	public Cursor getOption(String option) {
		return db.query(DataHandler.TABLE_M_NAME,
				new String[] { DataHandler.TABLE_COL_M_VALUE },
				DataHandler.TABLE_COL_M_OPTION + "='" + option + "'", null,
				null, null, null);
	}
	
	public Cursor getBT(String addr) {
		return db.query(DataHandler.TABLE_BT_NAME,
				new String[] { DataHandler.TABLE_COL_B_ADDR },
				DataHandler.TABLE_COL_B_ADDR + "='" + addr + "'", null,
				null, null, null);
	}
}
