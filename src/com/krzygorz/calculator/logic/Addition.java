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
package com.krzygorz.calculator.logic;

import java.util.Vector;

import com.krzygorz.calculator.misc.SettingsManager;
import com.krzygorz.calculator.parser.MathParser;

public class Addition implements ExpressionPart{
	
	Vector<ExpressionPart> addends; //skladniki
	
	public Addition() {
		addends = new Vector<ExpressionPart>();
	}
	
	public Addition(Vector<ExpressionPart> addends){
		this.addends = addends;
	}
	
	public Addition(ExpressionPart... addends){
		this.addends = new Vector<ExpressionPart>();
		for(ExpressionPart i : addends){
			this.addends.add(i);
		}
	}

	/*@Override
	public int getType() {
		return 0;
	}*/

	@Override
	public ExpressionPart simplyfy() {
		if(addends != null){
			if(addends.size() > 1){
				Vector<ExpressionPart> toAdd = new Vector<ExpressionPart>(addends);
				Vector<ExpressionPart> result = new Vector<ExpressionPart>();
				while(toAdd.size() != 0){
					ExpressionPart tmp = null;
					Vector<ExpressionPart> toAddNext = new Vector<ExpressionPart>();
					for(ExpressionPart i : toAdd){
						ExpressionPart tmp1 = addTwoArgs(tmp, i);
						if(tmp1 != null){
							tmp = tmp1;
						}else{
							toAddNext.add(i);
						}
					}
					toAdd = toAddNext;
					result.add(tmp);
				}
				if(result.size() == 1){
					return result.get(0);
				}else{
					return new Addition(result);
				}
			}else if(addends.size() == 1){
				return addends.get(0);
			}
		}
		return null;
	}

	private ExpressionPart addTwoArgs(ExpressionPart addend1, ExpressionPart addend2){
		if(addend1 == null){
			return addend2;
		}
		if(addend2 == null){
			return addend1;
		}
		if(addend1.canBeSimplified()){
			addend1 = addend1.simplyfy();
		}
		if(addend2.canBeSimplified()){
			addend2 = addend2.simplyfy();
		}
		if(addend1 instanceof Number && addend2 instanceof Number){
			Number addend1Converted = (Number)addend1;
			Number addend2Converted = (Number)addend2;
			
			return new Number(addend1Converted.getValue() + addend2Converted.getValue());
		}
		if(SettingsManager.getSetting("simplyfyToFraction").equals("1")){
			if(addend1 instanceof Division && addend2 instanceof Number){
				Division addend1Converted = new Division(((Division)addend1).getDividend(), ((Division)addend1).getDivisor());
				Number addend2Converted = new Number(((Number)addend2).getValue());

				addend1Converted.setDividend(new Addition(addend1Converted.getDividend(), new Multiplication(addend2Converted, addend1Converted.getDivisor())));

				return addend1Converted.simplyfy();
			}
			if(addend1 instanceof Number && addend2 instanceof Division){
				return new Addition(addend2, addend1).simplyfy();
			}
			if(addend1 instanceof Division && addend2 instanceof Division){
				Division addend1Converted = new Division(((Division)addend1).getDividend(), ((Division)addend1).getDivisor());
				Division addend2Converted = new Division(((Division)addend2).getDividend(), ((Division)addend2).getDivisor());
				ExpressionPart lcp = new LeastCommonMultiple(addend1Converted.getDivisor(), addend2Converted.getDivisor());

				addend1Converted.setDividend(new Multiplication(new Division(lcp, addend1Converted.getDivisor()), addend1Converted.getDividend()));
				addend1Converted.setDivisor(new Multiplication(new Division(lcp, addend1Converted.getDivisor()), addend1Converted.getDivisor()));
				addend2Converted.setDividend(new Multiplication(new Division(lcp, addend2Converted.getDivisor()), addend2Converted.getDividend()));
				//addend2Converted.setDivisor(new Multiplication(new Division(lcp, addend2Converted.getDivisor()), addend2Converted.getDivisor()));
				return new Division(new Addition(addend1Converted.getDividend(), addend2Converted.getDividend()), addend1Converted.getDivisor()).simplyfy();
			}
			if(addend1 instanceof Variable && addend2 instanceof Variable && addend1.matches(addend2)){
				return new Multiplication(new Number(2), addend1);
			}
		}
		return null;
	}
	
	public void addAddend(ExpressionPart addend){
		addends.add(addend);
	}
	

	@Override
	public boolean canBeSimplified() {
		if(this.simplyfy().matches(this)){
			return false;
		}
		return true;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy(){
		boolean isLast = true;
		for(ExpressionPart arg : addends){
			//System.out.println("current arg: " + arg.toString());
			if(arg.canBeSimplified()){
				isLast = false;
				break;
			}
		}

		if(isLast){
			if(addends != null){
				if(addends.size() > 1){
					Vector<ExpressionPart> toAdd = new Vector<ExpressionPart>(addends);
					Vector<ExpressionPart> result = new Vector<ExpressionPart>();
					while(toAdd.size() != 0){
						ExpressionPart tmp = null;
						Vector<ExpressionPart> toAddNext = new Vector<ExpressionPart>();
						for(ExpressionPart i : toAdd){
							ExpressionPart tmp1 = addTwoArgs(tmp, i);
							if(tmp1 != null){
								tmp = tmp1;
							}else{
								toAddNext.add(i);
							}
						}
						toAdd = toAddNext;
						result.add(tmp);
					}
					if(result.size() == 1){
						return result.get(0);
					}else{
						return new Addition(result);
					}
				}else if(addends.size() == 1){
					return addends.get(0);
				}
			}
		}else{
			Addition retValue = new Addition();
			for(ExpressionPart tmp : addends){
				if(!(tmp instanceof Number) && tmp.canBeSimplified()){
					//System.out.println("next NaN arg: " + tmp);
					tmp = tmp.nextStepToSimplyfy();
				}
				retValue.addAddend(tmp);
			}
			return retValue;
		}
		return null;
	}

	@Override
	public String toString(){
		String returnVal = "";
		for(ExpressionPart arg : addends){
			//returnVal = returnVal.concat("(");
			returnVal = returnVal.concat(arg.toString());
			//returnVal = returnVal.concat(")");
			returnVal = returnVal.concat("+");
		}
		
		return returnVal.substring(0, returnVal.length() - 1);
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Addition){
			Vector<ExpressionPart> left = new Vector<ExpressionPart>(addends);
			Addition argConverted = (Addition)arg;
			for(ExpressionPart i : argConverted.addends){
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
		}
		return true;
	}

}
