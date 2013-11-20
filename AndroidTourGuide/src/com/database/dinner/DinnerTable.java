package com.database.dinner;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DinnerTable {
	
	//Create the database coffee
	
	private static final String DATABASE_CREATE = "create table Dinner "
			+ "(_id integer primary key autoincrement, "
			+ "title text not null, " + "address text not null, "
			+ "postcode text not null, " + "phone text not null, "
			+ "web text not null, " + "distance text not null, "
			+ "cuisine text not null, " + "bookable text not null, "
			+ "rating text not null );";

	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DinnerTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS Dinner");
		onCreate(database);
	}
}
