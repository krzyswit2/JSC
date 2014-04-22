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

public class Fraction implements ExpressionPart {

	private ExpressionPart numerator;//liczebnik
	private ExpressionPart denominator;//mianownik
	private boolean isPositive;
	private boolean isSignChecked;
	
	public Fraction() {
		this.numerator = null;
		this.denominator = null;
		this.isPositive = true;
		this.isSignChecked = false;
	}
	
	public Fraction(ExpressionPart numerator, ExpressionPart denominator){
		this.setNumerator(numerator);
		this.setDenominator(denominator);
		this.isPositive = true;
		this.updateSign();
	}

	/*@Override
	public int getType() {
		return 1;
	}*/

	public void updateSign(){
		/*//System.out.println("isSignChecked: " + isSignChecked);
		if(this.numerator instanceof Number && (!this.isSignChecked)){
			if(((Number)numerator).getValue() < 0){
				//System.out.println("setting as negative");
				this.isPositive = false;
				this.numerator = new Number(Math.abs(((Number) this.numerator).getValue()));
			}else{
				//System.out.println("setting as positive");
				this.isPositive = true;
			}
			this.isSignChecked = true;
		}*/
	}
	
	@Override
	public ExpressionPart simplyfy() {//TODO skracanie ulamkow pietrowych(robota dla tryToTransform) typu (1\4)\2 = (4/4)\8 = 1\8 lub (1\4)\2 = (1\4)*(1\2) = (1\8)
		ExpressionPart tmpNumerator = numerator;
		ExpressionPart tmpDenominator = denominator;
		
		if(tmpNumerator.canBeSimplified()){
			tmpNumerator = tmpNumerator.simplyfy();
		}
		if(tmpDenominator.canBeSimplified()){
			tmpDenominator = tmpDenominator.simplyfy();
		}
		if(tmpNumerator instanceof Number && tmpDenominator instanceof Number){
			Number numeratorConverted = (Number) tmpNumerator;
			Number denominatorConverted = (Number) tmpDenominator;
			
			if(Operation.isInteger(numeratorConverted.getValue()) && Operation.isInteger(denominatorConverted.getValue())){
				ExpressionPart gcd = new GreatestCommonDivisor(numeratorConverted, denominatorConverted).simplyfy();
				
				if(gcd instanceof Number && ((Number)gcd).getValue() > 1){
					tmpNumerator = new Division(numeratorConverted, gcd).simplyfy();
					tmpDenominator = new Division(denominatorConverted, gcd).simplyfy();
				}
			}else{
				while(!(Operation.isInteger(numeratorConverted.getValue()) && Operation.isInteger(denominatorConverted.getValue()))){
					numeratorConverted = new Number(numeratorConverted.getValue() * 10);
					denominatorConverted = new Number(denominatorConverted.getValue() * 10);
				}
				return new Fraction(numeratorConverted, denominatorConverted).simplyfy();
			}
			
		}
		
		if(tmpDenominator instanceof Number){
			if(((Number)tmpDenominator).getValue() == 1){
				return tmpNumerator;
			}
		}
		
		Fraction retValue = new Fraction(tmpNumerator, tmpDenominator);
		/*retValue.setSign(this.isPositive);
		if(tmpNumerator instanceof Number){
			if(((Number)tmpNumerator).getValue() < 0){
				retValue.setSign(false);
				tmpNumerator = new Number(Math.abs(((Number)tmpNumerator).getValue()));
				retValue.setSignChecked(true);
			}
		}*/
		
		
		return retValue;
	}

	@Override
	public boolean canBeSimplified() {
		if(this.numerator != null && this.denominator != null){
			if(this.numerator instanceof Number && this.denominator instanceof Number){
				Number numeratorConverted = (Number) numerator;
				Number denominatorConverted = (Number) denominator;
				if(Operation.isInteger(numeratorConverted.getValue()) && Operation.isInteger(denominatorConverted.getValue())){
					ExpressionPart gcd = new GreatestCommonDivisor(numeratorConverted, denominatorConverted).simplyfy();
					if(gcd instanceof Number && ((Number)gcd).getValue() > 1){
						return true;
					}
				}else{
					return true;
				}
			}
			
			if(denominator instanceof Number){
				if(((Number)denominator).getValue() == 1){
					return true;
				}
			}
			
			if(numerator.canBeSimplified() || denominator.canBeSimplified())
				return true;
		}
		return false;
	}

	/*@Override
	public Countable doOperation(HashMap<Integer, Countable> args,
			String operator) {
		System.out.println("Fraction DoOperation");
		if(args.size() == 1 && args.get(0) != null){
			if(operator.equals("+") || operator.equals("-")){
				HashMap<Integer, Countable> tmpArgs = new HashMap<Integer, Countable>();
				tmpArgs.put(0, this);
				tmpArgs.put(1, args.get(0));
				return new ExpressionTree(tmpArgs, operator);
			}
			if(operator.equals("*") || operator.equals("/")){
				numerator.doOperation(args, operator);
				return this;
			}
		}
		
		return null;
	}*/
	
	private void setSign(boolean sign){
		this.isPositive = sign;
	}
	
	private void setSignChecked(boolean isChecked){
		this.isSignChecked = isChecked;
	}
	
	public ExpressionPart getNumerator() {
		return numerator;
	}

	public void setNumerator(ExpressionPart numerator) {
		this.numerator = numerator;
		this.isSignChecked = false;
		this.updateSign();
	}

	public ExpressionPart getDenominator() {
		return denominator;
	}

	public void setDenominator(ExpressionPart denominator) {
		this.denominator = denominator;
		this.updateSign();
	}
	
	@Override
	public String toString(){
		//String convertedNumerator = numerator.toString();
		//String convertedDenominator = denominator.toString();
		if(this.numerator != null && this.denominator != null){
			String returnVal = "(".concat(this.numerator.toString()).concat(")").concat("\\").concat("(").concat(this.denominator.toString()).concat(")");
			if(this.isPositive){
				return returnVal;
			}else{
				return "-".concat("(").concat(returnVal).concat(")");
			}
		}
		return "something is null!";
	}

	@Override
	public ExpressionPart nextStepToSimplyfy() {
		ExpressionPart tmpNumerator = numerator;
		ExpressionPart tmpDenominator = denominator;
		
		if(tmpDenominator instanceof Number){
			if(((Number)tmpDenominator).getValue() == 1){
				return tmpNumerator;
			}
		}
		
		boolean end = false;
		if(tmpNumerator.canBeSimplified()){
			tmpNumerator = tmpNumerator.nextStepToSimplyfy();
			end = true;
		}
		if(tmpDenominator.canBeSimplified()){
			tmpDenominator = tmpDenominator.nextStepToSimplyfy();
			end = true;
		}
		
		if(end){
			Fraction retValue = new Fraction();
			retValue.setNumerator(tmpNumerator);
			retValue.setDenominator(tmpDenominator);
			
			return retValue;
		}
		
		if(tmpNumerator instanceof Number && tmpDenominator instanceof Number){
			Number numeratorConverted = (Number) tmpNumerator;
			Number denominatorConverted = (Number) tmpDenominator;
			
			if(Operation.isInteger(numeratorConverted.getValue()) && Operation.isInteger(denominatorConverted.getValue())){
				ExpressionPart gcd = new GreatestCommonDivisor(numeratorConverted, denominatorConverted).simplyfy();
				if(gcd instanceof Number && ((Number)gcd).getValue() > 1){
					tmpNumerator = new Division(numeratorConverted, gcd);
					tmpDenominator = new Division(denominatorConverted, gcd);
				}
			}else{
				while(!(Operation.isInteger(numeratorConverted.getValue()) && Operation.isInteger(denominatorConverted.getValue()))){
					numeratorConverted = new Number(numeratorConverted.getValue() * 10);
					denominatorConverted = new Number(denominatorConverted.getValue() * 10);
				}
				return new Fraction(numeratorConverted, denominatorConverted);
			}
		}
		
		
		
		Fraction retValue = new Fraction();
		retValue.setNumerator(tmpNumerator);
		retValue.setDenominator(tmpDenominator);
		retValue.setSign(this.isPositive);
		if(tmpNumerator instanceof Number){
			if(((Number)tmpNumerator).getValue() < 0){
				retValue.setSign(false);
				tmpNumerator = new Number(Math.abs(((Number)tmpNumerator).getValue()));
				retValue.setSignChecked(true);
			}
		}
		return retValue;
	}

	public boolean isPositive() {
		return isPositive;
	}

	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Fraction && (((Fraction)arg).getNumerator() == this.getNumerator()) && (((Fraction)arg).getDenominator() == this.getDenominator())){
			return true;
		}
		return false;
	}

}
