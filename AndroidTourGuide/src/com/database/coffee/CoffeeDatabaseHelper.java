package com.database.coffee;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CoffeeDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Coffee";

	private static final int DATABASE_VERSION = 1;

	public CoffeeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// method is used when a database is being created
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		CoffeeTable.onCreate(database);
	}

	
	//used if database is altered or upgraded
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		CoffeeTable.onUpgrade(database, oldVersion, newVersion);
	}
}
