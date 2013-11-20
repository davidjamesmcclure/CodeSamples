package com.menus.app;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class FindFood extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_tab_layout);
 
        TabHost tabHost = getTabHost();
 
        //Tab for coffee
        TabSpec coffeespec = tabHost.newTabSpec("Coffee");
        // setting Title and Icon for the Tab
        coffeespec.setIndicator("Coffee", getResources().getDrawable(R.drawable.icon_coffee_tab));
        Intent photosIntent = new Intent(FindFood.this, CoffeeTab.class);
        coffeespec.setContent(photosIntent);
 
        // Tab for lunch
        TabSpec lunchspec = tabHost.newTabSpec("Lunch");
        lunchspec.setIndicator("Lunch", getResources().getDrawable(R.drawable.icon_lunch_tab));
        Intent songsIntent = new Intent(FindFood.this, LunchTab.class);
        lunchspec.setContent(songsIntent);
 
        // Tab for dinner
        TabSpec dinnerspec = tabHost.newTabSpec("Dinner");
        dinnerspec.setIndicator("Dinner", getResources().getDrawable(R.drawable.icon_dinner_tab));
        Intent videosIntent = new Intent(FindFood.this, DinnerTab.class);
        dinnerspec.setContent(videosIntent);
 
        // Adding the tabs to tabhost
        tabHost.addTab(coffeespec); 
        tabHost.addTab(lunchspec); 
        tabHost.addTab(dinnerspec); 
    }
}