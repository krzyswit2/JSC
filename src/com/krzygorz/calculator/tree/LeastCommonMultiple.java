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

public class LeastCommonMultiple implements ExpressionPart {
	private Vector<ExpressionPart> args;
	
	
	public LeastCommonMultiple() {
		this.args = new Vector<ExpressionPart>();
	}
	
	public LeastCommonMultiple(ExpressionPart... args){
		this.args = new Vector<ExpressionPart>();
		for(ExpressionPart i : args){
			this.args.add(i);
		}
	}
	
	public LeastCommonMultiple(Vector<ExpressionPart> args) {
		this.args = args;
	}
	
	public void addArg(ExpressionPart arg){
		this.args.add(arg);
	}

	/*@Override
	public boolean canBeSimplified() {
		
		return true;
	}

	@Override
	public ExpressionPart simplyfy() {
		return new Division(new Multiplication(args), new GreatestCommonDivisor(args)).simplyfy();
	}*/
	
	@Override public String toString(){
		String returnVal = "lcm(";
		for(ExpressionPart arg : args){
			returnVal = returnVal.concat(arg.toString());
			returnVal = returnVal.concat(", ");
		}
		
		return returnVal.substring(0, returnVal.length() - 2).concat(")");
		
	}
	
	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof LeastCommonMultiple){
			Vector<ExpressionPart> left = args;
			LeastCommonMultiple argConverted = (LeastCommonMultiple)arg;
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
