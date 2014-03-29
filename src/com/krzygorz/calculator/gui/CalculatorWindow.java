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

package com.krzygorz.calculator.gui;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.krzygorz.calculator.logic.ExpressionPart;
import com.krzygorz.calculator.misc.LoggingInterface;
import com.krzygorz.calculator.parser.MathParser;

public class CalculatorWindow extends JFrame implements ActionListener, LoggingInterface{//I'm not using it, becouse I don't need gui now
	private JTextField input = new JTextField("enter expression here");
	private JTextArea outputArea = new JTextArea();
	private JTextArea logArea = new JTextArea();
	private JButton simplyfyButton = new JButton("Simplyfy");
	
	public CalculatorWindow(){
		this.setSize(500, 500);
		this.setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		input.setBounds(10, 10, 350, 20);
		this.add(input);
		
		simplyfyButton.setBounds(360, 10, 100, 20);
		simplyfyButton.setActionCommand("simplyfy");
		simplyfyButton.addActionListener(this);
		this.add(simplyfyButton);
		
		outputArea.setBounds(10, 40, 400, 200);
		outputArea.setEditable(false);
		this.add(outputArea);
		
		logArea.setBounds(10, 250, 400, 200);
		logArea.setEditable(false);
		this.add(logArea);
		
		this.setVisible(true);
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("simplyfy")){
			MathParser parser = new MathParser();
			System.out.println("enter expression to simplyfy");
			try {
				ExpressionPart parsedInput = parser.parseString(input.getText());
				System.out.println("result tree: " + parsedInput.toString());
				System.out.println("value: " + parsedInput.simplyfy());
				
				System.out.println("now trying to do this step by step");
				String toOutput = "";
				while(parsedInput.canBeSimplified()){
					toOutput = toOutput.concat(parsedInput.toString());
					toOutput = toOutput.concat(" = ");
					parsedInput = parsedInput.nextStepToSimplyfy();
				}
				toOutput = toOutput.concat(parsedInput.toString());
				outputArea.setText(toOutput);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}


	@Override
	public void Log(String message) {
		this.logArea.setText(logArea.getText() + "/n" + message);
	}
	
}
