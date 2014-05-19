/*
    Java Scientific Calculator
    Copyright (C) 2014  krzygorz

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.krzygorz.calculator.misc;

import java.util.HashMap;

public final class SettingsManager {
	
	private static HashMap<String, String> settings = new HashMap<String, String>();
	
	private SettingsManager() {
		
	}

	public static HashMap<String, String> getSettings() {
		return settings;
	}
	
	public static String getSetting(String key){
		return settings.get(key);
	}

	public static void setSetting(String key, String setting) {
		SettingsManager.settings.put(key, setting);
	}
	
	public static void loadSettings(String[] args){
		for(String i : args){
			if(i.split("=").length < 2){
				settings.put(i, "1");
			}else{
				settings.put(i.split("=")[0], i.split("=")[1]);
			}
		}
	}
}
