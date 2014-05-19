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

public class Number implements ExpressionPart{
	private double value;

	public Number(double value){
		this.value = value;
	}
	
	public Number(){
		this.value = 0;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public double getValue(){
		return value;
	}
	@Override
	public String toString(){
		String retValue = "";
		if(this.value < 0){
			retValue = retValue.concat("(");
		}
		if((value % 1) == 0){
			retValue = retValue.concat(String.valueOf((int)value));
		}else{
			retValue = retValue.concat(String.valueOf(value));
		}
		
		if(this.value < 0){
			retValue = retValue.concat(")");
		}
		return retValue;
	}
	
	/*@Override
	public int getType() {
		return 2;
	}*/

	/*@Override
	public boolean canBeSimplified() {
		return false;
	}

	@Override
	public ExpressionPart simplyfy() {
		return this;
	}*/

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Number && (((Number)arg).getValue() == this.getValue())){
			return true;
		}
		return false;
	}

}