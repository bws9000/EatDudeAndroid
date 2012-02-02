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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDisplay extends Activity {

	private RestaurantDbAdapter mDbHelper;
	private String errorCopy = "Internet Connection Error";
	private TextView tv_item_number;
	private TextView tv_item_name;
	private TextView tv_item_desc;
	private TextView tv_item_price;
	private TextView tv_blank_space;

	private String item_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);
		MainHome.in_restaurant = true;

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			item_id = "" + extras.getInt("item");
		}

		this.setTitle(MainHome.restaurant_name);

		tv_item_number = (TextView) findViewById(R.id.item_number);
		tv_item_name = (TextView) findViewById(R.id.item_name);
		tv_item_desc = (TextView) findViewById(R.id.item_desc);
		tv_blank_space = (TextView) findViewById(R.id.blank_space);
		tv_item_price = (TextView) findViewById(R.id.item_price);

		mDbHelper = new RestaurantDbAdapter(this);
		try {
			mDbHelper.open();
			Cursor itemCursor = mDbHelper.fetchItem(item_id);
			startManagingCursor(itemCursor);

			// key_rowid => 0
			// id => 1
			// menu_id => 2
			// cat_id => 3
			// name => 4
			// desc => 5
			// price => 6
			// number => 7
			String tin = itemCursor.getString(7).trim();
			String tip = itemCursor.getString(6).trim();
			
			//System.out.println("=> " + tin);
			//System.out.println("=> " + tip);
			
			if(!tin.equals("")){
				tv_item_number.setText("# " + itemCursor.getString(7) + " -");
			}else{
				tv_item_number.setText("");
			}
			tv_item_name.setText( itemCursor.getString(4) );
			tv_item_desc.setText( itemCursor.getString(5) );
			tv_blank_space.setText("");
			
			if(!tip.equals("")){
				//added replace if they fuckup and added a $
				tv_item_price.setText("$" + itemCursor.getString(6).replace("$", ""));
			}else{
				tv_item_price.setText("");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		 * final Button button = (Button) findViewById(R.id.button02);
		 * button.setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) { StringUtils su = new StringUtils(); String
		 * addedMessage; if(!su.addOrder(item_id)){ addedMessage = "item noted";
		 * }else{ addedMessage = "already noted"; }
		 * Toast.makeText(button.getContext(), addedMessage,
		 * Toast.LENGTH_SHORT).show(); } });
		 */

		mDbHelper.close();
	}

	@Override
	protected void onStop() {
		// do nothing
		super.onStop();
	}

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
					Intent intent = new Intent(ItemDisplay.this, MainHome.class);
					startActivity(intent);
					return true;
				}
			} catch (Exception e) {
				Toast.makeText(ItemDisplay.this, errorCopy,
						Toast.LENGTH_SHORT).show();
				return true;
			}
		case R.id.help:
			Intent intent2 = new Intent(ItemDisplay.this, HelpHome.class);
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
		String message = "Are you sure you want to call "
				+ MainHome.restaurant_name + " now ? This call "
				+ "will be immediately dialed for you.";
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								call();
							}
						});
		builder.setMessage(message).setCancelable(false)
				.setNegativeButton("no", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
