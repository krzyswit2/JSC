package com.krzygorz.calculator.modules;

import java.util.Vector;

import com.krzygorz.calculator.tree.Addition;
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
			if(i.canBeSimplified()){
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
				if(!(tmp instanceof Number) && tmp.canBeSimplified()){
					//System.out.println("next NaN arg: " + tmp);
					tmp = tmp.simplyfy();
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
		for(ExpressionPart i : factors){
			if(i.canBeSimplified()){
				isLast = false;
				break;
			}
		}
		
		if(isLast){
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
		}else{
			Multiplication retValue = new Multiplication();
			for(ExpressionPart tmp : factors){
				if(tmp.canBeSimplified()){
					//System.out.println("next NaN arg: " + tmp);
					tmp = tmp.simplyfy();
				}
				retValue.addFactor(tmp);
			}
			return retValue;
		}
		return null;
	}
	private ExpressionPart simplyfySubstraction(Substraction arg){
		if(arg.getMinuend() instanceof Number && arg.getSubtrahend() instanceof Number){
			return new Number(((Number)arg.getMinuend()).getValue() - ((Number)arg.getSubtrahend()).getValue());
		}
		if(arg.getMinuend().canBeSimplified()){
			arg.setMinuend(simplyfy(arg.getMinuend()));
		}
		return null;
	}
	@Override
	public ExpressionPart simplyfy(ExpressionPart arg) {
		if(arg instanceof Addition)
			return simplyfyAddition((Addition)arg);
		if(arg instanceof Multiplication)
			return simplyfyMultiplication((Multiplication)arg);
		if(arg instanceof Substraction)
			return simplyfySubstraction((Substraction)arg);
		return null;
	}

}
