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

import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import com.wileynet.eatdude.xml.MessageSaxHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainHome extends Activity implements Runnable {
	// item notes added
	public static Vector<String> orders = new Vector<String>();
	private String pre = "daisy";
	// menu successfully downloaded and entered into database
	private ProgressDialog pd;
	private String errorCopy = "Internet Connection Error";
	public static boolean menu_success = false;
	public static String restaurant_message_detail_error = "please select a restaurant first";
	public static boolean in_restaurant = false;
	public static String restaurant_name = "Eat Dude!";
	public static String restaurant_phone = "none";
	public static String restaurant_address = "none";
	public static String restaurant_city = "none";

	private String remote_app_address;
	private String message;
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.eatdudemain);
		
		layout = (LinearLayout) findViewById(R.id.eatdude_main);
		// set the color
		layout.setBackgroundColor(Color.WHITE);
		ImageView image = (ImageView) findViewById(R.id.eatdudeicon);
		image.setImageResource(R.drawable.eatdudeicon);
		remote_app_address = this.getString(R.string.remote_app_address);

		// button on fail to connect
		final Button button1 = (Button) findViewById(R.id.mainbutton1);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				loadMessageXML();
			}
		});

		message = savedInstanceState != null ? savedInstanceState
				.getString("Message") : null;

	}

	protected void loadMessageXML() {

		pd = ProgressDialog.show(this, "one moment", "connecting eatdude.com",
				true, false);

		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// do nothing
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStart() {
		
			super.onStart();
			
			//license agreement
			PackageInfo versionInfo = getPackageInfo();
			
			final String eulaKey = pre + versionInfo.versionCode;
			final SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			boolean viewed = prefs.getBoolean(eulaKey, false);
			
			if (viewed == false) {

				String title = this.getString(R.string.app_name) + " v"
						+ versionInfo.versionName;

				String message = "";
				String update = this.getString(R.string.updates);

				if (!update.equals("")) {
					message = this.getString(R.string.updates) + "\n\n"
							+ this.getString(R.string.eula);
				}else{
					message = this.getString(R.string.eula);
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(this)
						.setTitle(title)
						.setMessage(message)
						.setPositiveButton("Yes",
								new Dialog.OnClickListener() {

									public void onClick(
											DialogInterface dialogInterface, int i) {
										SharedPreferences.Editor editor = prefs
												.edit();
										editor.putBoolean(eulaKey, true);
										editor.commit();
										dialogInterface.dismiss();
										startLoad();
									}
								})
						.setNegativeButton("No",
								new Dialog.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}

								});
				builder.create().show();
			}else{
				startLoad();
			}

	}
	
	
	private void startLoad(){
		//start this activity
		if (message == null || message.length() == 0) {
			loadMessageXML();
		} else {
			TextView textview = (TextView) layout
					.findViewById(R.id.eatdudemaintextview);
			textview.setText(message);

			final Button button1 = (Button) layout
					.findViewById(R.id.mainbutton1);
			button1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(MainHome.this,
							CountrySelection.class);
					startActivity(intent);
				}
			});
		}
	}
	
	
	
	private PackageInfo getPackageInfo() {
		PackageInfo pi = null;
		try {
			pi = this.getPackageManager().getPackageInfo(this.getPackageName(),
					PackageManager.GET_ACTIVITIES);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return pi;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("Message", message);
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainHome.in_restaurant = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	public void run() {
		ConnectivityManager netcon = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = netcon.getActiveNetworkInfo();

		try {
			if (ni.isConnected()) {
				String url = remote_app_address + "/xml/app_message/mh";
				MessageSaxHelper msh = new MessageSaxHelper();

				try {
					if (msh.parseContent(url)) {
						message = msh.message;

						// close loading window
						// add start button
						// app doesn't work without internet access
						// button on successful connection with eatdude.com
						final Button button1 = (Button) layout
								.findViewById(R.id.mainbutton1);
						button1.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								Intent intent = new Intent(MainHome.this,
										CountrySelection.class);
								startActivity(intent);
							}
						});

						handler.sendEmptyMessage(0);

					} else {
						pd.dismiss();
						errorhandler.sendEmptyMessage(0);
					}
				} catch (SAXException e) {
					pd.dismiss();
					errorhandler.sendEmptyMessage(0);
					e.printStackTrace();
				} catch (IOException e) {
					pd.dismiss();
					errorhandler.sendEmptyMessage(0);
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					pd.dismiss();
					errorhandler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			} else {
				pd.dismiss();
				errorhandler.sendEmptyMessage(0);
			}

		} catch (Exception e) {
			// network connection error // OR PHONE IN USE
			pd.dismiss();
			errorhandler.sendEmptyMessage(0);
		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			TextView textview = (TextView) layout
					.findViewById(R.id.eatdudemaintextview);
			textview.setText(message);
			pd.dismiss();
		}

	};

	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(MainHome.this, errorCopy, Toast.LENGTH_SHORT).show();
		}
	};

	// MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.call_restaurant_inactive:
			// Toast.makeText(CategorySelection.this, "call"
			// ,Toast.LENGTH_SHORT).show();
			errorAlertDialog();
			return true;
		case R.id.restaurant_details_inactive:
			// popup();
			errorAlertDialog();
			return true;
		case R.id.help:
			Intent intent = new Intent(MainHome.this, HelpHome.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
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
				Toast.makeText(MainHome.this, errorCopy, Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
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

}
