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

import java.util.HashMap;
import java.util.Scanner;

import com.krzygorz.calculator.gui.CalculatorWindow;
import com.krzygorz.calculator.misc.Logger;
import com.krzygorz.calculator.misc.SettingsManager;
import com.krzygorz.calculator.parser.MathParser;
import com.krzygorz.calculator.tree.ExpressionPart;


public class Main {
	//public static HashMap<String, Object> settings = new HashMap<String, Object>();
	
	public static void main(String[] args) {
		SettingsManager.loadSettings(args);
		
		for(String i : SettingsManager.getSettings().keySet()){
			System.out.println(i + ": " + SettingsManager.getSetting(i));
		}
		MathParser parser = new MathParser();
		Scanner inputScanner = new Scanner(System.in);
		System.out.println("enter expression to simplyfy");
		String input = inputScanner.nextLine();
		try {
			ExpressionPart parsedInput = parser.parseString(input); 
			System.out.println("result tree: " + parsedInput.toString());
			
			System.out.println("calculating:");
			String toOutput = "";
			int i = 0;
			
			while(parsedInput.canBeSimplified() && i < 100){
				toOutput = toOutput.concat(parsedInput.toString());
				toOutput = toOutput.concat(" = "); 
				parsedInput = parsedInput.simplyfy();
				i++;
			}
			toOutput = toOutput.concat(parsedInput.toString());
			System.out.println("result: "+toOutput);
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		inputScanner.close();
	}
	
}
