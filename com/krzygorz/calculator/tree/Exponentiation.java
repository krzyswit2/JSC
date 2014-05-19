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

public class Exponentiation implements ExpressionPart {
	private ExpressionPart base;
	private ExpressionPart exponent;
	
	public Exponentiation(){
		
	}
	
	public Exponentiation(ExpressionPart base, ExpressionPart exponent) {
		this.base = base;
		this.exponent = exponent;
	}

	@Override
	public boolean canBeSimplified() {
		if(this.matches(this.simplyfy())){
			return false;
		}
		return true;
	}

	@Override
	public ExpressionPart simplyfy() {
		if(base instanceof Number && exponent instanceof Number){
			return new Number(Math.pow(((Number)base).getValue(), ((Number)exponent).getValue()));
		}
		ExpressionPart tmpbase = base;
		ExpressionPart tmpexponent = exponent;
		if(base.canBeSimplified()){
			tmpbase = base.simplyfy();
		}
		if(exponent.canBeSimplified()){
			tmpexponent = exponent.simplyfy();
		}
		return this;
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Exponentiation){
			if(((Exponentiation)arg).getBase().matches(getBase()) && ((Exponentiation)arg).getExponent().matches(getExponent())){
				return true;
			}
		}
		return false;
	}

	public ExpressionPart getExponent() {
		return exponent;
	}

	public void setExponent(ExpressionPart exponent) {
		this.exponent = exponent;
	}

	public ExpressionPart getBase() {
		return base;
	}

	public void setBase(ExpressionPart base) {
		this.base = base;
	}
	
	@Override
	public String toString(){
		return base.toString().concat("^").concat(exponent.toString());
	}

}
