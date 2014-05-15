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
package com.krzygorz.calculator.tree;

import java.util.Vector;

import com.krzygorz.calculator.misc.SettingsManager;
import com.krzygorz.calculator.parser.MathParser;

public class Multiplication implements ExpressionPart{
	
	private Vector<ExpressionPart> factors; //czynniki
	
	public Multiplication() {
		factors = new Vector<ExpressionPart>();
	}
	
	public Multiplication(Vector<ExpressionPart> addends){
		this.factors = addends;
	}
	
	public Multiplication(ExpressionPart... factors){
		this.factors = new Vector<ExpressionPart>();
		for(ExpressionPart i : factors){
			this.factors.add(i);
		}
	}

	/*@Override
	public int getType() {
		return 0;
	}*/
	private ExpressionPart multiplyTwoArgs(ExpressionPart factor1, ExpressionPart factor2){
		if(factor1 == null){
			return factor2;
		}
		if(factor2 == null){
			return factor1;
		}
		boolean isChanged = false;
		if(factor1.canBeSimplified()){
			factor1 = factor1.simplyfy();
			isChanged = true;
		}
		if(factor2.canBeSimplified()){
			factor2 = factor2.simplyfy();
			isChanged = true;
		}
		if(factor1 instanceof Number && factor2 instanceof Number){//TODO nie dawac wynikow 1*cos
			Number addend1Converted = (Number)factor1;
			Number addend2Converted = (Number)factor2;

			return new Number(addend1Converted.getValue() * addend2Converted.getValue());
		}
		
		if(factor1 instanceof Multiplication){
			Multiplication factor1Converted = (Multiplication)factor1;
			factor1Converted.addFactor(factor2);
			return factor1Converted.simplyfy();
		}
		if(factor2 instanceof Multiplication){
			Multiplication factor2Converted = (Multiplication)factor2;
			factor2Converted.addFactor(factor1);
			return factor2Converted.simplyfy();
		}
		
		if(factor1 instanceof Addition){
			Addition factor1Converted = (Addition)factor1;
			Addition ret = new Addition();
			for(ExpressionPart i : factor1Converted.getAddends()){
				ret.addAddend(new Multiplication(i, factor2));
			}
			return ret;
		}
		if(factor2 instanceof Addition){
			Addition factor2Converted = (Addition)factor2;
			Addition ret = new Addition();
			for(ExpressionPart i : factor2Converted.getAddends()){
				ret.addAddend(new Multiplication(i, factor1));
			}
			return ret;
		}
		if(factor1 instanceof Substraction){
			Substraction factor1Converted = (Substraction)factor1;
			return new Substraction(new Multiplication(factor1Converted.getMinuend(), factor2), new Multiplication(factor1Converted.getSubtrahend(), factor2));
		}
		if(factor2 instanceof Substraction){
			Substraction factor2Converted = (Substraction)factor2;
			return new Substraction(new Multiplication(factor2Converted.getMinuend(), factor1), new Multiplication(factor2Converted.getSubtrahend(), factor1));
		}
		
		if(SettingsManager.getSetting("simplyfyToFraction").equals("1")){
			if(factor1 instanceof Number && factor2 instanceof Division){//TODO jak sie da, to zrobic, zeby kolejnosc argumentow nie miala znaczenia
				Number factor1Converted = (Number)factor1;
				Division factor2Converted = (Division)factor2;

				ExpressionPart gcd = new GreatestCommonDivisor(factor1Converted, factor2Converted.getDivisor()).simplyfy();
				return new Division(new Multiplication(new Division(factor1Converted, gcd), factor2Converted.getDividend()), new Division(factor2Converted.getDivisor(), gcd)).simplyfy();
			}
			if(factor1 instanceof Division && factor2 instanceof Number){
				return new Multiplication(factor2, factor1).simplyfy();
			}
			if(factor1 instanceof Division && factor2 instanceof Division){
				Division factor1Converted = (Division)factor1;
				Division factor2Converted = (Division)factor2;
				ExpressionPart gcd1 = new GreatestCommonDivisor(factor1Converted.getDividend(), factor2Converted.getDivisor()).simplyfy();
				ExpressionPart gcd2 = new GreatestCommonDivisor(factor2Converted.getDividend(), factor1Converted.getDivisor()).simplyfy();
				return new Division(new Multiplication(new Division(factor1Converted.getDividend(), gcd1), new Division(factor2Converted.getDividend(), gcd2)), new Multiplication(new Division(factor1Converted.getDivisor(), gcd2),new Division(factor2Converted.getDivisor(), gcd1))).simplyfy();
			}
		}
		
		if(factor1.matches(factor2)){
			return new Exponentiation(factor1, new Number(2));
		}
		if(factor1 instanceof Exponentiation && ((Exponentiation)factor1).getBase().matches(factor2)){
			return new Exponentiation(factor1, new Addition(((Exponentiation)factor1).getBase(), new Number(1)).simplyfy());
		}
		
		if(isChanged){
			return new Multiplication(factor1, factor2);
		}
		return null;
	}
	
	public void addFactor(ExpressionPart factor){
		//if(factor != null){
		factors.add(factor);
		//}
	}
	

	@Override
	public boolean canBeSimplified() {
		if(this.simplyfy().matches(this)){
			return false;
		}
		return true;
	}

	@Override
	public ExpressionPart simplyfy(){
		boolean isLast = true;
		for(ExpressionPart arg : factors){
			if(arg.canBeSimplified()){
				isLast = false;
				break;
			}
		}
		
		if(isLast){
			if(factors != null){
				if(factors.size() > 1){
					Vector<ExpressionPart> toMultiply = factors;
					Vector<ExpressionPart> result = new Vector<ExpressionPart>();
					while(toMultiply.size() != 0){
						ExpressionPart tmp = null;
						Vector<ExpressionPart> toMultiplyNext = new Vector<ExpressionPart>();
						for(ExpressionPart i : toMultiply){
							ExpressionPart tmp1 = multiplyTwoArgs(tmp, i);
							if(tmp1 != null){
								tmp = tmp1;
							}else{
								toMultiplyNext.add(i);
							}
						}
						toMultiply = toMultiplyNext;
						result.add(tmp);
					}
					if(result.size() == 1){
						return result.get(0);
					}else{
						return new Multiplication(result);
					}
				}else if(factors.size() == 1){
					return factors.get(0);
				}
			}
		}else{
			Multiplication retValue = new Multiplication();
			for(ExpressionPart tmp : factors){
				if(tmp.canBeSimplified()){
					//System.out.println("next NaN arg: " + tmp);
					tmp = tmp.simplyfy();
				}
				retValue.addFactor(tmp);
			}
			return retValue;
		}
		return null;
	}
	
	@Override
	public String toString(){
		String returnVal = "";
		if(factors.size() == 2){
			if(factors.get(0) instanceof Number && factors.get(1) instanceof Variable){
				return factors.get(0).toString().concat(factors.get(1).toString());
			}
			if(factors.get(1) instanceof Number && factors.get(0) instanceof Variable){
				return factors.get(1).toString().concat(factors.get(0).toString());
			}
		}
		for(ExpressionPart arg : factors){
			returnVal = returnVal.concat("(");
			returnVal = returnVal.concat(arg.toString());
			returnVal = returnVal.concat(")");
			returnVal = returnVal.concat("*");
		}
		
		return returnVal.substring(0, returnVal.length() - 1);
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Multiplication){
			Vector<ExpressionPart> left = new Vector<ExpressionPart>(factors);
			Multiplication argConverted = (Multiplication)arg;
			for(ExpressionPart i : argConverted.factors){
				boolean hasMatchingArg = false;
				for(ExpressionPart k : left){
					if(i.matches(k)){
						left.remove(k);
						hasMatchingArg = true;
						break;
					}
				}
				if(!hasMatchingArg){
					return false;
				}
			}
			return true;
		}
		return false;
	}

}
