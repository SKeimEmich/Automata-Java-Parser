package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.ParserException;

public class Validator {
	private String code; // Shouldn't be touched outside of this class
	private ArrayList<String> reservedKeywords;
	private Map<String, DataType> declaredVariables;
	private boolean isValidated;
	private boolean isValid;
	private Stack<Character> parens;

	// Constructor
	public Validator(String fileName) {
		// Construct a scanner object to read from file
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = null;
			String separator = "\n";
			while((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(separator);
			}
			// remove last line separator
			sb.deleteCharAt(sb.length() - 1);
			reader.close();
			code = sb.toString();
		} catch (FileNotFoundException fnfe) {
			System.err.printf("File %s not found, please try again.", fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		getReservedKeywords();
		declaredVariables = new HashMap<>();
		isValidated = false;
		isValid = false; // Assume invalid
	}

	// Setup Methods for the constructor to use
	/**
	 * Reads in Java Reserved Keywords from file
	 * 
	 * @return List of reserved keywords
	 */
	private ArrayList<String> getReservedKeywords() {
		Scanner s = null;
		try {
			s = new Scanner(new File("ReservedKeywords.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		reservedKeywords = new ArrayList<String>();
		while (s.hasNext()) {
			reservedKeywords.add(s.next());
		}
		s.close();
		return reservedKeywords;
	}

	// Variable access methods
	public boolean isValidated() {
		return isValidated;
	}

	public boolean isValid() {
		return isValid;
	}

	// Main Method to be run

	/**
	 * Tests if the file scanned by Scanner object contains valid Java code.
	 * 
	 * @param scan
	 */
	public void validate() {
		// check if the code has been scanned already, escape to prevent duplicate scans
		if(isValidated) {
			return;
		}
		
		// get indices of code block braces
		int indexOfOpeningBrace = code.indexOf("{");
		int indexOfClosingBrace = getPositionOfClosingBrace(code.substring(indexOfOpeningBrace));		
		
		// if there is text after the closing brace, throw an error
		if(code.substring(indexOfClosingBrace).isBlank()) {
			throw new ParserException("Cannot have code outside of method code block.");
		}
		
		// if there is no opening brace, throw an error
		if(indexOfOpeningBrace == -1) {
			throw new ParserException("No code block exists.");
		}
		
		// Validate method signature
		String methodSignature = code.substring(0, indexOfOpeningBrace);
		isValidMethodSignature(methodSignature);
		
		// get code block (block belonging to method)
		String codeBlock = code.substring(indexOfOpeningBrace);
		isValidCodeBlock(codeBlock);
		
		// Assume that if the code has reached this point of execution, then it has
		// found no errors (Error causes early exiting)
		isValid = true;
		isValidated = true;
	}

	// Methods used to parse lines and blocks, alphabetical by Author last name

	/**
	 * //todo Jon Returns true if the string passed is a valid for loop
	 * 
	 * @param forLoop
	 * @return true if forLoop contains a valid loop
	 */
	public boolean isValidFor(String forLoop) {
		// check parameters in () at start of loop, calling other methods like
		// isValidBoolExp()
		// Check that there is a valid statement after the declaration of the for loop
		return true;
	}

	/**
	 * todo Jon Returns true if the string passed is a valid simple statement
	 * 
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
	 * Todo: Jon Returns true if the string passed is a valid method signature
	 * 
	 * @param methodSignature
	 * @return True if the method signature is valid
	 */
	public boolean isValidMethodSignature(String methodSignature) {
		// check keywords are valid and in expected order
		// V1: Accept no parameters, void return type
		// V2: If we have time (probably not) add passed parameters to declaredVariables
		// map
		// and check for a return statement that matches the return type (keep as class
		// variable?)
		return true;
	}

	/**
	 * Tests if the string passed is a valid if statement and it contains a valid
	 * statement or code block.
	 * 
	 * @param ifBlock
	 * @return True if if-block is valid, false if error is encountered.
	 */
	public boolean isValidIf(String ifBlock) {
		// Author Sam
		
		// Check if the IF statement does not match the pattern "if() "
		if (!ifBlock.matches("\\A\\s*if\\s?\\(.+\\).*")) {
			throw new ParserException(String.format("Invalid if statement: \"%s\".", ifBlock));
		}

		// It does match the pattern for IF(), proceed
		
		// inc first index, exc last index
		// Check if it contains a valid boolean expression
		int startIndex = ifBlock.indexOf('(') + 1;
		int endIndex = ifBlock.indexOf(')');
		if (!isValidBoolExpression(ifBlock.substring(startIndex, endIndex))) {
			throw new ParserException("I don't know how you got here, so congratulations on that.");
		}

		// remove if() from ifBlock
		String remainingIf = ifBlock.substring(endIndex + 1).trim();
		// If there's nothing after the if statement, it's invalid
		if (remainingIf.length() == 0) {
			throw new ParserException(String.format("Invalid if statement %s, expected statement at end.", ifBlock));
		}

		// Get block of code if it exists
		if (remainingIf.charAt(0) == '{') {
			int indexOfClosingBrace = getPositionOfClosingBrace(remainingIf);
			if (indexOfClosingBrace > 0) {
				String codeBlock = remainingIf.substring(0, indexOfClosingBrace + 1);
				isValidCodeBlock(codeBlock); // This method will throw an error if it is invalid
			}
		} else {
			// Remaining code in block is assumed to be a simple statement
			isValidSimpleStatement(remainingIf); // This method will throw an error if it is invalid				
		}
		return true;
	}

	/**
	 * Returns the index of the closing brace.
	 * 
	 * @param code that begins with an opening brace
	 * @return index of closing brace that matches first brace, or -1 if code does
	 *         not begin with an opening brace
	 */
	public int getPositionOfClosingBrace(String code) {
		// Author Sam
		// trim code block as redundancy
		code = code.trim();
		// the code passed should start with an opening brace ( {, (, or [ )
		String openingBrace = Character.toString(code.charAt(0));
		if (!openingBrace.matches("(\\{|\\(|\\[)")) { // Confirm that code begins with opening brace
			return -1;
		}
		// Find the index of the brace that closes this block
		// Declare variables
		Deque<Character> stack = new ArrayDeque<Character>();
		int index = 0;
		char character, stackMatch;
		// Push starting brace to stack
		stack.push(openingBrace.charAt(0));
		// while stack is not empty
		while (!stack.isEmpty()) {
			index++;
			// if we've reached the end of the string and the stack is not empty, throw an
			// error
			if (index == code.length()) {
				throw new ParserException(String.format("Braces do not match within code block, %s is not closed.%n%s",
						stack.peek(), code));
			}
			// iterate over string
			character = code.charAt(index);
			// push opening braces to stack
			if (character == '{' || character == '(' || character == '[') {
				stack.push(character);
			}
			// when closing brace is encountered, pop from stack
			stackMatch = stack.peek();
			if ((stackMatch == '{' && character == '}') || (stackMatch == '(' && character == ')')
					|| (stackMatch == '[' && character == ']')) {
				stack.pop();
			}
		}
		// return index when stack is empty
		return index;
	}

	/**
	 * Returns true if the string is a valid boolean expression
	 * 
	 * @param boolExp
	 * @return true if boolExp is a valid boolean expression.
	 */
	public boolean isValidBoolExpression(String boolExp) {
		// Author Sam
		Matcher andOrMatcher = Pattern.compile("(\\|\\|?|\\&\\&?)").matcher(boolExp);
		// Recurse if ||, &&, |, & is found
		if (andOrMatcher.find()) {
			// Split into first half and second half
			int operatorIndex = andOrMatcher.end();
			// .replaceAll() on first half clears the operator that we're splitting on
			String firstHalf = boolExp.substring(0, operatorIndex).replaceAll("(\\|\\|?|\\&\\&?)", "").trim();
			// No .replaceAll() on the second half, which might contain additional operators
			String secondHalf = boolExp.substring(operatorIndex).trim();
			// Recurse
			return isValidBoolExpression(firstHalf) && isValidBoolExpression(secondHalf);
		}

		// Does the string contain a valid comparator operator? { <, >, ==, !=, <=, >= }
		Matcher matcher = Pattern.compile("(<=?|>=?|==|!=)").matcher(boolExp);
		if (!matcher.find()) {
			// no operators were found
			// Check if string is hard-coded { true, false }
			if (getType(boolExp) == DataType.BOOLEAN) {
				return true;
			}
			throw new ParserException(
					String.format("Could not find a valid operator in the expression \"%s\".", boolExp));
		} else { // operators were found in boolExp, continue
			// Get index of operator
			int operatorIndex = matcher.end();

			// Separate operands into substrings
			String leftOperand = boolExp.substring(0, operatorIndex).replaceAll("(<=?|>=?|==|!=)", "").trim();
			String rightOperand = boolExp.substring(operatorIndex).replaceAll("(<=?|>=?|==|!=)", "").trim();

			// Cannot compare booleans
			if (getType(leftOperand) == DataType.BOOLEAN || getType(rightOperand) == DataType.BOOLEAN) {
				throw new ParserException("Error, cannot compare booleans.");
			}

			// All other datatypes can be compared (int, double, char)
			return true;
		}
	}

	/**
	 * Parses a string and gets the datatype of the data stored in the string
	 * 
	 * @param data
	 * @return enum DataType matching the data stored in the string
	 */
	public DataType getType(String data) {
		// Author Sam
		// Check empty string (Edge case)
		if (data.isBlank()) {
			throw new ParserException(String.format("Invalid data type, data was blank.", data));
		}

		// Check if data passed is a variable, get datatype from the declared variable
		// map
		if (declaredVariables.containsKey(data)) {
			return declaredVariables.get(data);
		}

		// check int
		try {
			Integer.parseInt(data);
			return DataType.INT;
		} catch (NumberFormatException nfe) {
		}

		// check double
		try {
			Double.parseDouble(data);
			return DataType.DOUBLE;
		} catch (NumberFormatException nfe) {
		}

		// check boolean
		if (data.equals("true") || data.equals("false")) {
			return DataType.BOOLEAN;
		}

		// check char
		if (data.length() == 3 && data.charAt(0) == '\'' && data.charAt(2) == '\'') {
			return DataType.CHAR;
		}

		throw new ParserException(String.format("Invalid data type: %s.", data));
	}

	/**
	 * Adds a variable to the list of declared variables
	 * 
	 * @param variableName
	 * @param type
	 * @return True if successful, false if failed (Nothing was added to the map).
	 */
	public boolean addVariable(String variableName, DataType type) {
		// Author Sam
		// Check if variableName is a reserved keyword
		if (reservedKeywords.contains(variableName)) {
			throw new ParserException(String.format("%s is a reserved keyword.", variableName));
		}

		// check if variable was declared previously
		// .get will not return null if it has been declared previously
		if (declaredVariables.get(variableName) != null) {
			throw new ParserException(String.format("Variable %s was declared previously.", variableName));
		}

		// Validate variable name (Can begin with alphanumeric or $, followed by any
		// alphanumeric, digit, $ or _)
		if (variableName.matches("[A-z$_]{1}[A-z0-9$_]*")) {
			declaredVariables.put(variableName, type);
			return true;
		}

		// All previous attempts to validate the variable name have failed, assume
		// invalid
		throw new ParserException(String.format("Unknown error adding variable (%s, %s).", variableName, type));
	}

	/**
	 * Returns true if the string passed is a valid Switch Statement
	 * 
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
	 * 
	 * @param whileLoop
	 * @return True if while loop is valid
	 */
	public boolean isValidWhile(String whileLoop) {
		// Check that block is a valid block
		// Check that statement at end of block contains a valid boolean expression
		return true;
	}

	/**
	 * Returns true if the string passed is a valid code block.
	 * 
	 * @param String codeBlock
	 * @return True if the code block is valid.
	 */
	public boolean isValidCodeBlock(String codeBlock) {
		// Check if beginning of string is a keyword for a complex statement
		// if so, read up to end of that statement and send to isValidStatement
		// if not, read up to ; and send to isValidSimpleStatement
		// loop until end is reached, must end with }
		return true;
	}

	/**
	 * 
	 */
	public boolean isValidStatement(String statement) {
		// Check if it begins with a keyword (if, for, while, do, switch)
		// send to appropriate method
		// if not, throw a fit
		return true;
	}
}
