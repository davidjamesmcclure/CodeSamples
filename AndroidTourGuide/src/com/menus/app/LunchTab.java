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

import com.database.lunch.LunchDbAdapter;

public class LunchTab extends ListActivity {
	
	private LunchDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int VIEW_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;
	
	//declare a tag to be used on log statements
	
	String TAG = "LunchTab";
	
	//creates the listview of entries from the database
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_lunch_layout);
        this.getListView().setDividerHeight(2);
        
        //checks if a database exists, if it doesn't then it will populate the table with the values below
        //this prevents the original values from being constantly inserted into the table and if the table
        //is emptied by the user allows it to stay empty 
        
        if(checkDataBase() == false){
			dbHelper = new LunchDbAdapter(this);
			dbHelper.open();
			
			//these are the values originally used in the table
			
			dbHelper.createLunch("The Elephant House", "21 George IV Bridge", "EH1 1EN", "0131 220 5355", "www.elephanthouse.biz", "10 minutes", "cafe", "no", "6" );
			dbHelper.createLunch("Peter's Yard", "27 Simpson Loan", "EH3 9GG", "0131 228 5876", "www.petersyard.com", "5 minutes", "swedish", "no", "8");
			dbHelper.createLunch("Mosque Kitchen", "50 Potterow, Southside", "EH8 9DD", "0131 667 1777", "no website", "5 minutes", "indian", "yes", "8");
			dbHelper.createLunch("Always Sunday", "170 High Street", "EH1 1QS", "0131 622 0667", "www.alwayssunday.co.uk", "15 minutes", "cafe", "yes", "8");
			dbHelper.createLunch("The Baked Potato Shop", "56 Cockburn Street", "EH1 1PB", "0131 225 7572", "no website", "15 minutes", "baked potatoes", "no", "8" );
		}else{
			dbHelper = new LunchDbAdapter(this);
			dbHelper.open();
		}
		fillData();
		registerForContextMenu(getListView());
    }
    
    //this method checks to see if a database exists with the name used
    
    protected boolean checkDataBase() {
		String DB_PATH = "/data/data/com.menus.app/databases/Lunch";
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
			createLunch();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createLunch();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dbHelper.deleteLunch(info.id);
			fillData();
			return true;
		case VIEW_ID:
			return true;
		}
		return super.onContextItemSelected(item);
	}
    
    private void createLunch() {
		Intent i = new Intent(this, LunchDetail.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, LunchDetail.class);
		i.putExtra(LunchDbAdapter.KEY_ROWID, id);
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
		cursor = dbHelper.fetchAllLunch();
		startManagingCursor(cursor);

		String[] from = new String[] { LunchDbAdapter.KEY_TITLE, LunchDbAdapter.KEY_ADDRESS,
							           LunchDbAdapter.KEY_POSTCODE, LunchDbAdapter.KEY_PHONE,
							           LunchDbAdapter.KEY_DISTANCE, LunchDbAdapter.KEY_CUISINE,
							           LunchDbAdapter.KEY_BOOKABLE, LunchDbAdapter.KEY_WEB,
							           LunchDbAdapter.KEY_RATING };
		int[] to = new int[] { R.id.lunchName, R.id.lunchAddress, R.id.lunchPostcode, R.id.lunchPhone,
							   R.id.Distance, R.id.lunchCuisine, R.id.lunchBookable, R.id.lunchWebsite,
							   R.id.Rating };

		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.food_lunch_row, cursor, from, to);
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
