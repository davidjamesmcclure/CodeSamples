package com.menus.app;

import android.os.Bundle;
import android.util.Log;

public class DirectionDisplay extends GetDirections {
	
	String TAG = "DirectionDisplay";
	
	public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        
        //takes the values sent from GetDirections
        
        Bundle extras = getIntent().getExtras();
        String transportItemChosen = extras.getString("transportSpinnerValue");
        String locationItemChosen = extras.getString("locationSpinnerValue");
        
        //produces 1 of 4 layouts (taxi is the same for both locations) depending on the spinner values used

        if((transportItemChosen.equals("Walking")) && (locationItemChosen.equals("Waverley Station"))){
        	setContentView(R.layout.directions_walkingwaverley_layout);
        	
        	if(((transportItemChosen.equals("Bus"))||(transportItemChosen.equals("Taxi")))
        		|| (locationItemChosen.equals("Edinburgh Airport"))){
        		Log.w(TAG, "Incorrect layout has been used");
        	}
        	
        }else if((transportItemChosen.equals("Bus")) && (locationItemChosen.equals("Waverley Station"))){
        	setContentView(R.layout.directions_buswaverley_layout);
        	
        	if(((transportItemChosen.equals("Walking"))||(transportItemChosen.equals("Taxi")))
            		|| (locationItemChosen.equals("Edinburgh Airport"))){
            		Log.w(TAG, "Incorrect layout has been used");
            	}
        	
        }else if((transportItemChosen.equals("Taxi")) && (locationItemChosen.equals("Waverley Station"))){
        	setContentView(R.layout.directions_taxi_layout);
        	
        	if(((transportItemChosen.equals("Bus"))||(transportItemChosen.equals("Walking")))
            		|| (locationItemChosen.equals("Edinburgh Airport"))){
            		Log.w(TAG, "Incorrect layout has been used");
            	}
        	
        }else if((transportItemChosen.equals("Bus")) && (locationItemChosen.equals("Edinburgh Airport"))){
        	setContentView(R.layout.directions_busairport_layout);
        	
        	if(((transportItemChosen.equals("Walking"))||(transportItemChosen.equals("Taxi")))
            		|| (locationItemChosen.equals("Waverley Station"))){
            		Log.w(TAG, "Incorrect layout has been used");
            	}
        	
        }else if((transportItemChosen.equals("Taxi")) && (locationItemChosen.equals("Edinburgh Airport"))){
        	setContentView(R.layout.directions_taxi_layout);
        	
        	if(((transportItemChosen.equals("Bus"))||(transportItemChosen.equals("Walking")))
            		|| (locationItemChosen.equals("Waverley Station"))){
            		Log.w(TAG, "Incorrect layout has been used");
            	}
        	
        }
	}
}
