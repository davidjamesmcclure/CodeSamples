package com.database.coffee;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CoffeeTable {
	
	//Create the database coffee
	
	private static final String DATABASE_CREATE = "create table Coffee "
			+ "(_id integer primary key autoincrement, "
			+ "title text not null, " + "address text not null, "
			+ "postcode text not null, " + "phone text not null, "
			+ "website text not null, " + "distance text not null, "
			+ "rating text not null );";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(CoffeeTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS Coffee");
		onCreate(database);
	}
}
