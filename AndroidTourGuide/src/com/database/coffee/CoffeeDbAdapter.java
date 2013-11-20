package com.database.coffee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CoffeeDbAdapter {

	// Defining the database fields
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_ADDRESS = "address";
	public static final String KEY_POSTCODE = "postcode";
	public static final String KEY_PHONE = "phone";
	public static final String KEY_WEB = "website";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_RATING = "rating";
	private static final String DB_TABLE = "Coffee";
	private Context context;
	private SQLiteDatabase db;
	private CoffeeDatabaseHelper dbHelper;

	public CoffeeDbAdapter(Context context) {
		this.context = context;
	}

	public CoffeeDbAdapter open() throws SQLException {
		dbHelper = new CoffeeDatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	//create a new entry in the table coffee
	
	public long createCoffee(String title, String address, String postcode, String phone, 
						   String website, String distance, String rating) {
		ContentValues values = createContentValues(title, address, postcode, phone, website, distance, rating);

		return db.insert(DB_TABLE, null, values);
	}

	
	//update an entry in the table coffee

	public boolean updateCoffee(long rowId, String title, String address, String postcode, String phone,
			                  String website, String distance, String rating) {
		ContentValues values = createContentValues(title, address, postcode, phone, website, distance, rating);

		return db.update(DB_TABLE, values, KEY_ROWID + "=" + rowId, null) > 0;
	}

	
	//delete an entry in the table coffee

	public boolean deleteCoffee(long rowId) {
		return db.delete(DB_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	
	//return a cursor over all items in coffee

	public Cursor fetchAllCoffee() {
		return db.query(DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_ADDRESS, KEY_POSTCODE,
				KEY_PHONE, KEY_WEB, KEY_DISTANCE, KEY_RATING }, 
				null, null, null, null, null);
	}

	
	//return a cursor at a specific coffee

	public Cursor fetchCoffee(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DB_TABLE, new String[] { KEY_ROWID, KEY_TITLE, 
				KEY_ADDRESS, KEY_POSTCODE, KEY_PHONE, KEY_WEB, KEY_DISTANCE, KEY_RATING}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//create the content values for each of the entries
	
	private ContentValues createContentValues(String title, String address, String postcode, String phone,
							String website, String distance, String rating) {
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_ADDRESS, address);
		values.put(KEY_POSTCODE, postcode);
		values.put(KEY_PHONE, phone);
		values.put(KEY_WEB, website);
		values.put(KEY_DISTANCE, distance);
		values.put(KEY_RATING, rating);
		return values;
	}
}
