package com.database.dinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DinnerDbAdapter {

	// Defining the database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_POSTCODE = "postcode";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_WEB = "web";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_CUISINE = "cuisine";
	public static final String KEY_BOOKABLE = "bookable";
	public static final String KEY_RATING = "rating";
	private static final String DB_TABLE = "Dinner";
	private Context context;
	private SQLiteDatabase db;
	private DinnerDatabaseHelper dbHelper;

	public DinnerDbAdapter(Context context) {
		this.context = context;
	}

	public DinnerDbAdapter open() throws SQLException {
		dbHelper = new DinnerDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	
	//create a new entry in the table dinner
	
	public long createDinner(String title, String address, String postcode, String phone, 
						    String web, String distance, String cuisine, String bookable, String rating) {
		ContentValues values = createContentValues(title, address, postcode, phone, web,
												   distance, cuisine, bookable, rating);

		return db.insert(DB_TABLE, null, values);
	}

	
	//update an entry in the table dinner

	public boolean updateDinner(long rowId, String title, String address, String postcode, String phone,
			                  String web, String distance, String cuisine, String bookable, String rating) {
		ContentValues values = createContentValues(title, address, postcode, phone, web,
												   distance, cuisine, bookable, rating);

		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}

	
	//delete an entry in the table dinner

	public boolean deleteDinner(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	
	//return a cursor over all items in dinner

	public Cursor fetchAllDinner() {
		return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_ADDRESS, KEY_POSTCODE,
				KEY_PHONE, KEY_WEB, KEY_DISTANCE, KEY_CUISINE, KEY_BOOKABLE, KEY_RATING }, 
				null, null, null, null, null);
	}

	
	//return a cursor at a specific dinner

	public Cursor fetchDinner(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, 
				KEY_ADDRESS, KEY_POSTCODE, KEY_PHONE, KEY_WEB, KEY_DISTANCE, KEY_CUISINE,
				KEY_BOOKABLE, KEY_RATING }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	//create the content values for each of the entries

	private ContentValues createContentValues(String title, String address, String postcode, String phone,
							String web, String distance, String cuisine, String bookable, String rating) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_ADDRESS, address);
		values.put(KEY_POSTCODE, postcode);
		values.put(KEY_PHONE, phone);
		values.put(KEY_WEB, web);
		values.put(KEY_DISTANCE, distance);
		values.put(KEY_CUISINE, cuisine);
		values.put(KEY_BOOKABLE, bookable);
		values.put(KEY_RATING, rating);
		return values;
	}
}
