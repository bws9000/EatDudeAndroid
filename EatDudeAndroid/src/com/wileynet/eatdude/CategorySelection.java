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



//   ==>   http://developer.android.com/guide/appendix/faq/commontasks.html

import java.util.HashMap;

import com.wileynet.eatdude.db.LoadRestaurant;
import com.wileynet.eatdude.db.RestaurantDbAdapter;
import com.wileynet.eatdude.db.RestaurantDbHelper;
import com.wileynet.eatdude.util.StringUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategorySelection extends ListActivity {
	
	private RestaurantDbAdapter mDbHelper;
	private RestaurantDbHelper rDb = new RestaurantDbHelper(this);
	private LoadRestaurant loadR;
	TextView selection;
	private String[] categories;
	// private PopupWindow pw;

	protected HashMap<String, String> menumap = new HashMap<String, String>();

	protected HashMap<String, String> menu_category = new HashMap<String, String>();

	private String errorCopy = "Internet Connection Error";
	private String selected_menu_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_selection);
		
		MainHome.in_restaurant = false;
		this.setTitle(MainHome.restaurant_name);

		menumap = RestaurantSelection.r.getMenu();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			selected_menu_id = extras.getString("menu_id");
		}

		// Toast.makeText(CategorySelection.this, selected_menu_id ,
		// Toast.LENGTH_SHORT).show();

		mDbHelper = new RestaurantDbAdapter(this);
		try {
			mDbHelper.open();

			loadR = new LoadRestaurant(this);
			loadR.load();

			categories = rDb.getCategoryArrayFromDb(selected_menu_id);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// display categories
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, categories));

		selection = (TextView) findViewById(R.id.selection);

	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		String check = categories[position];
		// String selection = parent.getItemAtPosition(position).toString();
		// Toast.makeText(v.getContext(), ""+selection ,
		// Toast.LENGTH_SHORT).show();

		for (int i = 0; i < categories.length; i++) {
			String value = categories[i];
			if (value == check) {
				Intent intent = new Intent(v.getContext(), ItemSelection.class);
				intent.putExtra("category", check);
				intent.putExtra("m_id", selected_menu_id);
				startActivity(intent);
			}
		}

		/*
		 * for(Map.Entry<String,String> entry : r.getMenu_category().entrySet())
		 * { String key = entry.getKey(); String value = entry.getValue();
		 * if(value == check){
		 * 
		 * //load new intent with items from that category key
		 * System.out.println(key + " => " + value); //Intent myIntent = new
		 * Intent(Intent.ACTION_VIEW,Uri.parse("http://www.google.com"));
		 * //startActivity(myIntent); Intent i= new Intent(v.getContext(),
		 * ItemSelection.class); i.putExtra("category", check);
		 * startActivity(i);
		 * 
		 * } }
		 */
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

	// MENU
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
			// popup();
			alertDialog();
			return true;
		case R.id.home:
			try {
				if (ni.isConnected()) {
					Intent intent = new Intent(CategorySelection.this, MainHome.class);
					startActivity(intent);
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(CategorySelection.this, errorCopy,
						Toast.LENGTH_SHORT).show();
				return true;
			}
			
		case R.id.help:
			Intent intent2 = new Intent(CategorySelection.this, HelpHome.class);
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
				// Toast.makeText(CategorySelection.this,
				// phone_number,Toast.LENGTH_SHORT).show();
				callIntent.setData(Uri.parse("tel:" + phone_number));
				startActivity(callIntent);
			}
		} catch (ActivityNotFoundException activityException) {
			Log.e("helloandroid dialing example", "Call failed",
					activityException);
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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
				Toast.makeText(CategorySelection.this, errorCopy,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
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