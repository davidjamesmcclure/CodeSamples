package com.menus.app;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.database.dinner.DinnerDbAdapter;

public class DinnerTab extends ListActivity {
	
	private DinnerDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int VIEW_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;
	
	//declare a tag to be used on log statements
	
	String TAG = "DinnerTab";
	
	//creates the listview of entries from the database
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_lunch_layout);
        this.getListView().setDividerHeight(2);
        
        //checks if a database exists, if it doesn't then it will populate the table with the values below
        //this prevents the original values from being constantly inserted into the table and if the table
        //is emptied by the user allows it to stay empty 
        
        if(checkDataBase() == false){
			dbHelper = new DinnerDbAdapter(this);
			dbHelper.open();
			
			//these are the values originally used in the table
			
			dbHelper.createDinner("The Witchery", "The Royal Mile", "EH1 2NF", "0131 225 5613", "www.thewitchery.com", "15 minutes", "scottish", "yes", "8" );
			dbHelper.createDinner("Khushi's Diner", "32b West Nicolson Street", "EH8 9DD", "0131 667 4871",  "www.khushisdiner.com", "5 minutes", "indian", "yes", "7");
			dbHelper.createDinner("The Olive Branch", "44-46 George IV Bridge", "EH1 3RX", "0131 226 4155", "www.theolivebranch.co.uk", "10 minutes", "mediterranean", "yes", "9");
			dbHelper.createDinner("Mamma's American Pizza Company", "30 Grassmarket", "EH1 NJU", "0131 225 6464", "www.mammas.co.uk", "15 minutes", "pizza", "no", "7");
			dbHelper.createDinner("Los Argentinos", "28 West Preston Street", "EH8 9PZ", "0131 668 3111", "www.losargentinossteakhouseinedinburgh.co.uk", "15 minutes", "Argentinean", "yes", "8");
		}else{
			dbHelper = new DinnerDbAdapter(this);
			dbHelper.open();
		}
		fillData();
		registerForContextMenu(getListView());
    }
    
    //this method checks to see if a database exists with the name used
    
    protected boolean checkDataBase() {
		String DB_PATH = "/data/data/com.menus.app/databases/Dinner";
		SQLiteDatabase checkDB = null;
		try {
			
		checkDB = SQLiteDatabase.openDatabase(DB_PATH, null,
		SQLiteDatabase.OPEN_READONLY);
		checkDB.close();
		
		} catch (SQLiteException e) {
			
		}

		return checkDB != null ? true : false;
		
	} 
    // Initialise Menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createDinner();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createDinner();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dbHelper.deleteDinner(info.id);
			fillData();
			return true;
		case VIEW_ID:
			return true;
		}
		return super.onContextItemSelected(item);
	}
    
    private void createDinner() {
		Intent i = new Intent(this, DinnerDetail.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, DinnerDetail.class);
		i.putExtra(DinnerDbAdapter.KEY_ROWID, id);
		// Activity returns an result if called with startActivityForResult

		startActivityForResult(i, ACTIVITY_EDIT);
	}
    
    // Called with the result of the other activity
 	// requestCode was the origin request code send to the activity
 	// resultCode is the return code, 0 is everything is ok
 	// intend can be used to get data
 	@Override
 	protected void onActivityResult(int requestCode, int resultCode,
 			Intent intent) {
 		super.onActivityResult(requestCode, resultCode, intent);
 		fillData();

 	}
    
 	//fills the data that was stored in the KEY values into the row objects
 	
 	private void fillData() {
		cursor = dbHelper.fetchAllDinner();
		startManagingCursor(cursor);

		String[] from = new String[] { DinnerDbAdapter.KEY_TITLE, DinnerDbAdapter.KEY_ADDRESS,
							           DinnerDbAdapter.KEY_POSTCODE, DinnerDbAdapter.KEY_PHONE,
							           DinnerDbAdapter.KEY_DISTANCE, DinnerDbAdapter.KEY_CUISINE,
							           DinnerDbAdapter.KEY_BOOKABLE, DinnerDbAdapter.KEY_WEB,
							           DinnerDbAdapter.KEY_RATING };
		int[] to = new int[] { R.id.dinnerName, R.id.dinnerAddress, R.id.dinnerPostcode, R.id.dinnerPhone,
							   R.id.Distance, R.id.dinnerCuisine, R.id.dinnerBookable, R.id.dinnerWebsite,
							   R.id.Rating };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.food_dinner_row, cursor, from, to);
		setListAdapter(notes);
		
	}
 	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}
