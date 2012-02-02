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

package com.wileynet.eatdude.xml;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
//import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

//import com.wileynet.eatdude.RestaurantSelection;

public class SAXHelper {
	
	public SAXHelper(){}
	
	public String restaurant_id;
	public String restaurant_name;
	public String restaurant_phone;
	public String restaurant_address;
	
	//public String menu_id;
	//public String menu_name;
	
	public HashMap<String, String> menu =
		new HashMap<String, String>();
	
	public LinkedHashMap<String, String> menu_category = 
		new LinkedHashMap<String, String>();
	
	public LinkedHashMap<String, String> menu_item = 
		new LinkedHashMap<String, String>();
    
	public void parseContent(String parseContent) {
        
    	MyDefaultHandler df = new MyDefaultHandler();
        
        try {
        	
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            
            URL url = new URL(parseContent);
            sp.parse(new InputSource(url.openStream()), df);
            
            restaurant_id = df.restaurant_id;
            restaurant_name = df.restaurant_name;
            restaurant_phone= df.restaurant_phone;
            restaurant_address = df.restaurant_address;
            //menu_id = df.menu_id;
            //menu_name = df.menu_name;
            menu = df.menu;
            menu_category = df.menu_category;
            menu_item = df.menu_item;
           
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
