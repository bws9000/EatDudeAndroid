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

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Application;
import android.content.res.Configuration;

public class Restaurant extends Application{

	private String restaurant_id;
	private String restaurant_name;
	private String restaurant_phone;
	private String restaurant_address;
	
	//private String menu_id;
	//private String menu_name;
	private HashMap<String, String> menu =
		new HashMap<String,String>();
	
	private LinkedHashMap<String, String> menu_category = 
		new LinkedHashMap<String, String>();
	
	private LinkedHashMap<String, String> menu_item = 
		new LinkedHashMap<String, String>();
	
	public Restaurant(String id,
								    String name,
								    String phone,
								    String address,
								    //String m_id,
								    //String m_name,
								    HashMap<String,String> menu,
								    LinkedHashMap<String, String> m_category,
								    LinkedHashMap<String, String> m_item){
		
		setRestaurant_id(id);
		setRestaurant_name(name);
		setRestaurant_phone(phone);
		setRestaurant_address(address);
		//setMenu_id(m_id);
		//setMenu_name(m_name);
		setMenu(menu);
		setMenu_category(m_category);
		setMenu_item(m_item);
	}
	
	public String getRestaurant_id(){
		return restaurant_id;
	}
	public void setRestaurant_id(String restaurant_id){
		if(!restaurant_id.equals("")){
			this.restaurant_id = restaurant_id;
		}else{
			this.restaurant_id = "error";
		}
	}
	
	public String getRestaurant_name(){
		return restaurant_name;
	}
	public void setRestaurant_name(String restaurant_name){
		if(!restaurant_name.equals("")){
			this.restaurant_name = restaurant_name;
		}else{
			this.restaurant_name = "error";
		}
	}
	
	public String getRestaurant_phone(){
		return restaurant_phone;
	}
	public void setRestaurant_phone(String restaurant_phone){
		if(!restaurant_phone.equals("")){
			this.restaurant_phone = restaurant_phone;
		}else{
			this.restaurant_phone = "error";
		}
	}
	
	public String getRestaurant_address(){
		return restaurant_address;
	}
	public void setRestaurant_address(String restaurant_address){
		if(!restaurant_address.equals("")){
			this.restaurant_address = restaurant_address;
		}else{
			this.restaurant_address = "error";
		}
	}
	
	/*
	public String getMenu_id(){
		return menu_id;
	}
	public void setMenu_id(String menu_id){
		if(!menu_id.equals("")){
			this.menu_id = menu_id;
		}else{
			this.menu_id = "error";
		}
	}
	
	public String getMenu_name(){
		return menu_name;
	}
	public void setMenu_name(String menu_name){
		if(!menu_name.equals("")){
			this.menu_name = menu_name;
		}else{
			this.menu_name = "error";
		}
	}
	*/
	
	public HashMap<String,String> getMenu(){
		return menu;
	}
	public void setMenu(HashMap<String,String> menu){
		    
			this.menu = menu;
		
	}
	
	public LinkedHashMap<String,String> getMenu_category(){
		return menu_category;
	}
	public void setMenu_category(LinkedHashMap<String,String> menu_category){
		    
			this.menu_category = menu_category;
		
	}
	
	public LinkedHashMap<String,String> getMenu_item(){
		return menu_item;
	}
	public void setMenu_item(LinkedHashMap<String,String> menu_item){
		
			this.menu_item = menu_item;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
}
