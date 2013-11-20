package com.menus.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.database.coffee.CoffeeDbAdapter;
import com.database.hotel.HotelDbAdapter;

public class CoffeeDetail extends Activity {
	
	private EditText mTitle;
	private EditText mAddress;
	private EditText mPostcode;
	private EditText mPhone;
	private EditText mWeb;
	private Spinner mDistance;
	private Spinner mRating;
	private Long mRowId;
	private CoffeeDbAdapter mDbHelper;
	
	//declare a tag to be used on log statements
	
	String TAG = "CoffeeDetail";
	
	//this creates and populates the row on the listview
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new CoffeeDbAdapter(this);
		mDbHelper.open();
        setContentView(R.layout.food_coffee_edit);
        
        //linking the titles with their object in the layout
        
        mTitle = (EditText) findViewById(R.id.editCoffeeName);
        mAddress = (EditText) findViewById(R.id.editCoffeeAddress);
        mPostcode = (EditText) findViewById(R.id.editCoffeePostcode);
        mPhone = (EditText) findViewById(R.id.editCoffeePhone);
        mWeb = (EditText) findViewById(R.id.editCoffeeWebsite);
        mDistance = (Spinner) findViewById(R.id.editDistance);
        mRating = (Spinner) findViewById(R.id.editRating);
        
        Button save = (Button) findViewById(R.id.coffee_edit_button);
        
        mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (savedInstanceState == null) ? null : (Long) 
				savedInstanceState.getSerializable(CoffeeDbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(CoffeeDbAdapter.KEY_ROWID);
		}
		
		populateFields();
        
		save.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				setResult(RESULT_OK);
				finish();
			}

		});
    }
    
    //populates the fields in the row
    
    private void populateFields() {
		if (mRowId != null) {
			Cursor coffee = mDbHelper.fetchCoffee(mRowId);
			startManagingCursor(coffee);
			
			//the code here retrieves the values from the database
			
			String distance = coffee.getString(coffee
					.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_DISTANCE));

			for (int i = 0; i < mDistance.getCount(); i++) {

				String s = (String) mDistance.getItemAtPosition(i);
				Log.e(null, s + " " + distance);
				if (s.equalsIgnoreCase(distance)) {
					mDistance.setSelection(i);
				}
			}
			
			String ratingtemp = coffee.getString(coffee
					.getColumnIndexOrThrow(HotelDbAdapter.KEY_RATING));

			for (int i = 0; i < mRating.getCount(); i++) {

				String s = (String) mRating.getItemAtPosition(i);
				Log.e(null, s + " " + ratingtemp);
				if (s.equalsIgnoreCase(ratingtemp)) {
					mRating.setSelection(i);
				}
			}

			mTitle.setText(coffee.getString(coffee.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_TITLE)));
			mAddress.setText(coffee.getString(coffee.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_ADDRESS)));
			mPostcode.setText(coffee.getString(coffee.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_POSTCODE)));
			mPhone.setText(coffee.getString(coffee.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_PHONE)));
			mWeb.setText(coffee.getString(coffee.getColumnIndexOrThrow(CoffeeDbAdapter.KEY_WEB)));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(CoffeeDbAdapter.KEY_ROWID, mRowId);
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	@Override
	protected void onResume() {
		super.onResume();
		populateFields();
	}

	private void saveState() {
		String title = mTitle.getText().toString();
		String address = mAddress.getText().toString();
		String postcode = mPostcode.getText().toString();
		String phone = mPhone.getText().toString();
		String web = mWeb.getText().toString();
		String distance = (String) mDistance.getSelectedItem();
		String rating = (String) mRating.getSelectedItem();
		
		//record the values in the savestate at this point
		
		Log.d(TAG, "Values in savestate are" + title + address + postcode + phone + web + distance + rating);

		if (mRowId == null) {
			long id = mDbHelper.createCoffee(title, address, postcode, phone, web, distance, rating);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateCoffee(mRowId, title, address, postcode, phone, web, distance, rating);
		}
	}
}
