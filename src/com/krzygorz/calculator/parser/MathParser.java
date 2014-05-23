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

package com.krzygorz.calculator.parser;

import java.util.Arrays;
import java.util.Vector;

import com.krzygorz.calculator.tree.*;
import com.krzygorz.calculator.tree.Number;

public class MathParser {//TODO wieksze mozliwosci konfiguracji z argumentow/config file(debugMode czy simplyfyFractions)
	public MathParser() {

	}
	public int getCharType(char input){//char types: 0: numbers 1: operators 2: brackets 3: variables
		if((input == '1') || (input == '2') || (input == '3') || (input == '4') || (input == '5') || (input == '6') || (input == '7') || (input == '8') || (input == '9') || (input == '0') || (input == '.')){
			return 0;
		}else if((input == '+') || (input == '-') || (input == '*') || (input == '/') || (input == '^') || (input == '\\') || (input == '='))
			return 1;
		else if((input == '(') || (input == ')')) 
			return 2;
		else if((input == 'x') || (input == 'y') || (input == 'z')) 
			return 3;
		else
			return 4;
	}

	public boolean canBeConcated(String a, char b){
		if(a.equals("")){
			return true;
		}
		if(a.equals(" ")){
			return false;
		}
		if(this.getCharType(a.charAt(a.length() - 1)) != getCharType(b) || getCharType(a.charAt(a.length() - 1)) == 2 || getCharType(b) == 2){//one token can have only characters at the same type
			return false;
		}
		return true;
	}

	public int getPriority(String op){
		if(op.equals("=")){
			return 0;
		}else if(op.equals("+") || op.equals("-")){
			return 1;
		}else if(op.equals("*") || op.equals("/")){
			return 2;
		}else if(op.equals("\\")){   //fractions
			return 3;
		}else if(op.equals("^") || op.equals("sqrt")){
			return 4;
		}else if(op.length() == 1 && getCharType(op.charAt(0)) == 0){
			return 5;
		}else{
			return 6;
		}
	}

	public Vector<Token> splitToTokens(String input){//TODO laczenie minusa z liczbami jesli operator przed minusem ma typ inny niz 0
		Vector<Token> output = new Vector<Token>();//seperate text to tokens, example: 10+5.4 -> 10, +, 5.4
		String currToken = "";
		int lastType = getCharType(input.charAt(0));
		for(int i = 0;i < input.length();i++){
			if(getCharType(input.charAt(i)) == 0 | getCharType(input.charAt(i)) == 3){
				currToken += input.charAt(i);
			}else if(getCharType(input.charAt(i)) == 1){
				if(!"".equals(currToken)){
					output.add(new Token(currToken, getCharType(currToken.charAt(0))));
					currToken = "";
				}
				currToken += input.charAt(i);
				if(input.charAt(i) == '-' && lastType != 0);
				else{
					output.add(new Token(currToken, 1));
					currToken = "";
				}
			}else if(input.charAt(i) == '('){
				int cntr = 0, bracketLevel = 0;
				String abc = "";
				while(bracketLevel != 0 || cntr == 0){
					abc += input.charAt(i);
					if(input.charAt(i) == '(')
						bracketLevel ++;
					else if(input.charAt(i) == ')')
						bracketLevel --;
					i++;
				}
				output.add(new Token(abc,4));
			}else{
				currToken += input.charAt(i);
			}
			lastType = getCharType(input.charAt(i));
		}
		output.add(new Token(currToken, this.getCharType(currToken.charAt(0))));
		return output;
	}
	private int search(Object[] array, Object[] tokens, int direction){
		int x = -1;
		if(direction == 0){
			for(int i = 0;i < array.length;i++)
				for(int j = 0;j < tokens.length;j++)
					if(array[i] == tokens[j])
						if(x == -1)
							x = i;
						else if(x > i)
							x = i;
		}else{
			for(int i = 0;i < array.length;i++)
				for(int j = 0;j < tokens.length;j++)
					if(array[i] == tokens[j])
						if(x < i)
							x = i;
		}
		return x;
	}
	private String[][] searchArray(Vector<Token> input, String[] tokens, int direction){
		Vector<String> temp = new Vector<String>();
		for(Token i : input)
			temp.add(i.getToken());
		String[] array = temp.toArray(new String[0]);
		if(direction == 0){ // left-to-right
			int maxIndex = search(array, tokens, 0);
			if(maxIndex == -1)
				return new String[][]{array};
			else{
				String[][] abcc = new String[3][];
				abcc[0] = Arrays.copyOfRange(array, 0, maxIndex);
				abcc[1] = Arrays.copyOfRange(array, maxIndex, maxIndex+1);
				abcc[2] = Arrays.copyOfRange(array, maxIndex+1, array.length);
				return abcc;
			}
		}else{ // right-to-left
			int maxIndex = search(array, tokens, 1);
			if(maxIndex == -1)
				return new String[][]{array};
			else{
				String[][] abcc = new String[3][];
				abcc[0] = Arrays.copyOfRange(array, 0, maxIndex);
				abcc[1] = Arrays.copyOfRange(array, maxIndex, maxIndex+1);
				abcc[2] = Arrays.copyOfRange(array, maxIndex+1, array.length);
				return abcc;
			}
		}
	}
	
	public ExpressionPart parseTokens(Vector<Token> input) throws Exception{//TODO INNY PARSER
		if(input.size() == 1){
			if(input.elementAt(0).getType() == 0)
				return new Number(Double.parseDouble(input.elementAt(0).getToken()));
			else if(input.elementAt(0).getType() == 3)
				return new Variable(input.elementAt(0).getToken());
		}
		ExpressionPart zet = null;
		for(int i = 0;i < input.size();i++){
			if(searchArray(input, new String[]{"+", "-"}, 1).length > 1){
				if(search(input.toArray(new Token[0]), new Token[]{new Token("+", 2)}, 1) > search(input.toArray(new Token[0]), new Token[]{new Token("-", 2)}, 1)){
					zet = new Addition();
				}
			}
		}
		return zet;
	}
	
	/*private ExpressionPart addTokensToSide(ExpressionPart op, bool left){
		if(left){
			
		}else{
			
		}
	}*/

	public ExpressionPart parseString(String input) throws Exception{
		Vector<Token> tokens = splitToTokens(input);
		ExpressionPart returnVal = parseTokens(tokens);
		return returnVal;
	}
}
