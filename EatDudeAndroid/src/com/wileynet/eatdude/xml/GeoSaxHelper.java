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

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class GeoSaxHelper {
	
	public GeoSaxHelper(){}
	public HashMap<String, String> info =
		new HashMap<String, String>();
	
	public boolean parseContent(String parseContent) throws SAXException, 
															IOException, ParserConfigurationException {
        
		boolean success = true;
    	GeoDefaultHandler gdf = new GeoDefaultHandler();
        	
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
            
        URL url = new URL(parseContent);
        try{
        	sp.parse(new InputSource(url.openStream()), gdf);
        }catch(UnknownHostException e){
        	success = false;
        }catch(Exception e){
        	success = false;
        }
            
        info = gdf.info;
        
        return success;
    }
}
