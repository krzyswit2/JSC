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

import com.krzygorz.calculator.misc.SettingsManager;
import com.krzygorz.calculator.parser.MathParser;

public class Division implements ExpressionPart{
	
	ExpressionPart dividend;//dzielnik
	ExpressionPart divisor;//dzielna
	
	public Division() {}
	
	public Division(ExpressionPart dividend, ExpressionPart divisor){
		this.dividend = dividend;
		this.divisor = divisor;
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
		
		if(tmpDivisor.matches(new Number(1))){
			return tmpDividend;
		}
		
		if(SettingsManager.getSetting("simplyfyToFraction").equals("1")){
			if(tmpDividend instanceof Division && tmpDivisor instanceof Number){
				Division dividendConverted = (Division)tmpDividend;
				Number divisorConverted = (Number)tmpDivisor;

				return new Division(dividendConverted.getDividend(), new Multiplication(dividendConverted.getDivisor(), divisorConverted)).simplyfy();
			}
			if(tmpDividend instanceof Number && tmpDivisor instanceof Division){
				Number dividendConverted = (Number)tmpDividend;
				Division divisorConverted = (Division)tmpDivisor;

				return new Multiplication(dividendConverted, new Division(divisorConverted.getDivisor(), divisorConverted.getDividend())).simplyfy();
			}
			if(tmpDividend instanceof Division && tmpDivisor instanceof Division){
				Division dividendConverted = (Division)tmpDividend;
				Division divisorConverted = (Division)tmpDivisor;

				return new Multiplication(dividendConverted, new Division(divisorConverted.getDivisor(), divisorConverted.getDividend())).simplyfy();
			}
			if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
				Number DividendConverted = (Number) tmpDividend;
				Number DivisorConverted = (Number) tmpDivisor;
				
				if(Operation.isInteger(DividendConverted.getValue() / DivisorConverted.getValue())){
					return new Number(DividendConverted.getValue() / DivisorConverted.getValue());
				}
				
				if(Operation.isInteger(DividendConverted.getValue()) && Operation.isInteger(DivisorConverted.getValue())){
					ExpressionPart gcd = new GreatestCommonDivisor(DividendConverted, DivisorConverted).simplyfy();
					
					if(gcd instanceof Number && ((Number)gcd).getValue() > 1){
						tmpDividend = new Division(DividendConverted, gcd);
						tmpDivisor = new Division(DivisorConverted, gcd);
						return new Division(tmpDividend, tmpDivisor).simplyfy();
					}else{
						return new Division(tmpDividend, tmpDivisor);
					}
				}else{
					while(!(Operation.isInteger(DividendConverted.getValue()) && Operation.isInteger(DivisorConverted.getValue()))){
						DividendConverted = new Number(DividendConverted.getValue() * 10);
						DivisorConverted = new Number(DivisorConverted.getValue() * 10);
					}
					return new Division(DividendConverted, DivisorConverted);
				}
				
			}
		}else{
			if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
				Number dividendConverted = (Number)tmpDividend;
				Number divisorConverted = (Number)tmpDivisor;
				
				return new Number(dividendConverted.getValue() / divisorConverted.getValue());
			}
		}
		return new Division(tmpDividend, tmpDivisor);
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
		ExpressionPart tmpDividend = dividend;
		ExpressionPart tmpDivisor = divisor;
		
		boolean isLast = true;
		if(dividend.canBeSimplified()){
			tmpDividend = this.dividend.nextStepToSimplyfy();
			isLast = false;
		}
		if(divisor.canBeSimplified()){
			tmpDivisor = this.divisor.nextStepToSimplyfy();
			isLast = false;
		}
		if(!isLast){
			return new Division(tmpDividend, tmpDivisor);
		}
		
		if(tmpDivisor.matches(new Number(1))){
			return tmpDividend;
		}
		
		if(SettingsManager.getSetting("simplyfyToFraction").equals("1")){
			if(tmpDividend instanceof Division && tmpDivisor instanceof Number){
				Division dividendConverted = (Division)tmpDividend;
				Number divisorConverted = (Number)tmpDivisor;

				return new Division(dividendConverted.getDividend(), new Multiplication(dividendConverted.getDivisor(), divisorConverted));
			}
			if(tmpDividend instanceof Number && tmpDivisor instanceof Division){
				Number dividendConverted = (Number)tmpDividend;
				Division divisorConverted = (Division)tmpDivisor;

				return new Multiplication(dividendConverted, new Division(divisorConverted.getDivisor(), divisorConverted.getDividend()));
			}
			if(tmpDividend instanceof Division && tmpDivisor instanceof Division){
				Division dividendConverted = (Division)tmpDividend;
				Division divisorConverted = (Division)tmpDivisor;

				return new Multiplication(dividendConverted, new Division(divisorConverted.getDivisor(), divisorConverted.getDividend()));
			}
			if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
				Number DividendConverted = (Number) tmpDividend;
				Number DivisorConverted = (Number) tmpDivisor;
				
				if(Operation.isInteger(DividendConverted.getValue() / DivisorConverted.getValue())){
					return new Number(DividendConverted.getValue() / DivisorConverted.getValue());
				}
				
				if(Operation.isInteger(DividendConverted.getValue()) && Operation.isInteger(DivisorConverted.getValue())){
					ExpressionPart gcd = new GreatestCommonDivisor(DividendConverted, DivisorConverted).simplyfy();
					
					if(gcd instanceof Number && ((Number)gcd).getValue() > 1){
						tmpDividend = new Division(DividendConverted, gcd).simplyfy();
						tmpDivisor = new Division(DivisorConverted, gcd).simplyfy();
						return new Division(tmpDividend, tmpDivisor);
					}else{
						return this;
					}
				}else{
					while(!(Operation.isInteger(DividendConverted.getValue()) && Operation.isInteger(DivisorConverted.getValue()))){
						DividendConverted = new Number(DividendConverted.getValue() * 10);
						DivisorConverted = new Number(DivisorConverted.getValue() * 10);
					}
					return new Division(DividendConverted, DivisorConverted);
				}
				
			}
		}else{
			if(tmpDividend instanceof Number && tmpDivisor instanceof Number){
				Number dividendConverted = (Number)tmpDividend;
				Number divisorConverted = (Number)tmpDivisor;
				
				return new Number(dividendConverted.getValue() / divisorConverted.getValue());
			}
		}
		return this;
		
	}
	
	@Override
	public String toString(){
		//String convertedNumerator = numerator.toString();
		//String convertedDenominator = denominator.toString();
		return "(".concat(this.dividend.toString()).concat(")").concat("/").concat("(").concat(this.divisor.toString()).concat(")");
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
		if(arg instanceof Division && (((Division)arg).getDividend().matches(this.getDividend())) && (((Division)arg).getDivisor().matches(this.getDivisor()))){
			return true;
		}
		return false;
	}
}
