package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.ParserException;

public class Validator {
	private Scanner code; // Shouldn't be touched outside of this class
	private ArrayList<String> reservedKeywords;
	private Map<String, DataType> declaredVariables;
	private boolean isValidated;
	private boolean isValid;
	private Stack<Character> parens;

	// Constructor
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

	// Setup Methods for the constructor to use

	/**
	 * Reads in Java Reserved Keywords from file
	 *
	 * @return List of reserved keywords
	 */
	private ArrayList<String> getReservedKeywords() {
		Scanner s = null;
		try {
			s = new Scanner(new File("Automata-Java-Parser/ReservedKeywords.txt"));
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

		String nextLine;
		while (code.hasNext()) {
			nextLine = code.next(); // Uses delimiter outlined when scanner was constructed
			if (nextLine.contains("if(") || nextLine.contains("if (")) { // There's probably a way to check with a
				// regex?
				if (isValidIf(nextLine)) {

				} else {
					return; // Not valid, exit method
				}
			}
		}

		// Assume that if the code has reached this point of execution, then it has
		// found no errors (Error causes early exiting)
		isValid = true;
		isValidated = true;
		return;
	}

	// Methods used to parse lines and blocks, alphabetical by Author last name
	/**
	 *author Jon
	 * @param forLoop
	 * @return true if forLoop contains a valid loop
	 */
	public boolean isValidFor(String forLoop) {

		// Check for valid for loop declaration/control block
		Matcher forMatcher = Pattern.compile("^\\s*for\\(.*;.*;.*\\)\\{$").matcher(forLoop);

		if(forMatcher.find()) {
			//trim whitespace, extract substring between parentheses
			forLoop.trim();
			String forLoopCntrl = forLoop.substring(forLoop.indexOf('(') + 1, forLoop.indexOf(')'));

			//split by ';', then assign each to a string. append ';' to each since their respective parse methods require them
			String [] forArray = forLoopCntrl.split(";");
			String counterInit = forArray[0].trim() + ';';  //counter initialization, eg int i = 0

			String forCondition = forArray[1].trim();   //bool, ex: i < 10
			String inlineOp = forArray[2].trim() + ';';  //operator, ex i++

			//check if all three components of for loop parentheses are valid
			if(!isValidAssignmentStatement(counterInit))
				return false;
			else if(!isValidBoolExpression(forCondition))
				return false;
			else if(isValidOperation(inlineOp))
				return true;
		}
		return false;
	}

	/**
	 *
	 * todo Jon needs to pass in other simple statement methods
	 * @param simpleStatement
	 * @return True if simple statement is valid
	 * Jon
	 */
	public boolean isValidSimpleStatement(String simpleStatement) {

		//checking if valid print statement
		if (simpleStatement.matches("\\s*System\\.out\\.(println|printf|print)\\s*\\(\".+\"\\);")) {

			//extract code between parentheses
			int startIndex = simpleStatement.indexOf('(') + 1;
			int endIndex = simpleStatement.indexOf(')');

			//print statement should be a string literal enclosed by quotations
			String printBlock = simpleStatement.substring(startIndex, endIndex);
			Matcher quoteMatcher = Pattern.compile("\".*\"").matcher(printBlock);
			if (quoteMatcher.find()) {
				return true;
			}
		}

		//inline and block comments
		if(simpleStatement.matches("//.*")){
			return true;
		}

		if(simpleStatement.matches("/\\*.*\\*/")) {
			return true;
		}

		//check if statement is a valid assignment statement
		if(isValidAssignmentStatement(simpleStatement))
			return true;

		//check if it's a valid operation
		if(isValidOperation(simpleStatement))
			return true;

		//if no true conditions met
		return false;
	}

	/*
	* @param string to be checked for assignment statement
	* @return true if a valid assignment statement
	 */
	public boolean isValidAssignmentStatement(String assignmentStatement) {

		//initialize data type for adding to declaredVariables map
		DataType type = null;

		//if statement is a simple declaration return true, add variable and its type to map
		if (assignmentStatement.matches("^\\s*(int|double|boolean|char)\\s+[A-z$_][A-z0-9$_]*;$")) {

			//split declaration statement into type and var name, excluding semicolon
			String [] statementTokens = assignmentStatement.split(" ");
			String newVarType = statementTokens[0].trim();
			String newVar = statementTokens[1].trim().substring(0, statementTokens[1].indexOf(';'));

			//assign its data type and add to the map, then return true
			switch (newVarType) {
				case "int" -> type = DataType.INT;
				case "double" -> type = DataType.DOUBLE;
				case "boolean" -> type = DataType.BOOLEAN;
				case "char" -> type = DataType.CHAR;
			}

			//add variable to map, if successful return true
			if(addVariable(newVar, type))
				return true;

			return false;
		}

		//parsing for assignment, if an '=' is present in the string
		Matcher assignmentMatcher = Pattern.compile("^[^=]+\\s*=[^=]+;$").matcher(assignmentStatement);

		if (assignmentMatcher.find()) {

			//split by '=' into left and right sides of string, trim to remove any leading/trailing whitespace
			String[] arr = assignmentStatement.split("=");
			String leftOfEquals = arr[0].trim();
			String rightOfEquals = arr[1].substring(0, arr[1].length()).trim();

			String[] varParams = leftOfEquals.split(" ");

			/*if varParams.length is 2, then a declaration is happening along with assignment
			 *checks if the variable name is a valid variable name, otherwise will not pass
			 * if valid variable name, then it's added to the map
			 */
			if (varParams.length == 2 && varParams[1].matches("[A-z$_][A-z0-9$_]*")) {
				if (!declaredVariables.containsKey(varParams[1]) && !reservedKeywords.contains(varParams[1])) {
					DataType varType = null;
					switch (varParams[0]) {
						case "boolean":
							if (isValidBoolExpression(rightOfEquals.substring(0, rightOfEquals.length()-1)))
								varType = DataType.BOOLEAN;
							break;

						case "char":
							if (rightOfEquals.matches("^'[\\x00-\\x7F]';$") | isValidOperation(rightOfEquals))
								varType = DataType.CHAR;
							break;

						case "int":
							if (rightOfEquals.matches("^[0-9]+;$") | isValidOperation(rightOfEquals))
								varType = DataType.INT;
							break;

						case "double":
							if (rightOfEquals.matches("^([0-9]+\\.[0-9]+|[0-9]+);$") | isValidOperation(rightOfEquals))
								varType = DataType.DOUBLE;
							break;
					}
					addVariable(varParams[1], varType);
					return true;
				}
			}

			/*if varParams.length is 1, then check to see if the variable being assigned is in the map. if it is, grab
			  its type and check accordingly
			*/

			if(varParams.length == 1 && varParams[0].trim().matches("[A-z$_][A-z0-9$_]*")) {
				if((reservedKeywords.contains(varParams[0]))) {
					return false;
					//throw new ParserException(String.format("%s: variable name cannot be a reserved keyword, invalid assignment", varParams[0]));
				}

				if(declaredVariables.containsKey(varParams[0])) {
					DataType varType = declaredVariables.get(varParams[0]);

					switch(varType) {
						case BOOLEAN:
							if (isValidBoolExpression(rightOfEquals.substring(0, rightOfEquals.length()-1)))
								return true;

						case CHAR:
							if(rightOfEquals.matches("^'[\\x00-\\x7F]';$") | isValidOperation(rightOfEquals))
								return true;

						case INT:
							if(rightOfEquals.matches("^[0-9]+;$") | isValidOperation(rightOfEquals))
								return true;

						case DOUBLE:
							if(rightOfEquals.matches("^([0-9]+\\.[0-9]+|[0-9]+);$") | isValidOperation(rightOfEquals))
								return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 tests for valid operation, allows for +, -, *, /, %
	 allows for character arithmetic as well as combining number/char
	 */
	public boolean isValidOperation(String operation) {

		//checks for valid operation, allows character operations as well
		Matcher OpMatcher = Pattern.compile("^'?[\\x00-\\x7F]+'?\\s*[+\\-*/%]\\s*'?[\\x00-\\x7F]+'?;$").matcher(operation);
		if(OpMatcher.find())
			return true;

		//check if inline operation, eg 'i++'
		if(operation.matches("\\s*[A-z$_][A-z0-9$_]*\\s*(\\+\\+|--);$"))
			return true;

		//false if invalid operation
		return false;
	}

	/**
	 * Todo: Jon Returns true if the string passed is a valid method signature
	 * @param methodSignature
	 * @return True if the method signature is valid
	 */
	public boolean isValidMethodSignature(String methodSignature) {

		//basic structure for method signature
		if(methodSignature.matches("\\s*public\\s*static\\s*void\\s+[a-zA-z]+\\s*\\(\\s*\\)")){

			//get substring up to opening parentheses of method declaration, split it into tokens
			String subString = methodSignature.substring(0, methodSignature.indexOf("("));
			String[] arr = subString.split("\\s");

			//arr[3] should be the method name, if it is a reserved keyword throw exception
			if(reservedKeywords.contains(arr[3])) {
				throw new ParserException(String.format("%s is a reserved keyword.", arr[1]));
			}
			else return true;
		}
		return false;
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
		// if not valid IF, throw Exception
		// Check if it contains a valid bool
		if (ifBlock.matches("\\s*if\\s?\\(.+\\).*")) {
			// inc first index, exc last index
			// Check if it contains a valid boolean expression
			int startIndex = ifBlock.indexOf('(') + 1;
			int endIndex = ifBlock.indexOf(')');
			if (isValidBoolExpression(ifBlock.substring(startIndex, endIndex))) {
				// TODO Sam process rest of line after closing paren of if block, throw errors where appropriate
				return true;
			}
		}
		throw new ParserException(String.format("Invalid if statement: \"%s\".", ifBlock));
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
			throw new ParserException(String.format("Could not find a valid operator in the expression \"%s\".", boolExp));
		} else { // operators were found in boolExp, continue
			// Get index of operator
			int operatorIndex = matcher.end();
			
			// Separate operands into substrings
			String leftOperand = boolExp.substring(0, operatorIndex).replaceAll("(<=?|>=?|==|!=)", "").trim();
			String rightOperand = boolExp.substring(operatorIndex).replaceAll("(<=?|>=?|==|!=)", "").trim();
			
			// Cannot compare booleans
			if(getType(leftOperand) == DataType.BOOLEAN || getType(rightOperand) == DataType.BOOLEAN ) {
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
	 * Returns true if the parentheses or bracket passed to the method either:
	 * Successfully closes a set of parentheses or brackets. Is successfully added
	 * to the stack.
	 * 
	 * @param paren
	 * @return True if the parentheses is correctly placed.
	 */
	public boolean isValidParens(char paren) {
		// Check if opening or closing bracket
		// if Opening, add to stack, return true
		// if Closing, peek at stack to see if it matches the one at the top
		// if it does match, pop and return true
		// if it does not match, throw an exception
		return true;
	}

}
