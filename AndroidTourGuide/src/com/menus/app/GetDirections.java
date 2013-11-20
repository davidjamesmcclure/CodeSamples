package com.menus.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class GetDirections extends Activity {
	
	String TAG = "GetDirection";

	private Spinner transportSpinner, locationSpinner;
	private Button buttonSubmit;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions_layout);
        addListenerOnButton();
        
        //defines the spinner used to ask the user which mode of transport they will be using
        
        transportSpinner = (Spinner) findViewById(R.id.transportSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.modeTransport, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportSpinner.setAdapter(adapter);       
        
        //defines the spinner used to ask the user where they currently are

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                this, R.array.spinnerLocation, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter2);
    }    
    
    
    public void addListenerOnButton() {
        	 
        	transportSpinner = (Spinner) findViewById(R.id.transportSpinner);
        	locationSpinner = (Spinner) findViewById(R.id.locationSpinner);
        	
        	buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        	
        	buttonSubmit.setOnClickListener(new OnClickListener() {
        	
        	  @Override
        	  public void onClick(View v) {
        		  
        		  //converts the spinner values into a string to be used in selecting which layout is used in DirectionDisplay class
        		  
        	      String transportItemChosen = (transportSpinner.getSelectedItem().toString());
        	      String locationItemChosen = (locationSpinner.getSelectedItem().toString());
        	      
        	      //checks if user selects walking from the airport and produces a toast advising against it
        	      
        	      if ("Walking".equals(transportItemChosen) && "Edinburgh Airport".equals(locationItemChosen)) {
        			 
        			  Toast.makeText(getBaseContext(), "Walking from the airport is not advised.", Toast.LENGTH_LONG).show();
        		 
        			  if(((transportItemChosen.equals("Bus"))||(transportItemChosen.equals("Taxi")))
        		        		|| (locationItemChosen.equals("Waverley Station"))){
        		        		Log.w(TAG, "Incorrect layout has been used");
        		      }
        			  
        		  }else{
        			
        			  //if if not walking from the airport, sends the values from the spinners to DirectionDisplay (also changes to that class)
        			  
            		  Intent i = new Intent(GetDirections.this.getApplicationContext(), DirectionDisplay.class);
            		  i.putExtra("transportSpinnerValue", transportItemChosen); 
            		  i.putExtra("locationSpinnerValue", locationItemChosen);
      				  GetDirections.this.startActivity(i);
      				        			  
        		  }
        		  
        	  }
         
        	});
    }
}

