package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Validator {
	private Scanner code; // Shouldn't be touched outside of this class
	private ArrayList<String> reservedKeywords;
	private Map<String, DataType> declaredVariables;
	private boolean isValidated;
	private boolean isValid;
	private Stack<Character> parens;
	
	public Validator(String filename) {
		// TODO Fix this so it reads file as scanner object
		code = null;
		
//		scan.useDelimiter(";"); // TODO This delimiter may need to be changed
//		Should it be \n? How do we evaluate a file with no line breaks, could we recurse when we hit a ;?
		
		getReservedKeywords();
		declaredVariables = new HashMap<>();
		isValidated = false;
		isValid = false; // Assume invalid
	}
	
	/**
	 * Tests if the file scanned by Scanner object contains valid Java code.
	 * @param scan
	 */
	public void validate() {
		// check if the code has been scanned already, escape to prevent duplicate scans
		
		
		String nextLine;
		while(code.hasNext()) {
			nextLine = code.next(); // Uses delimiter outlined when scanner was constructed
			if(nextLine.contains("if(") || nextLine.contains("if (")) { // There's probably a way to check with a regex?
				if(isValidIf(nextLine)) {
					
				} else {
					return; // Not valid, exit method
				}
			}
		}
		
		
		// Assume that if the code has reached this point of execution, then it has found no errors (Error causes early exiting)
		isValid = true;
		isValidated = true;
		return; 
	}
	
	/**
	 * Tests if the string passed is a valid if statement and it contains a valid statement or code block.
	 * @param ifBlock
	 * @return True if if-block is valid, false if error is encountered.
	 */
	public boolean isValidIf(String ifBlock) {
		// TODO Sam
		// if not valid IF, throw Exception
		// Check if it contains a valid bool
		// inc first index, exc last index
		// Check if it contains a valid statement or compound statement
		return true;
	}
	
	/**
	 * Returns true if the string is a valid boolean expression
	 * @param boolExp
	 * @return true if boolExp is a valid boolean expression.
	 */
	public boolean isValidBoolExpression(String boolExp) {
		// TODO Sam
		// Recurse if || or && is found
		// Check datatypes being compared, do they match?
		// Does the string contain a valid comparator operator? { <, >, ==, !=, <=, >= }
		// Check if string is hard-coded { true, false }
		return true;
	}
	
	/**
	 * Returns true if the string passed is a valid for loop
	 * @param forLoop
	 * @return true if forLoop contains a valid loop
	 */
	public boolean isValidFor(String forLoop) {
		// check parameters in () at start of loop, calling other methods like isValidBoolExp()
		// Check that there is a valid statement after the declaration of the for loop
		return true;
	}
	
	/**
	 * Returns true if the string passed is a valid Switch Statement
	 * @param switchBlock
	 * @return True if Switch statement is valid
	 */
	public boolean isValidSwitch(String switchBlock) {
		// Check types in block, do they all match?
		// is the syntax correct?
		return true;
	}
	
	/**
	 * Returns true if the string passed is a valid While Loop
	 * @param whileLoop
	 * @return True if while loop is valid
	 */
	public boolean isValidWhile(String whileLoop) {
		// Check that block is a valid block
		// Check that statement at end of block contains a valid boolean expression
		return true;
	}
	
	/**
	 * Returns true if the string passed is a valid simple statement
	 * @param simpleStatement
	 * @return True if simple statement is valid
	 */
	public boolean isValidSimpleStatement(String simpleStatement) {
		// Check if it is an assignment statement
		// check if it is a Sysout
		// Check for in-line operators { ++, -- }
		// Check for System.out.println();
		// Check for comments both // line and /* block */
		return true;
	}
	
	/**
	 * Returns true if the parentheses or bracket passed to the method either:
	 * Successfully closes a set of parentheses or brackets.
	 * Is successfully added to the stack.
	 * @param paren
	 * @return True if the parentheses is correctly placed.
	 */
	public boolean isValidParens(char paren) {
		// Check if opening or closing bracket
		// if Opening, add to stack, return true
		// if Closing, peek at stack to see if it matches the one at the top
		// 		if it does match, pop and return true
		//		if it does not match, throw an exception
		return true;
	}
	
	/**
	 * Returns true if the string passed is a valid method signature
	 * @param methodSignature
	 * @return True if the method signature is valid
	 */
	public boolean isValidMethodSignature(String methodSignature) {
		// check keywords are valid and in expected order
		// V1: Accept no parameters, void return type
		// V2: If we have time (probably not) add passed parameters to declaredVariables map
		// and check for a return statement that matches the return type (keep as class variable?)
		return true;
	}
	
	/**
	 * Parses a string and gets the datatype of the data stored in the string
	 * @param data
	 * @return enum DataType matching the data stored in the string
	 */
	public DataType getType(String data) {
		// TODO Sam
		// Check empty string (Edge case)
		if(data.isBlank()) {
			return null;
		}
		
		// Check if data passed is a variable, get datatype from the declared variable map
		if(declaredVariables.containsKey(data)) {
			return declaredVariables.get(data);
		}
		
		// check int
		try {
			Integer.parseInt(data);
			return DataType.INT;
		} catch (NumberFormatException nfe) {}

		// check double
		try {
			Double.parseDouble(data);
			return DataType.DOUBLE;
		} catch (NumberFormatException nfe) {}
		
		// check boolean
		if(data.equals("true") || data.equals("false")) {
			return DataType.BOOLEAN;
		}
		// check if it's a boolean expression
		//TODO Sam
		
		// check char
		if(data.length() == 3 && data.charAt(0) == '\'' &&  data.charAt(2) == '\'') {
			return DataType.CHAR;
		}
		
		// Throw an error at this point? We haven't found a matching DataType so it's invalid code?
		return null;
	}
	
	public boolean isValidated() {
		return isValidated;
	}
	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}
	public boolean isValid() {
		return isValid;
	}
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * Adds a variable to the list of declared variables
	 * @param variableName
	 * @param type
	 * @return True if successful, false if failed.
	 */
	public boolean addVariable(String variableName, DataType type) {
		// Check if variableName is a reserved keyword
		if(reservedKeywords.contains(variableName)) {
			return false;
		}
		
		// check if variable was declared previously
		// .get will not return null if it has been declared previously
		if(declaredVariables.get(variableName) != null) {
			return false;
		}

		// Validate variable name
//		if(variableName.startsWith("_") || variableName.startsWith("$") || Character.isLetter(variableName.charAt(0))) {
		if(variableName.matches("[A-z$_]{1}[A-z0-9$_]*")) {
			declaredVariables.put(variableName, type);
			return true;
		}
		
		// All previous attempts to validate the variable name have failed, assume invalid
		return false;
	}
	
	/**
	 * Reads in Java Reserved Keywords from file
	 * @return List of reserved keywords
	 */
	private ArrayList<String> getReservedKeywords(){
		Scanner s = null;
		try {
			s = new Scanner(new File("ReservedKeywords.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		reservedKeywords = new ArrayList<String>();
		while (s.hasNext()){
		    reservedKeywords.add(s.next());
		}
		s.close();
		return reservedKeywords;
	}
}
