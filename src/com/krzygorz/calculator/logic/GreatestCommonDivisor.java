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

import com.krzygorz.calculator.parser.MathParser;

public class GreatestCommonDivisor implements ExpressionPart {
	Vector<ExpressionPart> args;
	
	
	public GreatestCommonDivisor() {
		this.args = new Vector<ExpressionPart>();
	}
	
	public GreatestCommonDivisor(ExpressionPart... args){
		this.args = new Vector<ExpressionPart>();
		for(ExpressionPart i : args){
			this.args.add(i);
		}
	}
	
	public long euclidsAlg(long l, long m) { //TODO obliczenia w GUI
		l = Math.abs(l);
		m = Math.abs(m);
		
	    if (l == 0){
	        return m;
	    }
	    
	    while (m != 0) {
	        if (l > m)
	            l = l - m;
	        else
	            m = m - l;
	    }
	    return l;
	}
	
	public GreatestCommonDivisor(Vector<ExpressionPart> args) {
		this.args = args;
	}
	
	public void addArg(ExpressionPart arg){
		this.args.add(arg);
	}

	@Override
	public ExpressionPart simplyfy() {
		if(args.size() > 1){
			if(args.size() > 2){//gcd(arg1, arg2, arg3) = gcd(arg1, gcd(arg2, arg3))
				return new GreatestCommonDivisor(new ExpressionPart[]{args.get(0), new GreatestCommonDivisor((ExpressionPart[])(args.subList(1, args.size() - 1).toArray()))});
			}else{
				ExpressionPart arg1 = args.get(0);
				ExpressionPart arg2 = args.get(1);
				if(arg1 instanceof Number && arg2 instanceof Number){
					Number arg1Converted = (Number)arg1;
					Number arg2Converted = (Number)arg2;
					if(Operation.isInteger(arg1Converted.getValue()) && Operation.isInteger(arg2Converted.getValue())){
						return new Number(euclidsAlg(Math.round(arg1Converted.getValue()), Math.round(arg2Converted.getValue())));
					}
				}
			}
		}
				
		return null;
	}

	@Override
	public boolean canBeSimplified() {
		
		return true;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy() {
		
		if(args.size() > 1){
			if(args.size() > 2){
				return new GreatestCommonDivisor(new ExpressionPart[]{args.get(0), new GreatestCommonDivisor((ExpressionPart[])(args.subList(1, args.size() - 1).toArray()))});
			}else{
				ExpressionPart arg1 = args.get(0);
				ExpressionPart arg2 = args.get(1);
				if(arg1 instanceof Number && arg2 instanceof Number){
					Number arg1Converted = (Number)arg1;
					Number arg2Converted = (Number)arg2;
					if(Operation.isInteger(arg1Converted.getValue()) && Operation.isInteger(arg2Converted.getValue())){
						return new Number(euclidsAlg(Math.round(arg1Converted.getValue()), Math.round(arg2Converted.getValue())));
					}
				}
			}
		}
				
		return null;
	}
	
	@Override public String toString(){
		String returnVal = "gcd(";
		for(ExpressionPart arg : args){
			returnVal = returnVal.concat(arg.toString());
			returnVal = returnVal.concat(", ");
		}
		
		return returnVal.substring(0, returnVal.length() - 2).concat(")");
		
	}
	
	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof GreatestCommonDivisor){
			Vector<ExpressionPart> left = args;
			GreatestCommonDivisor argConverted = (GreatestCommonDivisor)arg;
			for(ExpressionPart i : argConverted.args){
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
