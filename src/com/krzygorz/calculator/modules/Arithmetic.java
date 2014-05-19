package com.krzygorz.calculator.modules;

import java.util.Vector;

import com.krzygorz.calculator.tree.Addition;
import com.krzygorz.calculator.tree.Division;
import com.krzygorz.calculator.tree.ExpressionPart;
import com.krzygorz.calculator.tree.Multiplication;
import com.krzygorz.calculator.tree.Number;
import com.krzygorz.calculator.tree.Substraction;

public class Arithmetic implements Module {
	
	private ExpressionPart simplyfyAddition(Addition arg) {
		boolean isLast = true;
		Vector<ExpressionPart> addends = arg.getAddends();
		for(ExpressionPart i : addends){
			//System.out.println("current arg: " + arg.toString());
			if(canSimplyfy(i)){
				isLast = false;
				break;
			}
		}

		if(isLast){
			if(addends != null){
				if(addends.size() > 1){
					Vector<ExpressionPart> toAdd = new Vector<ExpressionPart>(addends);
					Vector<ExpressionPart> result = new Vector<ExpressionPart>();
					while(toAdd.size() != 0){
						ExpressionPart tmp = null;
						Vector<ExpressionPart> toAddNext = new Vector<ExpressionPart>();
						for(ExpressionPart i : toAdd){
							ExpressionPart newTmp = null;
							if(i == null){
								newTmp = tmp; 
							}
							if(tmp == null){
								newTmp = i; 
							}
							if(tmp instanceof Number && i instanceof Number){
								newTmp = new Number(((Number)tmp).getValue() + ((Number)i).getValue());
							}
							if(newTmp != null){
								tmp = newTmp;
							}else{
								toAddNext.add(i);
							}
						}
						toAdd = toAddNext;
						result.add(tmp);
					}
					if(result.size() == 1){
						return result.get(0);
					}else{
						return new Addition(result);
					}
				}else if(addends.size() == 1){
					return addends.get(0);
				}
			}
		}else{
			Addition retValue = new Addition();
			for(ExpressionPart tmp : addends){
				if(!(tmp instanceof Number) && canSimplyfy(tmp)){
					//System.out.println("next NaN arg: " + tmp);
					tmp = simplyfy(tmp);
				}
				retValue.addAddend(tmp);
			}
			return retValue;
		}
		return null;
	}
	
	private ExpressionPart simplyfyMultiplication(Multiplication arg){
		boolean isLast = true;
		Vector<ExpressionPart> factors = arg.getFactors();
			if(factors != null){
				if(factors.size() > 1){
					Vector<ExpressionPart> toMultiply = factors;
					Vector<ExpressionPart> result = new Vector<ExpressionPart>();
					while(toMultiply.size() != 0){
						ExpressionPart tmp = null;
						Vector<ExpressionPart> toMultiplyNext = new Vector<ExpressionPart>();
						for(ExpressionPart i : toMultiply){
							ExpressionPart newTmp = null;
							if(i == null){
								newTmp = tmp; 
							}
							if(tmp == null){
								newTmp = i; 
							}
							if(tmp instanceof Number && i instanceof Number){
								newTmp = new Number(((Number)tmp).getValue() * ((Number)i).getValue());
							}
							if(newTmp != null){
								tmp = newTmp;
							}else{
								toMultiplyNext.add(i);
							}
						}
						toMultiply = toMultiplyNext;
						result.add(tmp);
					}
					if(result.size() == 1){
						return result.get(0);
					}else{
						return new Multiplication(result);
					}
				}else if(factors.size() == 1){
					return factors.get(0);
				}
			}
			Multiplication retValue = new Multiplication();
			for(ExpressionPart tmp : factors){
				if(canSimplyfy(tmp)){
					//System.out.println("next NaN arg: " + tmp);
					tmp = simplyfy(tmp);
				}
				retValue.addFactor(tmp);
			}
			return retValue;
		//return null;
	}
	private ExpressionPart simplyfySubstraction(Substraction arg){
		if(arg.getMinuend() instanceof Number && arg.getSubtrahend() instanceof Number){
			return new Number(((Number)arg.getMinuend()).getValue() - ((Number)arg.getSubtrahend()).getValue());
		}
		if(canSimplyfy(arg.getMinuend())){
			arg.setMinuend(simplyfy(arg.getMinuend()));
		}
		if(canSimplyfy(arg.getSubtrahend())){
			arg.setSubtrahend(simplyfy(arg.getSubtrahend()));
		}
		return null;
	}
	private ExpressionPart simplyfyDivision(Division arg){
		if(arg.getDividend() instanceof Number && arg.getDivisor() instanceof Number){
			return new Number(((Number)arg.getDividend()).getValue() / ((Number)arg.getDivisor()).getValue());
		}
		if(canSimplyfy(arg.getDividend())){
			arg.setDividend(simplyfy(arg.getDividend()));
		}
		if(canSimplyfy(arg.getDivisor())){
			arg.setDivisor(simplyfy(arg.getDivisor()));
		}
		return null;
	}
	@Override
	public boolean canSimplyfy(ExpressionPart arg){
		if(arg == null){
			return false;
		}
		ExpressionPart simplifiedArg = simplyfy(arg);
		if(simplifiedArg != null){
			return !simplifiedArg.matches(arg);
		}
		return false;
	}
	@Override
	public ExpressionPart simplyfy(ExpressionPart arg) {
		if(arg instanceof Addition)
			return simplyfyAddition(new Addition(arg));
		if(arg instanceof Multiplication)
			return simplyfyMultiplication(new Multiplication(arg));
		if(arg instanceof Substraction)
			return simplyfySubstraction(new Substraction(arg));
		if(arg instanceof Division)
			return simplyfyDivision((Division)arg);
		return null;
	}

}
