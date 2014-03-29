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

//TODO gdy nie wiadomo jak cos zrobic, zajzec do zestawu rownan, i sprobowac je przeksztalcic
public class Equation implements ExpressionPart{//my current objective
	private ExpressionPart side1;
	private ExpressionPart side2;
	
	public Equation() {
		
	}
	
	public Equation(ExpressionPart side1, ExpressionPart side2) {
		this.side1 = side1;
		this.side1 = side2;
	}
	
	@Override
	public ExpressionPart simplyfy() {
		return this;
	}

	@Override
	public boolean canBeSimplified() {
		return false;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy() {
		return this;
	}
	
	public ExpressionPart getSide1() {
		return side1;
	}
	public void setSide1(ExpressionPart side1) {
		this.side1 = side1;
	}
	public ExpressionPart getSide2() {
		return side2;
	}
	public void setSide2(ExpressionPart side2) {
		this.side2 = side2;
	}
	
	@Override
	public String toString(){
		if(side1 != null && side2 != null){
			return side1.toString().concat("=").concat(side2.toString());
		}
		return "null !";
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Equation && (((Equation)arg).getSide1() == this.getSide1()) && (((Equation)arg).getSide2() == this.getSide2())){
			return true;
		}
		return false;
	}
}
