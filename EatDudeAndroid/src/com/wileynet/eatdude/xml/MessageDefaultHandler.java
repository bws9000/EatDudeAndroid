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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MessageDefaultHandler extends DefaultHandler {
    
	String _tmpValue;
	public String message;
	
	@Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {	
		
        //

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
    		
    		_tmpValue = new String(ch, start, length);
        
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
    	
    	if(localName.equalsIgnoreCase("s")){
    		message =  _tmpValue;
    	}
    }

}
