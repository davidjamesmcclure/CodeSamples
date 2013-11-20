package com.menus.app;

import android.app.Activity;
import android.os.Bundle;

//calls and displays the information layout

public class Information extends Activity{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);  
    }
    
}    