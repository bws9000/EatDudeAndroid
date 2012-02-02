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

package com.wileynet.eatdude.util;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import android.app.Application;

public class StringUtils extends Application {
	
	public StringUtils() {}
	
	public String checkForBadStrings(String in){
		
		String oldstring = in;
		String newstring;
		String newstring2;
		String newstring3;
		//String newstring4;
		
		newstring = oldstring.replaceAll("'","");
		//check for apastropies - remove for now
		//newstring = oldstring.replaceAll("'","");
		
		//check for pipe - remove for now
		newstring2 = newstring.replaceAll("[|]","");
		
		//check for forward slash
		newstring3 = newstring2.replaceAll("[/]","-");
		
		//special chars
		//newstring4 = StringEscapeUtils.unescapeXml(newstring3);
		//System.out.println("-------------------> " + newstring3);
		
		return newstring3;
	}
	
	public boolean addOrder(String order_id){
		
		boolean check = false;
		Vector<String> orders = com.wileynet.eatdude.MainHome.orders;
		
		if(orders.isEmpty()){
			com.wileynet.eatdude.MainHome.orders.add(order_id);
		}else{
			Iterator<String> itr = orders.iterator();
			while(itr.hasNext()){
				String item = itr.next();
				if(order_id.equals(item)){
						check = true;
				}
			}
		}
		
		if(!check){
			com.wileynet.eatdude.MainHome.orders.add(order_id);
		}
		
		return check;
		
	}
	
	public String capitalizeFirstLetter(String s) {
        
	    final StringTokenizer st = new StringTokenizer( s, " ", true );
	    final StringBuilder sb = new StringBuilder();
	     
	    while ( st.hasMoreTokens() ) {
	        String token = st.nextToken();
	        token = String.format( "%s%s",
	                                Character.toUpperCase(token.charAt(0)),
	                                token.substring(1) );
	        sb.append( token );
	    }
	    
	    return sb.toString();            
	}
	

}
