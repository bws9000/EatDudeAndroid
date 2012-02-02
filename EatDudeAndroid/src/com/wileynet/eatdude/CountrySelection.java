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
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.wileynet.eatdude.util.SpinnerData;
import com.wileynet.eatdude.xml.GeoSaxHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CountrySelection extends Activity implements Runnable {

	private HashMap<String, String> info = new HashMap<String, String>();
	TextView selection;
	private int count;
	private String errorCopy = "Internet Connection Error";
	private ProgressDialog pd;
	private ArrayAdapter<SpinnerData> adapter;
	private Spinner spinner;
	private SpinnerData[] countries;
	private String remote_app_address;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.eatdudesplash);
		MainHome.in_restaurant = false;
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.eatdude_splash);
		// set the color
		layout.setBackgroundColor(Color.WHITE);
		ImageView image = (ImageView) findViewById(R.id.eatdudeicon);
		image.setImageResource(R.drawable.eatdudeicon);
		TextView textview = (TextView) findViewById(R.id.geoTextview);
		textview.setText("Select Country");
		remote_app_address = this.getString(R.string.remote_app_address);

		if (getLastNonConfigurationInstance() != null) {
			info = (HashMap<String, String>) getLastNonConfigurationInstance();
		}
	}

	protected void  loadCountryXML() {

		pd = ProgressDialog.show(this, "one moment ...", " Loading Selection ",
				true, false);

		Thread thread = new Thread(this);
		thread.start();

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//do nothing
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (info.size() < 1) {
			loadCountryXML();
		}else{
			count = 1;
			countries = new SpinnerData[info.size() + 1];
			countries[0] = new SpinnerData("select", "select");

			for (Map.Entry<String, String> c : info.entrySet()) {
				countries[count] = new SpinnerData(c.getValue(),
						c.getKey());
				count++;
			}
			// info = null;

			spinner = (Spinner) findViewById(R.id.country_spinner);
			adapter = new ArrayAdapter<SpinnerData>(this,
					android.R.layout.simple_spinner_item, countries);
		}

	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		final HashMap<String, String> data = info;
		return data;
		// return super.onRetainNonConfigurationInstance();
	}

	public void run() {
		ConnectivityManager netcon = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = netcon.getActiveNetworkInfo();
		
		
		try {
			if (ni.isConnected()) {
				String url = remote_app_address + "/xml/country/all";
				GeoSaxHelper gsh = new GeoSaxHelper();

				try {
					if (gsh.parseContent(url)) {
						info = gsh.info;
						if (info.size() < 1) {
							emptyhandler.sendEmptyMessage(0);
						}
						count = 1;
						countries = new SpinnerData[info.size() + 1];
						countries[0] = new SpinnerData("select", "select");

						for (Map.Entry<String, String> c : info.entrySet()) {
							countries[count] = new SpinnerData(c.getValue(),
									c.getKey());
							count++;
						}
						// info = null;

						spinner = (Spinner) findViewById(R.id.country_spinner);
						adapter = new ArrayAdapter<SpinnerData>(this,
								android.R.layout.simple_spinner_item, countries);

						// close loading window
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

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View v,
						int pos, long id) {
					SpinnerData d = countries[pos];
					String sval = d.getValue();
					if (sval.equals("select")) {
						// do nothing
					} else {
						// Toast.makeText(EatDudeSplash.this, "" +
						// sval,Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CountrySelection.this,
								StateSelection.class);
						intent.putExtra("country_id", sval);
						startActivity(intent);
					}
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// do nothing
				}
			});

			pd.dismiss();
		}

	};

	private Handler errorhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(CountrySelection.this, errorCopy, Toast.LENGTH_SHORT)
					.show();
		}
	};

	private Handler emptyhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(CountrySelection.this, "please try again later",
					Toast.LENGTH_SHORT).show();
			finish();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();

		if (adapter != null) {
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner.setAdapter(adapter);

			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View v,
						int pos, long id) {
					SpinnerData d = countries[pos];
					String sval = d.getValue();
					if (sval.equals("select")) {
						// do nothing
					} else {
						// Toast.makeText(EatDudeSplash.this, "" +
						// sval,Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(CountrySelection.this,
								StateSelection.class);
						intent.putExtra("country_id", sval);
						startActivity(intent);
					}
				}

				public void onNothingSelected(AdapterView<?> parent) {
					// do nothing
				}
			});
		}
		MainHome.in_restaurant = false;
	}

	// MENU
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu_before_restaurant_select, menu);
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
		case R.id.home:
			Intent intent = new Intent(CountrySelection.this,
					MainHome.class);
			startActivity(intent);
			return true;
		case R.id.help:
			Intent intent2 = new Intent(CountrySelection.this,
					HelpHome.class);
			startActivity(intent2);
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
				Toast.makeText(CountrySelection.this, errorCopy,
						Toast.LENGTH_SHORT).show();
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
