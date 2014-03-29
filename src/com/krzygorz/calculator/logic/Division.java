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

import com.krzygorz.calculator.parser.MathParser;

public class Division implements ExpressionPart{
	
	ExpressionPart dividend;//dzielnik
	ExpressionPart divisor;//dzielna
	
	public Division() {}
	
	public Division(ExpressionPart minuend, ExpressionPart substrahend){
		this.dividend = minuend;
		this.divisor = substrahend;
	}

	/*@Override
	public int getType() {
		return 0;
	}*/

	@Override
	public ExpressionPart simplyfy() {
		ExpressionPart tmpDividend = dividend;
		ExpressionPart tmpDivisor = divisor;
		
		if(dividend.canBeSimplified()){
			tmpDividend = this.dividend.simplyfy();
		}
		if(divisor.canBeSimplified()){
			tmpDivisor = this.divisor.simplyfy();
		}
		
		if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
			Number dividendConverted = (Number)tmpDividend;
			Number divisorConverted = (Number)tmpDivisor;
			
			return new Number(dividendConverted.getValue() / divisorConverted.getValue());
		}
		if(tmpDividend instanceof Fraction && tmpDivisor instanceof Number){
			Fraction dividendConverted = (Fraction)tmpDividend;
			Number divisorConverted = (Number)tmpDivisor;
			
			return new Fraction(dividendConverted.getNumerator(), new Multiplication(dividendConverted.getDenominator(), divisorConverted)).simplyfy();
		}
		if(tmpDividend instanceof Number && tmpDivisor instanceof Fraction){
			Number dividendConverted = (Number)tmpDividend;
			Fraction divisorConverted = (Fraction)tmpDivisor;
			
			return new Multiplication(dividendConverted, new Fraction(divisorConverted.getDenominator(), divisorConverted.getNumerator())).simplyfy();
		}
		if(tmpDividend instanceof Fraction && tmpDivisor instanceof Fraction){
			Fraction dividendConverted = (Fraction)tmpDividend;
			Fraction divisorConverted = (Fraction)tmpDivisor;
			
			return new Multiplication(dividendConverted, new Fraction(divisorConverted.getDenominator(), divisorConverted.getNumerator())).simplyfy();
		}
		return new Division(tmpDividend, tmpDivisor);
	}

	@Override
	public boolean canBeSimplified() {
		return true;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy(){
		ExpressionPart tmpDividend = dividend;
		ExpressionPart tmpDivisor = divisor;
		
		if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
			Number dividendConverted = (Number)tmpDividend;
			Number divisorConverted = (Number)tmpDivisor;
			
			return new Number(dividendConverted.getValue() / divisorConverted.getValue());
		}
		if(tmpDividend instanceof Fraction && tmpDivisor instanceof Number){
			Fraction dividendConverted = (Fraction)tmpDividend;
			Number divisorConverted = (Number)tmpDivisor;
			
			return new Fraction(dividendConverted.getNumerator(), new Multiplication(dividendConverted.getDenominator(), divisorConverted));
		}
		if(tmpDividend instanceof Number && tmpDivisor instanceof Fraction){
			Number dividendConverted = (Number)tmpDividend;
			Fraction divisorConverted = (Fraction)tmpDivisor;
			
			return new Multiplication(dividendConverted, new Fraction(divisorConverted.getDenominator(), divisorConverted.getNumerator()));
		}
		if(tmpDividend instanceof Fraction && tmpDivisor instanceof Fraction){
			Fraction dividendConverted = (Fraction)tmpDividend;
			Fraction divisorConverted = (Fraction)tmpDivisor;
			
			return new Multiplication(dividendConverted, new Fraction(divisorConverted.getDenominator(), divisorConverted.getNumerator()));
		}
		
		if(dividend.canBeSimplified()){
			tmpDividend = this.dividend.nextStepToSimplyfy();
		}
		if(divisor.canBeSimplified()){
			tmpDivisor = this.divisor.nextStepToSimplyfy();
		}
		
		return new Division(tmpDividend, tmpDivisor);
	}
	
	@Override
	public String toString(){
		//String convertedNumerator = numerator.toString();
		//String convertedDenominator = denominator.toString();
		return this.dividend.toString().concat("/").concat(this.divisor.toString());
	}

	
	public ExpressionPart getDividend() {
		return dividend;
	}

	public void setDividend(ExpressionPart dividend) {
		this.dividend = dividend;
	}

	public ExpressionPart getDivisor() {
		return divisor;
	}

	public void setDivisor(ExpressionPart divisor) {
		this.divisor = divisor;
	}
	
	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Division && (((Division)arg).getDividend() == this.getDividend()) && (((Division)arg).getDivisor() == this.getDivisor())){
			return true;
		}
		return false;
	}
}
