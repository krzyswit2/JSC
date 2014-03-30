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

public class Variable implements ExpressionPart{
	private String name;
	public Variable() {
		name = "";
	}
	public Variable(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public ExpressionPart simplyfy() {
		return this;
	}
	@Override
	public boolean canBeSimplified() {
		return false;
	}
	@Override
	public ExpressionPart nextStepToSimplyfy() {
		return this;
	}
	
	@Override
	public String toString(){
		return this.name;
	}
	@Override
	public boolean matches(ExpressionPart arg) {
		if(arg instanceof Variable && (((Variable)arg).getName().equals(this.getName()))){
			return true;
		}
		return false;
	}
}
