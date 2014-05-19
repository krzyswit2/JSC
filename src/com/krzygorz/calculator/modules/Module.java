package com.krzygorz.calculator.modules;

import com.krzygorz.calculator.tree.ExpressionPart;

public interface Module {
	ExpressionPart simplyfy(ExpressionPart arg);
	boolean canSimplyfy(ExpressionPart arg);
}
