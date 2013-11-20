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

import com.database.hotel.HotelDbAdapter;

public class HotelTab extends ListActivity {
	
	private HotelDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int VIEW_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;
	
	//declare a tag to be used on log statements
	
	String TAG = "HotelTab";
	
	//creates the listview of entries from the database
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_layout);
        this.getListView().setDividerHeight(2);
        
        //checks if a database exists, if it doesn't then it will populate the table with the values below
        //this prevents the original values from being constantly inserted into the table and if the table
        //is emptied by the user allows it to stay empty 
        
        if(checkDataBase() == false){
			dbHelper = new HotelDbAdapter(this);
			dbHelper.open();
			
			//these are the values originally used in the table
			
			dbHelper.createHotel("Ten Hill Place Hotel", "10 hill place", "EH8 9DS", "0131 662 2080", "www.tenhillplace.com", "5 minutes", "8");
			dbHelper.createHotel("Hotel du Vin", "11 bristo place", "EH1 1EZ", "0131 247 4900", "www.hotelduvin.com", "5 minutes", "9");
			dbHelper.createHotel("The Balmoral Hotel", "1 princess street", "EH2 2EQ", "0131 556 2414", "www.thebalmoralhotel.com", "15 minutes", "9");
			dbHelper.createHotel("The Scotsman Hotel", "20 North Bridge", "EH1 1TR", "0131 556 5565", "www.thescotsmanhotel.co.uk", "15 minutes", "8");
			dbHelper.createHotel("Ibis Edinburgh Centre", "6 Hunter Square", "EH1 1QW", "0131 240 7007", "www.ibishotel.com", "10 minutes", "8");
		}else{
			dbHelper = new HotelDbAdapter(this);
			dbHelper.open();
		}
		fillData();
		registerForContextMenu(getListView());
    }
	
	//this method checks to see if a database exists with the name used
    
    protected boolean checkDataBase() {
		String DB_PATH = "/data/data/com.menus.app/databases/Hotel";
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
			createHotel();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createHotel();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dbHelper.deleteHotel(info.id);
			fillData();
			return true;
		case VIEW_ID:
			return true;
		}
		return super.onContextItemSelected(item);
	}
    
    private void createHotel() {
		Intent i = new Intent(this, HotelDetail.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, HotelDetail.class);
		i.putExtra(HotelDbAdapter.KEY_ROWID, id);
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
		cursor = dbHelper.fetchAllHotel();
		startManagingCursor(cursor);

		String[] from = new String[] { HotelDbAdapter.KEY_TITLE, HotelDbAdapter.KEY_ADDRESS,
							           HotelDbAdapter.KEY_POSTCODE, HotelDbAdapter.KEY_PHONE,
							           HotelDbAdapter.KEY_DISTANCE, HotelDbAdapter.KEY_WEB, HotelDbAdapter.KEY_RATING };
		int[] to = new int[] { R.id.hotelName, R.id.hotelAddress, R.id.hotelPostcode, R.id.hotelPhone,
							   R.id.Distance, R.id.hotelWebsite, R.id.Rating };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.hotel_row, cursor, from, to);
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
