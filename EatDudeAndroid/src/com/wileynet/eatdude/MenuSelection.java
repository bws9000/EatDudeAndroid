/*
 * 
    Copyright (C) 2012  Wiley Snyder

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or 
     any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
	
	Any other questions or concerns contact wiley@wileynet.com

*/

package com.wileynet.eatdude;

import java.util.HashMap;
import java.util.Map;

import com.wileynet.eatdude.util.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

//restaurant has more then one menu
public class MenuSelection extends Activity {
	
	private HashMap<String, String> menu = 
    	new HashMap<String, String>();
	private String errorCopy = "Internet Connection Error";
	private String key;
	private String value;
	private Object tag;
	private Intent intent;
	private int count =0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Toast.makeText(MenuSelection.this, ""+RestaurantSelection.r.getMenu().size() , Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_selection);
        this.setTitle(MainHome.restaurant_name);
        MainHome.in_restaurant = true;
        
        menu = RestaurantSelection.r.getMenu();
        
        for(Map.Entry<String,String> entry : menu.entrySet()) {
        	this.key = entry.getKey();
			this.value = entry.getValue();
			Button btn= new Button(this);
			btn.setText(value);
			btn.setTag(key);
			count++;
			btn.setOnClickListener(new View.OnClickListener()
	        {
	        	
	            public void onClick(View v)
	            {	
	            	//Toast.makeText(MenuSelection.this, ""+v.getTag(), Toast.LENGTH_SHORT).show();
	            	intent= new Intent(v.getContext(), CategorySelection.class);
	            	tag = v.getTag();
	            	intent.putExtra("menu_id", ""+tag);
	            	//loadMenu();
	            	startActivity(intent);
	            }
	        });
			
			LinearLayout layout = (LinearLayout)findViewById(R.id.menu_selection_layout);
	        
	        LinearLayout.LayoutParams params = new
	        LinearLayout.LayoutParams(
	        		LinearLayout.LayoutParams.FILL_PARENT,
	        		LinearLayout.LayoutParams.FILL_PARENT
	                    );
	        params.weight = 1.0f;
	        layout.addView(btn, params);
	        btn.setLayoutParams(params);
	        
	        
        }

    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
    }
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
    
 ///MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		ConnectivityManager netcon = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = netcon.getActiveNetworkInfo();
		
		switch (item.getItemId()) {
		case R.id.call_restaurant:
			// Toast.makeText(CategorySelection.this, "call"
			// ,Toast.LENGTH_SHORT).show();
			callDialog();
			return true;
		case R.id.restaurant_details:
			alertDialog();
			return true;
		case R.id.home:
			try {
				if (ni.isConnected()) {
					Intent intent = new Intent(MenuSelection.this, MainHome.class);
					startActivity(intent);
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(MenuSelection.this, errorCopy,
						Toast.LENGTH_SHORT).show();
				return true;
			}
		case R.id.help:
			Intent intent2 = new Intent(MenuSelection.this,
					HelpHome.class);
			startActivity(intent2);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void call() {
		try {
			if (!MainHome.restaurant_phone.equals("none")) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				String phone_number = MainHome.restaurant_phone.replaceAll(
						"[^\\d]", "");
				callIntent.setData(Uri.parse("tel:" + phone_number));
				startActivity(callIntent);
			}
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}

	}

	public void alertDialog() {
		StringUtils su = new StringUtils();
		String message = MainHome.restaurant_name + "\n"
				+ MainHome.restaurant_address + "\n" + su.capitalizeFirstLetter(MainHome.restaurant_city)
				+ "\n" + MainHome.restaurant_phone;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("close details",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void callDialog() {
		String message = "Are you sure you want to call " + MainHome.restaurant_name + " now ? This call " +
				"will be immediately dialed for you.";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								call();
							}
						});
		builder.setMessage(message)
		.setCancelable(false)
		.setNegativeButton("no",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
    
}