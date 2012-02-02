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

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

import com.wileynet.eatdude.db.RestaurantDbAdapter;

public class RestaurantDbHelper extends Activity  {

	private RestaurantDbAdapter mDbHelper;
	private final Context mCtx;
    
	public RestaurantDbHelper(Context ctx){
		this.mCtx = ctx;
	}



public String[] getCategoryArrayFromDb(String m_id){
		
		String[] categories;
		categories = null;
		
		try{
		mDbHelper = new RestaurantDbAdapter(mCtx);
		mDbHelper.open();
		
		Cursor category = mDbHelper.fetchAllCategories(m_id);
		categories = new String[category.getCount()];
		startManagingCursor(category);
		for(int i=0;i<category.getCount();i++){
			categories[i]=category.getString(3);
			//System.out.println("------------------------------> " + i + " - " + category.getString(3));
			if(i <= category.getCount()){
				category.moveToNext();
			}
		}
		return categories;
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return categories;
	}



	/*
	public void createRestaurantFromDb(){
		
		try{
		mDbHelper = new RestaurantDbAdapter(mCtx);
		mDbHelper.open();
		Cursor restaurant = mDbHelper.fetchAllRestaurants(); //only 1 restaurant allowed at this time
		startManagingCursor(restaurant);
		
		restaurant_id = restaurant.getString(1);
		restaurant_name = restaurant.getString(2);
		
		Cursor menu = mDbHelper.fetchAllMenus();//working with one at the moment
		startManagingCursor(menu);
		menu_id = menu.getString(1);
		menu_name = menu.getString(2);
		
		Cursor category = mDbHelper.fetchAllCategories();
		startManagingCursor(category);
		count=1;
		for(int i=0;i<category.getCount();i++){
			menu_category.put(category.getString(1), category.getString(2));
			if(count <= category.getCount()){
				category.moveToNext();
			}
		}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	*/

}
