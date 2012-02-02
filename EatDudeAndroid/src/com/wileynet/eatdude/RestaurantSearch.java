package com.wileynet.eatdude;

import java.util.Map;
import java.util.Vector;

import com.wileynet.eatdude.xml.SAXHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import android.widget.Toast;

public class RestaurantSearch extends Activity {

	public static Restaurant r;
	public static Vector<String> orders = new Vector<String>();
	private String menu_id;
	public static int dbClear = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_restaurant);

		// Proguard will rename the onClick method and break it unless you
		// specifically tell it not to,
		// or you can just turn off proguard which is easier.
		// http://forum.xda-developers.com/showthread.php?t=777399

		// I got this error from the xml layout for a ImageButton. I was using
		// api 3 which apparently
		// does not support this attribute. Simply going to api 4 resolved this
		// problem.
		// http://blog.jamesbaca.net/?p=40

		// public void selfDestruct(View view) {
		// Kabloey
		// }

		final Button button = (Button) findViewById(R.id.button01);
		button.setOnClickListener(new View.OnClickListener() {
			Intent i;

			public void onClick(View v) {
				if (loadRestaurantXML()) {
					if (r.getMenu().size() > 1) {
						i = new Intent(RestaurantSearch.this,
								MenuSelection.class);
					} else {
						for (Map.Entry<String, String> entry : r.getMenu()
								.entrySet()) {
							menu_id = "" + entry.getKey();
						}
						i = new Intent(RestaurantSearch.this,
								CategorySelection.class);
						i.putExtra("menu_id", menu_id);
					}
				} else {
					Toast.makeText(button.getContext(),
							"Internet Connection Error", Toast.LENGTH_SHORT)
							.show();
				}

				startActivity(i);
			}
		});

	}

	protected boolean loadRestaurantXML() {

		Boolean success = true;
		try {
			String url = "http://wileynet5.appspot.com/example_xml/23001";
			SAXHelper sh = new SAXHelper();

			sh.parseContent(url);
			r = new Restaurant(sh.restaurant_id, sh.restaurant_name, sh.menu,
					sh.menu_category, sh.menu_item);
			// sh = null;
		} catch (Exception e) {
			success = false;
		}

		return success;
	}

}
