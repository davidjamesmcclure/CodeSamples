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

import com.database.coffee.CoffeeDbAdapter;



public class CoffeeTab extends ListActivity {
	
	private CoffeeDbAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int VIEW_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private Cursor cursor;
	
	//declare a tag to be used on log statements
	
	String TAG = "CoffeeTab";
	
	//creates the listview of entries from the database
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_coffee_layout);
        this.getListView().setDividerHeight(2);
        
        //checks if a database exists, if it doesn't then it will populate the table with the values below
        //this prevents the original values from being constantly inserted into the table and if the table
        //is emptied by the user allows it to stay empty 
        
		if(checkDataBase() == false){
			dbHelper = new CoffeeDbAdapter(this);
			dbHelper.open();
			
			//these are the values originally used in the table
			
			dbHelper.createCoffee("Kilimanjaro Coffee", "104 Nicolson Street", "EH8 9EJ", "0131 662 0135", "No website", "5 minutes",  "9" );
			dbHelper.createCoffee("Beanscene", "99 Nicolson Street", "EH8 9BY", "0131 667 5967", "www.beanscene.co.uk", "5 minutes",  "5" );
			dbHelper.createCoffee("Black Medicine", "2 Nicolson Street", "EH8 9DH", "0131 557 6269", "www.blackmedicine.co.uk", "10 minutes",  "7" );
			dbHelper.createCoffee("Starbucks", "140 Nicolson Street", "EH8 9EH", "0131 662 8947", "www.starbucks.com", "5 minutes",  "7" );
			dbHelper.createCoffee("Caffe Nero", "53-59 Blackwells, South Bridge", "EH1 1LS", "0131 558 2624", "www.caffenero.com", "10 minutes",  "5" );
		}else{
			dbHelper = new CoffeeDbAdapter(this);
			dbHelper.open();
		}
		fillData();
		registerForContextMenu(getListView());
    }
    
    //this method checks to see if a database exists with the name used
    
    protected boolean checkDataBase() {
		String DB_PATH = "/data/data/com.menus.app/databases/Coffee";
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
			createCoffee();
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createCoffee();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			dbHelper.deleteCoffee(info.id);
			fillData();
			return true;
		case VIEW_ID:
			return true;
		}
		return super.onContextItemSelected(item);
	}
    
    private void createCoffee() {
		Intent i = new Intent(this, CoffeeDetail.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this, CoffeeDetail.class);
		i.putExtra(CoffeeDbAdapter.KEY_ROWID, id);
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
		cursor = dbHelper.fetchAllCoffee();
		startManagingCursor(cursor);

		String[] from = new String[] { CoffeeDbAdapter.KEY_TITLE, CoffeeDbAdapter.KEY_ADDRESS,
							           CoffeeDbAdapter.KEY_POSTCODE, CoffeeDbAdapter.KEY_PHONE,
							           CoffeeDbAdapter.KEY_WEB,CoffeeDbAdapter.KEY_DISTANCE, 
							           CoffeeDbAdapter.KEY_RATING };
		int[] to = new int[] { R.id.CoffeeName, R.id.CoffeeAddress, R.id.CoffeePostcode, R.id.CoffeePhone,
								R.id.CoffeeWebsite, R.id.Distance, R.id.Rating };
		
		// Now create an array adapter and set it to display using our row
		SimpleCursorAdapter notes = new SimpleCursorAdapter(this,
				R.layout.food_coffee_row, cursor, from, to);
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
