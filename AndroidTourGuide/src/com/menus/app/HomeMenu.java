package com.menus.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeMenu extends Activity {
	
	// Called when the activity is first created.
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_menu_layout);
		
		//sets the image as a button to go to directions (this is repeated another 3 times further on)

		ImageButton directionsicon = (ImageButton) findViewById(R.id.buttonDirections);

		directionsicon.setOnClickListener(new View.OnClickListener() {
			
			//sets the activity to go to when the button is pressed
			
			public void onClick(View v) {
				Intent i = new Intent(HomeMenu.this.getApplicationContext(),
						GetDirections.class);
				HomeMenu.this.startActivity(i);

			}
		});
		

		ImageButton foodicon = (ImageButton) findViewById(R.id.buttonFood);

		foodicon.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(HomeMenu.this.getApplicationContext(),
						FindFood.class);
				HomeMenu.this.startActivity(i);

			}
		});

		ImageButton hotelsicon = (ImageButton) findViewById(R.id.buttonHotels);

		hotelsicon.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(HomeMenu.this.getApplicationContext(),
						HotelTab.class);
				HomeMenu.this.startActivity(i);

			}
		});

		ImageButton information = (ImageButton) findViewById(R.id.buttonInfo);

		information.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(HomeMenu.this.getApplicationContext(),
						Information.class);
				HomeMenu.this.startActivity(i);

			}
		});
	}
}