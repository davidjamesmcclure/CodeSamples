package com.menus.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.database.dinner.DinnerDbAdapter;
import com.database.hotel.HotelDbAdapter;

public class DinnerDetail extends Activity {
	
	private EditText mTitle;
	private EditText mAddress;
	private EditText mPostcode;
	private EditText mPhone;
	private EditText mWeb;
	private Spinner mDistance;
	private EditText mCuisine;
	private Spinner mBookable;
	private Spinner mRating;
	private Long mRowId;
	private DinnerDbAdapter mDbHelper;
	
	//declare a tag to be used on log statements
	
	String TAG = "DinnerDetail";
	
	//this creates and populates the row on the listview
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DinnerDbAdapter(this);
		mDbHelper.open();
        setContentView(R.layout.food_dinner_edit);
        
        //linking the titles with their object in the layout
        
        mTitle = (EditText) findViewById(R.id.editDinnerName);
        mAddress = (EditText) findViewById(R.id.editDinnerAddress);
        mPostcode = (EditText) findViewById(R.id.editDinnerPostcode);
        mPhone = (EditText) findViewById(R.id.editDinnerPhone);
        mWeb = (EditText) findViewById(R.id.editDinnerWebsite);
        mDistance = (Spinner) findViewById(R.id.editDistance);
        mCuisine = (EditText)findViewById(R.id.editDinnerCuisine);
        mBookable = (Spinner) findViewById(R.id.editBookable);
        mRating = (Spinner) findViewById(R.id.editRating);
        
        Button save = (Button) findViewById(R.id.dinner_edit_button);
        
        mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (savedInstanceState == null) ? null : (Long) 
				savedInstanceState.getSerializable(DinnerDbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(DinnerDbAdapter.KEY_ROWID);
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
			Cursor dinner = mDbHelper.fetchDinner(mRowId);
			startManagingCursor(dinner);
			String distancetemp = dinner.getString(dinner
					.getColumnIndexOrThrow(DinnerDbAdapter.KEY_DISTANCE));
			
			//the code here retrieves the values from the database

			for (int i = 0; i < mDistance.getCount(); i++) {

				String s = (String) mDistance.getItemAtPosition(i);
				Log.e(null, s + " " + distancetemp);
				if (s.equalsIgnoreCase(distancetemp)) {
					mDistance.setSelection(i);
				}
			}
			
			String bookabletemp = dinner.getString(dinner
					.getColumnIndexOrThrow(DinnerDbAdapter.KEY_BOOKABLE));
			
			for (int i = 0; i < mBookable.getCount(); i++) {

				String s = (String) mBookable.getItemAtPosition(i);
				Log.e(null, s + " " + bookabletemp);
				if (s.equalsIgnoreCase(bookabletemp)) {
					mBookable.setSelection(i);
				}
			}
			
			String ratingtemp = dinner.getString(dinner
					.getColumnIndexOrThrow(HotelDbAdapter.KEY_RATING));

			for (int i = 0; i < mRating.getCount(); i++) {

				String s = (String) mRating.getItemAtPosition(i);
				Log.e(null, s + " " + ratingtemp);
				if (s.equalsIgnoreCase(ratingtemp)) {
					mRating.setSelection(i);
				}
			}

			mTitle.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_TITLE)));
			mAddress.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_ADDRESS)));
			mPostcode.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_POSTCODE)));
			mPhone.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_PHONE)));
			mWeb.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_WEB)));
			mCuisine.setText(dinner.getString(dinner.getColumnIndexOrThrow(DinnerDbAdapter.KEY_CUISINE)));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(DinnerDbAdapter.KEY_ROWID, mRowId);
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
		String cuisine = mCuisine.getText().toString();
		String bookable = (String) mBookable.getSelectedItem();
		String rating = (String) mRating.getSelectedItem();
		
		//record the values in the savestate at this point
		
		Log.d(TAG, "Values in savestate are" + title + address + postcode + phone + web + distance + rating);


		if (mRowId == null) {
			long id = mDbHelper.createDinner(title, address, postcode, phone, web, distance, cuisine, bookable, rating);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateDinner(mRowId, title, address, postcode, phone, web, distance, cuisine, bookable, rating);
		}
	}
}
