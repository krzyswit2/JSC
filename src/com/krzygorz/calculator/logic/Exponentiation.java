package com.krzygorz.calculator.logic;

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
	public ExpressionPart simplyfy() {
		if(base instanceof Number && exponent instanceof Number){
			return new Number(Math.pow(((Number)base).getValue(), ((Number)exponent).getValue()));
		}
		return null;
	}

	@Override
	public boolean canBeSimplified() {
		if(!this.matches(simplyfy())){
			return true;
		}
		return false;
	}

	@Override
	public ExpressionPart nextStepToSimplyfy() {
		if(base instanceof Number && exponent instanceof Number){
			return new Number(Math.pow(((Number)base).getValue(), ((Number)exponent).getValue()));
		}
		ExpressionPart tmpbase = base;
		ExpressionPart tmpexponent = exponent;
		if(base.canBeSimplified()){
			tmpbase = base.nextStepToSimplyfy();
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
