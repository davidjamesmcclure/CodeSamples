package com.menus.app;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.database.hotel.HotelDbAdapter;
import com.database.lunch.LunchDbAdapter;

public class LunchDetail extends Activity {
	
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
	private LunchDbAdapter mDbHelper;
	
	//declare a tag to be used on log statements
	
	String TAG = "LunchDetail";
	
	//this creates and populates the row on the listview
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new LunchDbAdapter(this);
		mDbHelper.open();
        setContentView(R.layout.food_lunch_edit);
        
        //linking the titles with their object in the layout
        
        mTitle = (EditText) findViewById(R.id.editLunchName);
        mAddress = (EditText) findViewById(R.id.editLunchAddress);
        mPostcode = (EditText) findViewById(R.id.editLunchPostcode);
        mPhone = (EditText) findViewById(R.id.editLunchPhone);
        mWeb = (EditText) findViewById(R.id.editLunchWebsite);
        mDistance = (Spinner) findViewById(R.id.editDistance);
        mCuisine = (EditText)findViewById(R.id.editLunchCuisine);
        mBookable = (Spinner) findViewById(R.id.editBookable);
        mRating = (Spinner) findViewById(R.id.editRating);
        
        Button save = (Button) findViewById(R.id.lunch_edit_button);
        
        mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (savedInstanceState == null) ? null : (Long) 
				savedInstanceState.getSerializable(LunchDbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(LunchDbAdapter.KEY_ROWID);
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
			Cursor lunch = mDbHelper.fetchLunch(mRowId);
			startManagingCursor(lunch);
			String distancetemp = lunch.getString(lunch
					.getColumnIndexOrThrow(LunchDbAdapter.KEY_DISTANCE));
			
			//the code here retrieves the values from the database
			
			for (int i = 0; i < mDistance.getCount(); i++) {

				String s = (String) mDistance.getItemAtPosition(i);
				Log.e(null, s + " " + distancetemp);
				if (s.equalsIgnoreCase(distancetemp)) {
					mDistance.setSelection(i);
				}
			}
			
			String bookabletemp = lunch.getString(lunch
					.getColumnIndexOrThrow(LunchDbAdapter.KEY_BOOKABLE));
			
			for (int i = 0; i < mBookable.getCount(); i++) {

				String s = (String) mBookable.getItemAtPosition(i);
				Log.e(null, s + " " + bookabletemp);
				if (s.equalsIgnoreCase(bookabletemp)) {
					mBookable.setSelection(i);
				}
			}
			
			String ratingtemp = lunch.getString(lunch
					.getColumnIndexOrThrow(HotelDbAdapter.KEY_RATING));

			for (int i = 0; i < mRating.getCount(); i++) {

				String s = (String) mRating.getItemAtPosition(i);
				Log.e(null, s + " " + ratingtemp);
				if (s.equalsIgnoreCase(ratingtemp)) {
					mRating.setSelection(i);
				}
			}

			mTitle.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_TITLE)));
			mAddress.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_ADDRESS)));
			mPostcode.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_POSTCODE)));
			mPhone.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_PHONE)));
			mWeb.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_WEB)));
			mCuisine.setText(lunch.getString(lunch.getColumnIndexOrThrow(LunchDbAdapter.KEY_CUISINE)));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(LunchDbAdapter.KEY_ROWID, mRowId);
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
			long id = mDbHelper.createLunch(title, address, postcode, phone, web, distance, cuisine, bookable, rating);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateLunch(mRowId, title, address, postcode, phone, web, distance, cuisine, bookable, rating);
		}
	}
}
