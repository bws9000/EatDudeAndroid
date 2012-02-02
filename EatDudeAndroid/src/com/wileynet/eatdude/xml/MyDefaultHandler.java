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

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.wileynet.eatdude.util.StringUtils;

public class MyDefaultHandler extends DefaultHandler {
    
	//String _tmpValue;
	String _tmpMenuID;
	String _tmpCategoryID;
	String _tmpItemID;
	String tmpv;
	
    String restaurant_id;
    String restaurant_name;
    String restaurant_phone;
    String restaurant_address;
    String menu_id;
    String menu_name;
    
    StringUtils su = new StringUtils();
    StringBuilder _tmpValue = new StringBuilder();

    
    HashMap<String, String> menu =
    	new HashMap<String, String>();
    
    LinkedHashMap<String, String> menu_category = 
    	new LinkedHashMap<String, String>();
    
    LinkedHashMap<String, String> menu_item = 
		new LinkedHashMap<String, String>();
	
	@Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {	
		
        if (localName.equalsIgnoreCase("restaurant")) {
        	restaurant_id = attributes.getValue("id");
        	restaurant_name = su.checkForBadStrings(attributes.getValue("name"));
        	restaurant_phone = su.checkForBadStrings(attributes.getValue("phone"));
        	restaurant_address = su.checkForBadStrings(attributes.getValue("address"));
        }
        if (localName.equalsIgnoreCase("menu")) {
        	//menu_id = attributes.getValue("id");
        	//menu_name = attributes.getValue("name");
        	menu.put(attributes.getValue("id"), su.checkForBadStrings(attributes.getValue("name")));
        	_tmpMenuID = attributes.getValue("id");
        }
        if (localName.equalsIgnoreCase("category")) {
        	menu_category.put(_tmpMenuID + "|" + attributes.getValue("id"), su.checkForBadStrings(attributes.getValue("name")));
        	//System.out.println(_tmpMenuID + "|" + attributes.getValue("id") + "|" + su.checkForBadStrings(attributes.getValue("name")));
        	//System.out.println("_tmpMenuID = " + _tmpMenuID);
        	//System.out.println("catid : " + attributes.getValue("id") + " = " + attributes.getValue("name") );
        	_tmpCategoryID = attributes.getValue("id");
        }
        if (localName.equalsIgnoreCase("item")) {
        	tmpv =  _tmpMenuID + "|" + _tmpCategoryID + "|" + su.checkForBadStrings(attributes.getValue("name")) + "|";
        	menu_item.put(attributes.getValue("id"),"");
        	_tmpItemID = attributes.getValue("id");
        }

    }

    
	@Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
			
			_tmpValue.append(su.checkForBadStrings(new String(ch, start, length)));
    		//_tmpValue = su.checkForBadStrings(new String(ch, start, length));
        
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    	
    	if(localName.equalsIgnoreCase("desc")){
    		tmpv +=  _tmpValue;
    		_tmpValue.setLength(0);
    		menu_item.put( _tmpItemID, tmpv );
    	}
    	if(localName.equalsIgnoreCase("price")){
    		tmpv += "|" + _tmpValue;
    		_tmpValue.setLength(0);
    		menu_item.put( _tmpItemID, tmpv );
    	}
    	if(localName.equalsIgnoreCase("number")){
    			tmpv += "|" + _tmpValue;
    			_tmpValue.setLength(0);
    			menu_item.put( _tmpItemID, tmpv );
    	}
    }

}
