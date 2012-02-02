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

import com.wileynet.eatdude.R;
import com.wileynet.eatdude.util.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;

public class HelpHome extends Activity {

	//private String errorCopy = "Internet Connection Error";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		LinearLayout layout = (LinearLayout) findViewById(R.id.help_main);
		layout.setBackgroundColor(Color.WHITE);
		
		
		//one
		TextView headerone = (TextView) findViewById(R.id.headerone);
		headerone.setText("Eat Dude 1.0 for Android");

		TextView onecontent = (TextView) findViewById(R.id.onecontent);
		onecontent
				.setText("This app was intended for people who would like all their "
						+ "take-out menus organized conveniently in one place you can take with you "
						+ "everywhere. The menus are managed and served to this app from eatdude.com. " +
								"Visit eatdude.com for more details.");
		
		
		//two
		TextView headertwo = (TextView) findViewById(R.id.headertwo);
		headertwo.setText("Menus");

		TextView twocontent = (TextView) findViewById(R.id.twocontent);
		twocontent
				.setText("Please contact us if we are missing your favorite restaurant menu. "
						+ "We will do everything we can to get it added into the system.  Send any details to "
						+ "admin@eatdude.com and we will email you back when the menu is available or add "
						+ "a menu free at eatdude.com.");
		
		//three
		TextView howto = (TextView) findViewById(R.id.howtoheader);
		howto.setText("How to use this app");

		TextView howtocontent = (TextView) findViewById(R.id.howtocontent);
		howtocontent.setText("The idea is to find the restaurant you want, view the menu and " +
						"call in your order. It does not send the order to the restaurant. At this " +
						"time it is just a take-out menu on your device. After making the call you " +
						"can hit the back button and browse the menu while you are on the phone " +
						"or before you make the call.");
		
		//four
		TextView restaurants = (TextView) findViewById(R.id.restaurantsheader);
		restaurants.setText("Restaurants");

		TextView restaurantscontent = (TextView) findViewById(R.id.restaurantscontent);
		restaurantscontent.setText("Menus can be created and managed by restaurant owners. Every " +
						"restaurant added to the system is contacted, verified and given the opportunity " +
						"of full control over every aspect of their menu including the option to not " +
						"have one listed. The basic free functionality and point of this system is listing " +
						"menus that can be managed easily and updated via the internet by the owners " +
						"of the menu making them available to mobile clients.");
		
		
		//five
		TextView headerthree = (TextView) findViewById(R.id.headerthree);
		headerthree.setText("Errors");

		TextView threecontent = (TextView) findViewById(R.id.threecontent);
		threecontent
				.setText("If you receive an “Internet Connection Error” message you are not " + 
						"connected to the internet or the menu server is experiencing a temporary " +
						"down time. Make sure you are not on an existing phone call when you are searching " +
						"for or selecting a restaurant.");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	// MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		
		if (!MainHome.in_restaurant) {
			inflater.inflate(R.menu.help_home_before_restaurant_select, menu);
		} else {
			inflater.inflate(R.menu.help_home, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		MenuItem select = item;
		if (!MainHome.in_restaurant) {
			switch (select.getItemId()) {
			case R.id.call_restaurant_inactive:
				// Toast.makeText(CategorySelection.this, "call"
				// ,Toast.LENGTH_SHORT).show();
				errorAlertDialog();
				return true;
			case R.id.restaurant_details_inactive:
				// popup();
				errorAlertDialog();
				return true;
			case R.id.home:
				Intent intent = new Intent(HelpHome.this, MainHome.class);
				startActivity(intent);
				return true;
			case R.id.help_inactive:
				//
			}
		} else {
			switch (select.getItemId()) {
			case R.id.call_restaurant:
				callDialog();
				return true;
			case R.id.restaurant_details:
				alertDialog();
				return true;
			case R.id.home:
				Intent intent = new Intent(HelpHome.this, MainHome.class);
				startActivity(intent);
				return true;
			case R.id.help_inactive:
				return true;
				//
			}
		}

		return super.onOptionsItemSelected(select);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
					finish();
					return true;
			
		}
		return super.onKeyDown(keyCode, event);
		
		/*
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ConnectivityManager netcon = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = netcon.getActiveNetworkInfo();

			try {
				if (ni.isConnected()) {
					// System.out.println("CONNECTED !" );
					finish();
					return true;
				}
			} catch (Exception e) {
				// System.out.println("ON THE PHONE !" );
				Toast.makeText(HelpHome.this, errorCopy, Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
		*/
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

	public void errorAlertDialog() {
		String message = MainHome.restaurant_message_detail_error;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("close",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
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
