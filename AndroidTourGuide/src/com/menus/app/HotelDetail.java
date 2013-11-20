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

public class HotelDetail extends Activity {
	
	private EditText mTitle;
	private EditText mAddress;
	private EditText mPostcode;
	private EditText mPhone;
	private EditText mWeb;
	private Spinner mDistance;
	private Spinner mRating;
	private Long mRowId;
	private HotelDbAdapter mDbHelper;
	
	//declare a tag to be used on log statements
	
	String TAG = "HotelDetail";
	
	//this creates and populates the row on the listview
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new HotelDbAdapter(this);
		mDbHelper.open();
        setContentView(R.layout.hotel_edit);
        
        //linking the titles with their object in the layout
        
        mTitle = (EditText) findViewById(R.id.editHotelName);
        mAddress = (EditText) findViewById(R.id.editHotelAddress);
        mPostcode = (EditText) findViewById(R.id.editHotelPostcode);
        mPhone = (EditText) findViewById(R.id.editHotelPhone);
        mWeb = (EditText) findViewById(R.id.editHotelWebsite);
        mDistance = (Spinner) findViewById(R.id.editDistance);
        mRating = (Spinner) findViewById(R.id.editRating);
        
        Button save = (Button) findViewById(R.id.hotel_edit_button);
        
        mRowId = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (savedInstanceState == null) ? null : (Long) 
				savedInstanceState.getSerializable(HotelDbAdapter.KEY_ROWID);
		if (extras != null) {
			mRowId = extras.getLong(HotelDbAdapter.KEY_ROWID);
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
			Cursor hotel = mDbHelper.fetchHotel(mRowId);
			startManagingCursor(hotel);
			String distancetemp = hotel.getString(hotel
					.getColumnIndexOrThrow(HotelDbAdapter.KEY_DISTANCE));
			
			//the code here retrieves the values from the database
			
			for (int i = 0; i < mDistance.getCount(); i++) {

				String s = (String) mDistance.getItemAtPosition(i);
				Log.e(null, s + " " + distancetemp);
				if (s.equalsIgnoreCase(distancetemp)) {
					mDistance.setSelection(i);
				}
			}
			
			String ratingtemp = hotel.getString(hotel
					.getColumnIndexOrThrow(HotelDbAdapter.KEY_RATING));

			for (int i = 0; i < mRating.getCount(); i++) {

				String s = (String) mRating.getItemAtPosition(i);
				Log.e(null, s + " " + ratingtemp);
				if (s.equalsIgnoreCase(ratingtemp)) {
					mRating.setSelection(i);
				}
			}			
			
			mTitle.setText(hotel.getString(hotel.getColumnIndexOrThrow(HotelDbAdapter.KEY_TITLE)));
			mAddress.setText(hotel.getString(hotel.getColumnIndexOrThrow(HotelDbAdapter.KEY_ADDRESS)));
			mPostcode.setText(hotel.getString(hotel.getColumnIndexOrThrow(HotelDbAdapter.KEY_POSTCODE)));
			mPhone.setText(hotel.getString(hotel.getColumnIndexOrThrow(HotelDbAdapter.KEY_PHONE)));
			mWeb.setText(hotel.getString(hotel.getColumnIndexOrThrow(HotelDbAdapter.KEY_WEB)));
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(HotelDbAdapter.KEY_ROWID, mRowId);
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
			long id = mDbHelper.createHotel(title, address, postcode, phone, web, distance, rating);
			if (id > 0) {
				mRowId = id;
			}
		} else {
			mDbHelper.updateHotel(mRowId, title, address, postcode, phone, web, distance, rating);
		}
	}
}
