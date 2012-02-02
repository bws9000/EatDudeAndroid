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

import com.wileynet.eatdude.db.RestaurantDbAdapter;
import com.wileynet.eatdude.util.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ItemSelection extends Activity {

	private RestaurantDbAdapter mDbHelper;
	private String errorCopy = "Internet Connection Error";
	// private String[] items = {"test","test"};
	private String category = "error";
	private String m_id = "error";
	private int count;
	private LayoutInflater linflater;
	private LinearLayout l;
	private Cursor itemsCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_selection);
		// http://about-android.blogspot.com/2010/04/creating-dynamic-customized-list-view.html
		l = (LinearLayout) findViewById(R.id.mylayout1);
		linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDbHelper = new RestaurantDbAdapter(this);
		this.setTitle(MainHome.restaurant_name);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			category = extras.getString("category");
			m_id = extras.getString("m_id");
		}

		try {

			mDbHelper.open();
			Cursor cat_idCursor = mDbHelper.fetchCategory(category, m_id);
			startManagingCursor(cat_idCursor);
			String cat_id = cat_idCursor.getString(0);
			itemsCursor = mDbHelper.fetchItems(cat_id, m_id);
			mDbHelper.close();
			startManagingCursor(itemsCursor);

		} catch (Exception e) {
			e.printStackTrace();
		}
		mDbHelper.close();

	}

	@Override
	protected void onStart() {
		super.onStart();

		// count=1;
		for (int i = 0; i < itemsCursor.getCount(); i++) {
			View customView = linflater.inflate(R.layout.customlistviewlayout,
					null);
			TextView tv = (TextView) customView.findViewById(R.id.TextView01);

			tv.setId((Integer.parseInt(itemsCursor.getString(1))));
			tv.setText(itemsCursor.getString(4));
			tv.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// Toast.makeText(ItemSelection.this, v.getId() + "",
					// Toast.LENGTH_LONG).show();
					Intent itemDisplay = new Intent(v.getContext(),
							ItemDisplay.class);
					itemDisplay.putExtra("item", v.getId());
					startActivity(itemDisplay);
				}
			});
			l.addView(customView);

			/*
			 * System.out.println("key_rowid => " + itemsCursor.getString(0));
			 * System.out.println("id => " + itemsCursor.getString(1));
			 * System.out.println("menu_id => " + itemsCursor.getString(2));
			 * System.out.println("cat_id => " + itemsCursor.getString(3));
			 * System.out.println("name => " + itemsCursor.getString(4));
			 * System.out.println("desc => " + itemsCursor.getString(5));
			 * System.out.println("price => " + itemsCursor.getString(6));
			 * System.out.println("number => " + itemsCursor.getString(7));
			 */

			count++;
			if (count <= itemsCursor.getCount()) {
				// just don't call moveToNext on the last run thru the loop
				itemsCursor.moveToNext();
			}
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
	
	
	//MENU
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
					Intent intent = new Intent(ItemSelection.this, MainHome.class);
					startActivity(intent);
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(ItemSelection.this, errorCopy,
						Toast.LENGTH_SHORT).show();
				return true;
			}
		case R.id.help:
			Intent intent2 = new Intent(ItemSelection.this, HelpHome.class);
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
