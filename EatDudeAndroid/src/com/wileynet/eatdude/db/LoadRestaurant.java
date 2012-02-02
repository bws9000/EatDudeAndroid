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

package com.wileynet.eatdude.db;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import android.content.Context;

import com.wileynet.eatdude.MainHome;
import com.wileynet.eatdude.RestaurantSelection;

public class LoadRestaurant {
	/* load restaurant into the db */
	private RestaurantDbAdapter mDbHelper;
	private final Context mCtx;
	private HashMap<String, String> menumap = new HashMap<String, String>();
	private int count = 0;
	
	public LoadRestaurant(Context ctx){
		this.mCtx = ctx;
	}

	public void load() {
		
		mDbHelper = new RestaurantDbAdapter(mCtx);
		menumap = RestaurantSelection.r.getMenu();
		
		try {
			mDbHelper.open();
			if (RestaurantSelection.dbClear == 0) {
				// clear all tables
				mDbHelper.clearTables();
				
				MainHome.restaurant_address = RestaurantSelection.r.getRestaurant_address();
				MainHome.restaurant_phone = RestaurantSelection.r.getRestaurant_phone();
				MainHome.restaurant_name = RestaurantSelection.r.getRestaurant_name();
				
				// restaurant
				mDbHelper.insertRestaurant(
						RestaurantSelection.r.getRestaurant_id(),
						MainHome.restaurant_address,
						MainHome.restaurant_phone,
						MainHome.restaurant_address);

				// menu
				for (Map.Entry<String, String> m : menumap.entrySet()) {
					String menu_id = m.getKey();
					String menu_name = m.getValue();
					mDbHelper.insertMenu(menu_id, menu_name);
					// System.out.println(">> menu : " + menu_id + " => " +
					// menu_name);

					// category
					for (Map.Entry<String, String> cat : RestaurantSelection.r
							.getMenu_category().entrySet()) {
						String _key = cat.getKey();
						String _value = cat.getValue();

						String ckeys[];
						ckeys = new String[2]; // 0=menu_id,1=cat_id
						StringTokenizer ctoken = new StringTokenizer(_key, "|");

						count = 0;
						while (ctoken.hasMoreElements()) {
							ckeys[count] = (String) ctoken.nextElement();
							count++;
						}
						
						if (ckeys[0].equals(menu_id)) {
							// System.out.println("-------------> " + ckeys[1] +
							// " => " + _value );
							mDbHelper.insertCategory(menu_id, ckeys[1], _value);
						}
						

					}

					// item
					for (Map.Entry<String, String> entry : RestaurantSelection.r
							.getMenu_item().entrySet()) {

						String key = entry.getKey();// item id
						String value = entry.getValue();// string menu_id |
														// cat_id | name |
														// description | price |
														// number
						// System.out.println("==============================");
						// System.out.println("KEY = " + key);
						// System.out.println("VALUE = " + value);
						// System.out.println("==============================");

						String meta[];
						meta = new String[6]; // 0=menu_id,1=cat_id,2=name,3=desc,4=price,5=number

						StringTokenizer token = new StringTokenizer(value, "|");

						count = 0;

						while (token.hasMoreElements()) {

							if (count <= 5) {
								meta[count] = (String) token.nextElement();
							}
							count++;
						}

						if (meta[0].equals(menu_id)) {
							// System.out.println(menu_id + "			item ----> " +
							// key + " => " + meta[2] );
							// insertItem(String id, String menu_id, String
							// cat_id, String name, String desc, String price,
							// String number)
							/*
							 * System.out.println("=============================="
							 * ); System.out.println("key : " + key);
							 * System.out.println("m0 : " + meta[0]);
							 * System.out.println("m1 : " + meta[1]);
							 * System.out.println("m2 : " + meta[2]);
							 * System.out.println("m3 : " + meta[3]);
							 * System.out.println("m4 : " + meta[4]);
							 * System.out.println("m5 : " + meta[5]);
							 * System.out.
							 * println("==============================");
							 */

							mDbHelper.insertItem(key, meta[0], meta[1],
									meta[2], meta[3], meta[4], meta[5]);
						}

					}
					RestaurantSelection.dbClear = 1;
					MainHome.menu_success = true;
					
				}
				mDbHelper.close();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
