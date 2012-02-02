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
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EatDudeSplash extends Activity {

	private HashMap<String, String> info = new HashMap<String, String>();
	TextView selection;
	private int count;
	private String errorCopy = "Internet Connection Error";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.eatdudesplash);
		LinearLayout layout = (LinearLayout) findViewById(R.id.eatdude_splash);
		// set the color
		layout.setBackgroundColor(Color.WHITE);
		ImageView image = (ImageView) findViewById(R.id.eatdudeicon);
		image.setImageResource(R.drawable.eatdudeicon);

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			Toast.makeText(parent.getContext(),
					"The planet is " + view.getTag(), Toast.LENGTH_LONG).show();
		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	protected boolean loadCountryXML() throws SAXException, IOException,
			ParserConfigurationException {

		ConnectivityManager netcon = 
			(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = netcon.getActiveNetworkInfo();
		boolean isConnected = ni.isConnected();
		Boolean success = true;

		if (isConnected) {
			String url = "http://wileynet5.appspot.com/xml/country/all";
			GeoSaxHelper gsh = new GeoSaxHelper();

			if (gsh.parseContent(url)) {
				info = gsh.info;
			} else {
				success = false;
			}
		} else {
			success = false;
		}

		return success;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		try {
			if (loadCountryXML()) {

				count = 1;
				// String[] countries = new String[info.size() + 1];
				final SpinnerData countries[] = new SpinnerData[info.size() + 1];
				countries[0]= new SpinnerData("select","select");

				for (Map.Entry<String, String> c : info.entrySet()) {
					// System.out.println("-----> " + c.getValue() + " -- " +
					// c.getKey() );
					countries[count] = new SpinnerData(c.getValue(), c.getKey());
					count++;
				}
				info = null;

				Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
				ArrayAdapter<SpinnerData> adapter = new ArrayAdapter<SpinnerData>(
						this, android.R.layout.simple_spinner_item, countries);

				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);

				spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent, View v,int pos, long id) {
						SpinnerData d = countries[pos];
						String sval = d.getValue();
						if(sval.equals("select")){
							//do nothing
						}else{
							//Toast.makeText(EatDudeSplash.this, "" + sval,Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(EatDudeSplash.this,StateSelection.class);
							intent.putExtra("country_id", sval);
							startActivity(intent);
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {
						// do nothing
					}
				});
			} else {
				Toast.makeText(EatDudeSplash.this, errorCopy, Toast.LENGTH_LONG)
						.show();
			}

		} catch (SAXException e) {
			e.printStackTrace();
			Toast.makeText(EatDudeSplash.this, errorCopy + "(SAXException)",
					Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(EatDudeSplash.this, errorCopy + "(IOException)",
					Toast.LENGTH_LONG).show();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			Toast.makeText(EatDudeSplash.this,
					errorCopy + "(ParserConfigurationException)",
					Toast.LENGTH_LONG).show();
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

}
