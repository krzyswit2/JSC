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

import com.krzygorz.calculator.misc.SettingsManager;
import com.krzygorz.calculator.parser.MathParser;

public class Substraction implements ExpressionPart{
	
	private ExpressionPart minuend;//odjemnik
	private ExpressionPart subtrahend;//odjemna
	
	public Substraction() {}
	
	public Substraction(ExpressionPart minuend, ExpressionPart substrahend){
		this.minuend = minuend;
		this.subtrahend = substrahend;
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
		ExpressionPart tmpMinuend = minuend;
		ExpressionPart tmpSubtrahend = subtrahend;
		
		if(minuend.canBeSimplified()){
			tmpMinuend = this.minuend.simplyfy();//TODO przeniesc to na koniec metody we wszystkich klasach
			isLast = false;
		}
		if(subtrahend.canBeSimplified()){
			isLast = false;
			tmpSubtrahend = this.subtrahend.simplyfy();
		}
		
		if(isLast){
			if((minuend instanceof Number) && (subtrahend instanceof Number)){
				Number minuendConverted = new Number(((Number)tmpMinuend).getValue());
				Number subtrahendConverted = new Number(((Number)tmpSubtrahend).getValue());
				
				return new Number(minuendConverted.getValue() - subtrahendConverted.getValue());
			}
			if(SettingsManager.getSetting("simplyfyToFraction").equals("1")){
				if((tmpMinuend instanceof Division) && (tmpSubtrahend instanceof Number)){
					Division minuendConverted = new Division(((Division)tmpMinuend).getDividend(), ((Division)tmpMinuend).getDivisor());
					Number subtrahendConverted = new Number(((Number)tmpSubtrahend).getValue());

					minuendConverted.setDividend(new Substraction(minuendConverted.getDividend(), new Multiplication(subtrahendConverted, minuendConverted.getDivisor())));
					return minuendConverted;
				}
				if((tmpMinuend instanceof Number) && (tmpSubtrahend instanceof Division)){
					Number minuendConverted = new Number(((Number)tmpMinuend).getValue());//TODO konstruktory kopiujace
					Division subtrahendConverted = new Division(((Division)tmpSubtrahend).getDividend(), ((Division)tmpSubtrahend).getDivisor());

					subtrahendConverted.setDividend(new Substraction(new Multiplication(minuendConverted, subtrahendConverted.getDivisor()), subtrahendConverted.getDivisor()));
					return subtrahendConverted;
				}
				if(minuend instanceof Division && subtrahend instanceof Division){
					Division minuendConverted = new Division(((Division)tmpMinuend).getDividend(), ((Division)tmpMinuend).getDivisor());
					Division subtrahendConverted = new Division(((Division)tmpSubtrahend).getDividend(), ((Division)tmpSubtrahend).getDivisor());
					ExpressionPart lcp = new LeastCommonMultiple(minuendConverted.getDivisor(), subtrahendConverted.getDivisor());

					minuendConverted.setDividend(new Multiplication(new Division(lcp, minuendConverted.getDivisor()), minuendConverted.getDividend()));
					minuendConverted.setDivisor(new Multiplication(new Division(lcp, minuendConverted.getDivisor()), minuendConverted.getDivisor()));
					subtrahendConverted.setDividend(new Multiplication(new Division(lcp, subtrahendConverted.getDivisor()), subtrahendConverted.getDividend()));
					subtrahendConverted.setDivisor(new Multiplication(new Division(lcp, subtrahendConverted.getDivisor()), subtrahendConverted.getDivisor()));

					return new Division(new Substraction(minuendConverted.getDividend(), subtrahendConverted.getDividend()), minuendConverted.getDivisor());
				}
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
