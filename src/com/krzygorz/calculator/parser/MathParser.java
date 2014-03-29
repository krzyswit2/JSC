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

import java.util.Vector;

import com.krzygorz.calculator.logic.Addition;
import com.krzygorz.calculator.logic.Division;
import com.krzygorz.calculator.logic.Equation;
import com.krzygorz.calculator.logic.Exponentiation;
import com.krzygorz.calculator.logic.ExpressionPart;
import com.krzygorz.calculator.logic.Fraction;
import com.krzygorz.calculator.logic.Multiplication;
import com.krzygorz.calculator.logic.Number;
import com.krzygorz.calculator.logic.Substraction;
import com.krzygorz.calculator.logic.Variable;

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
		if(this.getCharType(a.charAt(a.length() - 1)) != getCharType(b)){//one token can have only characters at the same type
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
		for(int i = 0; i < input.length(); i++){
			char currChar = input.charAt(i);
			//if(currChar == ' '){
			//output.add(new Token(currToken, this.getCharType(input.charAt(i - 1))));
			//currToken = "";
			//continue;
			//}
			System.out.println("current char: "+currChar);
			System.out.println("last type: "+lastType);
			lastType = this.getCharType(currChar);
			
			if(!canBeConcated(currToken, currChar)) {
				System.out.println("adding token: " + currToken);
				output.add(new Token(currToken, this.getCharType(currToken.charAt(0))));
				currToken = String.valueOf(currChar);
			} else {
				currToken = currToken.concat(String.valueOf(currChar));
			}

		}
		
		output.add(new Token(currToken, this.getCharType(currToken.charAt(0))));
		
		return output;
	}

	public ExpressionPart parseTokens(Vector<Token> input) throws Exception{//TODO INNY PARSER
		System.out.println("parsing tokens");
		
		if(input.size() == 0){
			System.out.println("no tokens to parse!");
			return null;
		}
		
		for(int k = 0; k < input.size(); k++){
			System.out.println("next token: "+input.get(k).getToken()+" type: "+input.get(k).getType());
		}

		if(input.size() == 1 && input.get(0).getType() == 0){
			System.out.println("only one number to parse");
			return new Number(Double.parseDouble(input.get(0).getToken()));
		}
		if(input.size() == 1 && input.get(0).getType() == 3){
			System.out.println("only one variable to parse");
			return new Variable(input.get(0).getToken());
		}

		ExpressionPart returnVal = null;
		System.out.println("amount of tokens: " + input.size());//I'm working on new parser, i don't like the way current one is functioning
		
		/*
		//int bracketLevel = 0;
		int tmpBracketLevel = 0;
		int i = input.size() - 1;
		boolean bracketFlag = false;
		if(input.get(0).getToken().equals("(") && input.get(i).getToken().equals(")")){
			bracketFlag = true;
			for(int j = 0; j < i; j++){
				Token currToken = input.get(j);
				if(currToken.getToken().equals("(")){
					tmpBracketLevel ++;
				}
				if(currToken.getToken().equals(")")){
					tmpBracketLevel --;
					if(tmpBracketLevel == 0 && j < i-1){
						bracketFlag = false;
						break;
					}
				}
			}
		}
		if(bracketFlag){
			i = input.size() - 2;
		}
		String currentOp = input.get(0).getToken();
		
		int k;
		if(bracketFlag){
			k = 2;
		}else{
			k = 1;
		}
		Vector<Token> rightSideOfLast = new Vector<Token>();
		for(; k < i; k++){
			Token currToken = input.get(k);
			if(getPriority(currentOp) >= getPriority(currToken.getToken())){
				System.out.println("token with higer priority!");
				if(returnVal instanceof Addition){
					Addition tmp = (Addition)returnVal;
					tmp.addAddend(parseTokens(rightSideOfLast));
					returnVal = tmp;
				}else if(returnVal instanceof Substraction){
					Substraction tmp = (Substraction)returnVal;
					tmp.setSubtrahend(parseTokens(rightSideOfLast));
					returnVal = tmp;
				}else{
					returnVal = parseTokens(rightSideOfLast);
				}
				
				currentOp = currToken.getToken();
				
				if(currentOp.equals("+")){
					System.out.println("addition");
					if(returnVal == null){
						Addition tmp = new Addition();
						tmp.addAddend(returnVal);
						returnVal = tmp;
						System.out.println("returnVal == null");
						System.out.println(returnVal);
					}else if(returnVal instanceof Addition){
						Addition tmp = (Addition)returnVal;
						tmp.addAddend(returnVal);
						returnVal = tmp;
					}
				}
				if(currentOp.equals("-")){
					Substraction tmp = new Substraction();
					tmp.setMinuend(returnVal);
					returnVal = tmp;
				}
				if(currentOp.equals("*")){

				}
				if(currentOp.equals("/")){

				}
				if(currentOp.equals("=")){

				}
				
				rightSideOfLast.clear();
			}else{
				rightSideOfLast.add(currToken);
			}
		}
		System.out.println("end of parsing!");
		return returnVal;*/
		
		String currentOp = "";
		int lastOperatorPos = 0;
		for(int k = 0; k <= 5; k++){//look only at operators at lowest priority, then ones higher priority
			Vector<Token> tokensToAdd = new Vector<Token>();
			System.out.println("searching for operators with priority: " + k);
			if(!currentOp.equals("")){
				break;
			}
			int bracketLevel = 0;
			int tmpBracketLevel = 0;
			int i = input.size() - 1;
			boolean bracketFlag = false;//TODO system struktur, ewentualnie bez tokenow
			if(input.get(0).getToken().equals("(") && input.get(i).getToken().equals(")")){
				bracketFlag = true;
				for(int j = 0; j < i; j++){
					Token currToken = input.get(j);
					if(currToken.getToken().equals("(")){//if thera are brackets surronding expression, ignore them
						tmpBracketLevel ++;
					}
					if(currToken.getToken().equals(")")){
						tmpBracketLevel --;
						if(tmpBracketLevel == 0 && j < i-1){
							bracketFlag = false;
							break;
						}
					}
				}
			}
			if(bracketFlag){
				i = input.size() - 2;
			}

			for(; i >= 0; i--){//TODO daje odwrotna kolejnosc niz wpisane(nie mozna )
				Token currToken = input.get(i);
				tokensToAdd.add(currToken);//tokens that are between last operator and next one(useful in addition/multiplications)
				if(currToken.getToken().equals(")")){
					bracketLevel ++;
				}
				if(currToken.getToken().equals("(")){
					bracketLevel --;
				}
				if(bracketLevel > 0){
					System.out.println("skipping token, searching for end of bracket");
					continue;
				}
				System.out.println("next token: " + currToken.getToken() + " type: " + currToken.getType());
				if(currToken.getType() == 1){
					if(getPriority(currToken.getToken()) == k){
						lastOperatorPos = i;
						if(currentOp.equals("")){//to jest, zeby nie ustawial nowego operatora
							currentOp = currToken.getToken();//TODO obsluga pierwiastkowania(a tym samym operacje z innym zapisem niz arg op arg)
						}

						if(currentOp.equals(currToken.getToken())){
							Vector<Token> firstPart = new Vector<Token>();
							Vector<Token> secondPart = new Vector<Token>();

							if(!bracketFlag){
								firstPart.addAll(input.subList(0, i));
								secondPart.addAll(input.subList(i + 1, input.size()));
							}else{
								firstPart.addAll(input.subList(1, i));
								secondPart.addAll(input.subList(i + 1, input.size() - 1));
							}
							if(returnVal == null){//parse diffrent operators
								System.out.println("first operator");
								if(currentOp.equals("\\")){
									Fraction returnValTmp = new Fraction();

									System.out.println("[\\]part 1");
									returnValTmp.setNumerator(parseTokens(firstPart));
									System.out.println("[\\]part 2");
									returnValTmp.setDenominator(parseTokens(secondPart));
									returnVal = returnValTmp;
								}
								if(currentOp.equals("+")){
									Addition returnValTmp = new Addition();

									System.out.println("[+]part 1");
									returnValTmp.addAddend(parseTokens(secondPart));
									//System.out.println("[+]part 2");
									//returnValTmp.addAddend(parseTokens(secondPart));
									returnVal = returnValTmp;
									System.out.println("end of part 2");
									tokensToAdd.clear();
								}
								if(currentOp.equals("-")){
									Substraction returnValTmp = new Substraction();

									System.out.println("[-]part 1");
									returnValTmp.setMinuend(parseTokens(firstPart));
									System.out.println("[-]part 2");
									returnValTmp.setSubtrahend(parseTokens(secondPart));
									returnVal = returnValTmp;
								}
								if(currentOp.equals("*")){
									Multiplication returnValTmp = new Multiplication();

									System.out.println("[*]part 1");
									returnValTmp.addFactor(parseTokens(secondPart));
									//System.out.println("[+]part 2");
									//returnValTmp.addAddend(parseTokens(secondPart));
									returnVal = returnValTmp;
									System.out.println("end of part 2");
									tokensToAdd.clear();
								}
								if(currentOp.equals("/")){
									Division returnValTmp = new Division();

									System.out.println("[/]part 1");
									returnValTmp.setDividend(parseTokens(firstPart));
									System.out.println("[/]part 2");
									returnValTmp.setDivisor(parseTokens(secondPart));
									returnVal = returnValTmp;
								}
								if(currentOp.equals("^")){
									Exponentiation returnValTmp = new Exponentiation();

									System.out.println("[^]part 1");
									returnValTmp.setBase(parseTokens(firstPart));
									System.out.println("[^]part 2");
									returnValTmp.setExponent(parseTokens(secondPart));
									returnVal = returnValTmp;
								}
								if(currentOp.equals("=")){
									Equation returnValTmp = new Equation();

									System.out.println("[=]part 1");
									returnValTmp.setSide1(parseTokens(firstPart));
									System.out.println("[=]part 2");
									returnValTmp.setSide2(parseTokens(secondPart));
									returnVal = returnValTmp;
								}
								}else{
									System.out.println("next operator");

									if(currentOp.equals("+") && returnVal instanceof Addition){
										Addition returnValConverted = (Addition) returnVal;

										//System.out.println("[+]part 1");
										//returnValConverted.addAddend(parseTokens(firstPart));
										Vector<Token> tokensInArg = new Vector<Token>();
										tokensInArg.addAll(tokensToAdd.subList(0, tokensToAdd.size() - 1));
										
										System.out.println("[+]part 2(adding only second part)");
										returnValConverted.addAddend(parseTokens(tokensInArg));
										tokensToAdd.clear();
									}
									if(currentOp.equals("*") && returnVal instanceof Multiplication){
										Multiplication returnValConverted = (Multiplication) returnVal;

										//System.out.println("[+]part 1");
										//returnValConverted.addAddend(parseTokens(firstPart));
										Vector<Token> tokensInArg = new Vector<Token>();
										tokensInArg.addAll(tokensToAdd.subList(0, tokensToAdd.size() - 1));
										
										System.out.println("[+]part 2(adding only second part)");
										returnValConverted.addFactor(parseTokens(tokensInArg));
										tokensToAdd.clear();
									}
									
									
							}
							
						}
					}
				}
				if(bracketFlag && i <= 1){
					break;
				}
			}
			
			Vector<Token> firstPart = new Vector<Token>();
			Vector<Token> secondPart = new Vector<Token>();

			if(!bracketFlag){
				firstPart.addAll(input.subList(0, lastOperatorPos));
				secondPart.addAll(input.subList(lastOperatorPos + 1, input.size()));
			}else if(lastOperatorPos >= 1){
				firstPart.addAll(input.subList(1, lastOperatorPos));
				secondPart.addAll(input.subList(lastOperatorPos + 1, input.size() - 1));
			}
			
			System.out.println("Adding last part");
			if(currentOp.equals("+") && returnVal instanceof Addition){
				Addition returnValConverted = (Addition) returnVal;
				
				System.out.println("[+]part 1(adding only first part)");
				returnValConverted.addAddend(parseTokens(firstPart));
				tokensToAdd.clear();
			}
			if(currentOp.equals("*") && returnVal instanceof Multiplication){
				Multiplication returnValConverted = (Multiplication) returnVal;

				System.out.println("[*]part 1(adding only first part)");
				returnValConverted.addFactor(parseTokens(firstPart));
				tokensToAdd.clear();
			}
			if(returnVal != null){
				break;
			}
		}
		return returnVal;
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
