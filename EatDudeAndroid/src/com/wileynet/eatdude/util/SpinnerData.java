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

import android.app.Application;

public class SpinnerData extends Application {
	public SpinnerData(String spinnerText, String value) {
		this.spinnerText = spinnerText;
		this.value = value;
	}

	public String getSpinnerText() {
		return spinnerText;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		String cc = spinnerText;
		return cc;
	}

	String spinnerText;
	String value;
}
