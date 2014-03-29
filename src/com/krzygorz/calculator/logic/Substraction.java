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

public class Substraction implements ExpressionPart{
	
	ExpressionPart minuend;//odjemnik
	ExpressionPart subtrahend;//odjemna
	
	public Substraction() {}
	
	public Substraction(ExpressionPart minuend, ExpressionPart substrahend){
		this.minuend = minuend;
		this.subtrahend = substrahend;
	}

	/*@Override
	public int getType() {
		return 0;
	}*/

	@Override
	public ExpressionPart simplyfy() {
		ExpressionPart tmpMinuend = minuend;
		ExpressionPart tmpSubtrahend = subtrahend;
		
		if(tmpMinuend.canBeSimplified()){
			tmpMinuend = tmpMinuend.simplyfy();
		}
		if(tmpSubtrahend.canBeSimplified()){
			tmpSubtrahend = tmpSubtrahend.simplyfy();
		}
		
		if(tmpMinuend instanceof Number && tmpSubtrahend instanceof Number){
			Number minuendConverted = (Number)tmpMinuend;
			Number subtrahendConverted = (Number)tmpSubtrahend;
			
			return new Number(minuendConverted.getValue() - subtrahendConverted.getValue());
		}
		if((tmpMinuend instanceof Fraction) && (tmpSubtrahend instanceof Number)){
			Fraction minuendConverted = new Fraction(((Fraction)tmpMinuend).getNumerator(), ((Fraction)tmpMinuend).getDenominator());
			Number subtrahendConverted = new Number(((Number)tmpSubtrahend).getValue());
			
			minuendConverted.setNumerator(new Substraction(minuendConverted.getNumerator(), new Multiplication(subtrahendConverted, minuendConverted.getDenominator())));
			return minuendConverted.simplyfy();
		}
		if((tmpMinuend instanceof Number) && (tmpSubtrahend instanceof Fraction)){
			Number minuendConverted = new Number(((Number)tmpMinuend).getValue());//TODO konstruktory kopiujace
			Fraction subtrahendConverted = new Fraction(((Fraction)tmpSubtrahend).getNumerator(), ((Fraction)tmpSubtrahend).getDenominator());
			
			subtrahendConverted.setNumerator(new Substraction(new Multiplication(minuendConverted, subtrahendConverted.getDenominator()), subtrahendConverted.getDenominator()));
			return subtrahendConverted.simplyfy();
		}
		if(minuend instanceof Fraction && subtrahend instanceof Fraction){
			Fraction minuendConverted = new Fraction(((Fraction)tmpMinuend).getNumerator(), ((Fraction)tmpMinuend).getDenominator());
			Fraction subtrahendConverted = new Fraction(((Fraction)tmpSubtrahend).getNumerator(), ((Fraction)tmpSubtrahend).getDenominator());
			ExpressionPart lcp = new LeastCommonMultiple(minuendConverted.getDenominator(), subtrahendConverted.getDenominator());
			
			minuendConverted.setNumerator(new Multiplication(new Division(lcp, minuendConverted.getDenominator()), minuendConverted.getNumerator()));
			minuendConverted.setDenominator(new Multiplication(new Division(lcp, minuendConverted.getDenominator()), minuendConverted.getDenominator()));
			subtrahendConverted.setNumerator(new Multiplication(new Division(lcp, subtrahendConverted.getDenominator()), subtrahendConverted.getNumerator()));
			subtrahendConverted.setDenominator(new Multiplication(new Division(lcp, subtrahendConverted.getDenominator()), subtrahendConverted.getDenominator()));
			
			return new Fraction(new Substraction(minuendConverted.getNumerator(), subtrahendConverted.getNumerator()), minuendConverted.getDenominator()).simplyfy();
		}
		return new Substraction(tmpMinuend, tmpSubtrahend);
		
	}

	@Override
	public boolean canBeSimplified() {
		return true;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy(){
		boolean isLast = true;
		ExpressionPart tmpMinuend = minuend;
		ExpressionPart tmpSubtrahend = subtrahend;
		
		if(minuend.canBeSimplified()){
			tmpMinuend = this.minuend.nextStepToSimplyfy();//TODO przeniesc to na koniec metody we wszystkich klasach
			isLast = false;
		}
		if(subtrahend.canBeSimplified()){
			isLast = false;
			tmpSubtrahend = this.subtrahend.nextStepToSimplyfy();
		}
		
		if(isLast){
			if((minuend instanceof Number) && (subtrahend instanceof Number)){
				Number minuendConverted = new Number(((Number)tmpMinuend).getValue());
				Number subtrahendConverted = new Number(((Number)tmpSubtrahend).getValue());
				
				return new Number(minuendConverted.getValue() - subtrahendConverted.getValue());
			}
			if((minuend instanceof Fraction) && (subtrahend instanceof Number)){
				Fraction minuendConverted = new Fraction(((Fraction)minuend).getNumerator(), ((Fraction)minuend).getDenominator());
				Number subtrahendConverted = new Number(((Number)subtrahend).getValue());
				
				minuendConverted.setNumerator(new Substraction(minuendConverted.getNumerator(), new Multiplication(subtrahendConverted, minuendConverted.getDenominator())));
				return minuendConverted;
			}
			if((minuend instanceof Number) && (subtrahend instanceof Fraction)){
				Number minuendConverted = new Number(((Number)minuend).getValue());
				Fraction subtrahendConverted = new Fraction(((Fraction)subtrahend).getNumerator(), ((Fraction)subtrahend).getDenominator());
				
				subtrahendConverted.setNumerator(new Substraction(new Multiplication(minuendConverted, subtrahendConverted.getDenominator()), subtrahendConverted.getNumerator()));
				return subtrahendConverted;
			}
			if(minuend instanceof Fraction && subtrahend instanceof Fraction){
				Fraction minuendConverted = new Fraction(((Fraction)tmpMinuend).getNumerator(), ((Fraction)tmpMinuend).getDenominator());
				Fraction subtrahendConverted = new Fraction(((Fraction)tmpSubtrahend).getNumerator(), ((Fraction)tmpSubtrahend).getDenominator());
				//if(minuendConverted.getDenominator().equals(subtrahendConverted.getDenominator())//TODO sprawdzac, czy ulamki maja taki sam mianownik
				ExpressionPart lcp = new LeastCommonMultiple(minuendConverted.getDenominator(), subtrahendConverted.getDenominator());
				
				minuendConverted.setNumerator(new Multiplication(new Division(lcp, minuendConverted.getDenominator()), minuendConverted.getNumerator()));
				minuendConverted.setDenominator(new Multiplication(new Division(lcp, minuendConverted.getDenominator()), minuendConverted.getDenominator()));
				subtrahendConverted.setNumerator(new Multiplication(new Division(lcp, subtrahendConverted.getDenominator()), subtrahendConverted.getNumerator()));
				subtrahendConverted.setDenominator(new Multiplication(new Division(lcp, subtrahendConverted.getDenominator()), subtrahendConverted.getDenominator()));
				
				return new Fraction(new Substraction(minuendConverted.getNumerator(), subtrahendConverted.getNumerator()), minuendConverted.getDenominator());
			}
		}
		
		return new Substraction(tmpMinuend, tmpSubtrahend);
	}
	
	@Override
	public String toString(){
		//String convertedNumerator = numerator.toString();
		//String convertedDenominator = denominator.toString();
		return this.minuend.toString().concat("-").concat(this.subtrahend.toString());//TODO nawiasy
	}

	
	public ExpressionPart getMinuend() {
		return minuend;
	}

	public void setMinuend(ExpressionPart minuend) {
		this.minuend = minuend;
	}

	public ExpressionPart getSubtrahend() {
		return subtrahend;
	}

	public void setSubtrahend(ExpressionPart subtrahend) {
		this.subtrahend = subtrahend;
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Substraction && (((Substraction)arg).getMinuend() == this.getMinuend()) && (((Substraction)arg).getSubtrahend() == this.getSubtrahend())){
			return true;
		}
		return false;
	}

}
