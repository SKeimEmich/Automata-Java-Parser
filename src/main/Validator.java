package main;
/**
 * Katie is just testing something...
 */

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exceptions.ParserException;
//todo: print variables in print statement, operations in print statement
public class Validator {
	private String code; // Shouldn't be touched outside of this class
	private ArrayList<String> reservedKeywords;
	private Map<String, DataType> declaredVariables;
	private boolean isValidated;
	private boolean isValid;
	String fileName;

	// Constructor
	public Validator() {
		getReservedKeywords();
		declaredVariables = new HashMap<>();
		isValidated = false;
		isValid = false; // Assume invalid
	}

	public Validator(String fileName) {
		this();
		setFileName(fileName);
	}

	// Setup Methods for the constructor to use
	
	/** Reads in File
	 * 
	 * 
	 */
	private void readFile(){
		// Construct a scanner object to read from file
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			String line = null;
			String separator = "\n";
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(separator);
			}
			// remove last line separator
			sb.deleteCharAt(sb.length() - 1);
			reader.close();
			code = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
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
	public void setFileName(String fileName) {
		this.fileName = fileName;
		readFile();
	}

	// Main Method to be run

	/**
	 * Tests if the file scanned by Scanner object contains valid Java code.
	 * 
	 * @param scan
	 */
	public void validate() {
		// check if the code has been scanned already, escape to prevent duplicate scans
		if (isValidated) {
			return;
		}

		// get indices of code block braces
		int indexOfOpeningBrace = code.indexOf("{");
		int indexOfClosingBrace = getPositionOfClosingBrace(code.substring(indexOfOpeningBrace)) + indexOfOpeningBrace;

		// if there is text after the closing brace, throw an error
		if (!code.substring(indexOfClosingBrace + 1).isBlank()) {
			throw new ParserException("Cannot have code outside of method code block.");
		}

		// if there is no opening brace, throw an error
		if (indexOfOpeningBrace == -1) {
			throw new ParserException("No code block exists.");
		}

		// Validate method signature
		String methodSignature = code.substring(0, indexOfOpeningBrace);
		isValidMethodSignature(methodSignature);

		// get code block (block belonging to method)
		String codeBlock = code.substring(indexOfOpeningBrace + 1, indexOfClosingBrace);
		isValidCodeBlock(codeBlock);

		// Assume that if the code has reached this point of execution, then it has
		// found no errors (Error causes early exiting)
		isValid = true;
		isValidated = true;
	}

	// Methods used to parse lines and blocks, alphabetical by Author last name

	/**
	 *author Jon
	 * @param forLoop, string to be parsed
	 * @return true if forLoop contains a valid loop declaration, throw ParserException if not
	 */
	public boolean isValidFor(String forLoop) {

		// Check for valid for loop declaration/control block
		Matcher forMatcher = Pattern.compile("^\\s*for\\(.*;.*;.*\\)[\\s\\S]*").matcher(forLoop);

		if (forMatcher.find()) {
			// trim whitespace, extract substring between parentheses
			forLoop.trim();
			String forLoopCntrl = forLoop.substring(forLoop.indexOf('(') + 1, forLoop.indexOf(')'));

			//split by ';', then assign each to a string. append ';' to each since their respective parse methods require them
			String [] forArray = forLoopCntrl.split(";");

			String counterInit = forArray[0].trim() + ';';  //counter initialization, eg int i = 0
			String forCondition = forArray[1].trim();   //bool, ex: i < 10
			String inlineOp = forArray[2].trim() + ';';  //operator, ex i++

			//check if all three components of for loop declaration are valid, if not exception is thrown w/ message
			if(!isValidAssignmentStatement(counterInit))
				throw new ParserException(String.format("%s : not a valid loop counter declaration/initialization", counterInit));
			if(!isValidBoolExpression(forCondition))
				throw new ParserException(String.format("%s : not a valid boolean loop control", forCondition));
			 if(!isValidOperation(inlineOp))
				throw new ParserException(String.format("%s : not a valid loop counter increment/decrement", inlineOp));

			 /*code below, up to 'return true' comes from sam's ifBlock method, with renaming of variables for this method*/
			// remove for(....) from forLoop
			String remainingFor = forLoop.substring(forLoop.indexOf(')') + 1).trim();
			// Get block of code if it exists
			if (remainingFor.charAt(0) == '{') {
				int indexOfClosingBrace = getPositionOfClosingBrace(remainingFor);
				if (indexOfClosingBrace > 0) {
					String codeBlock = remainingFor.substring(1, indexOfClosingBrace).trim();
					if (!isValidCodeBlock(codeBlock)) {
						throw new ParserException("I don't know how you got here, so congratulations on that.");
					}
				}
			} else {
				// Remaining code in block is assumed to be a simple statement
				if (!isValidSimpleStatement(remainingFor)) {
					throw new ParserException("I don't know how you got here, so congratulations on that.");
				}
			}
			return true;
		}
		//thrown if string did not match for loop declaration structure at all
		throw new ParserException(String.format("%s : not a valid for loop declaration", forLoop));
	}

	/**
	 * @param simpleStatement
	 *todo: break and continue
	 * @return True if simple statement is valid
	 * throws parserException if simple statement is not valid
	 * Jon
	 */
	public boolean isValidSimpleStatement(String simpleStatement) {

		// checking if valid print statement
		if (simpleStatement.matches("\\s*System\\.out\\.(println|print)\\s*\\(.+\\);")) {
			if(!isValidPrintStatement(simpleStatement))
				throw new ParserException(String.format("%s is not a valid print statement", simpleStatement));
			else return true;
		}

		//inline comment
		if(simpleStatement.matches("\\s*//[^\n]*\n")){
			return true;
		}

		//block comment
		if(simpleStatement.matches("\\s*/[*](.|\n)*[*]/")) {
			return true;
		}

		//check if statement is a valid variable declaration
		if(simpleStatement.matches("^\\s*(int|double|boolean|char)\\s+[A-z$_][A-z0-9$_]*;$")) {
			if(!isValidVariableDeclaration(simpleStatement))
				throw new ParserException(String.format("%s is not a valid variable declaration.", simpleStatement));
			else return true;
		}

		//check if valid variable initialization/assignment
		if(simpleStatement.matches("^[^=]+\\s*=[^=]+;$")) {
			if(!isValidAssignmentStatement(simpleStatement))
				throw new ParserException(String.format("%s is not a valid variable initialization/assignment", simpleStatement));
			else return true;
		}

		//check if valid inline operation
		if(simpleStatement.matches("\\s*[A-z$_][A-z0-9$_]*\\s*(\\+\\+|--);$")) {
			if (!isValidOperation(simpleStatement))
				throw new ParserException(String.format("%s is not a valid inline operation", simpleStatement));
			else return true;
		}

		//have to be implemented in a switch statement or loop
		if(simpleStatement.matches("\\s*break;") )
			return true;
		if(simpleStatement.matches("\\s*continue;"))
			return true;


		throw new ParserException(String.format("%s is not a valid simple statement", simpleStatement));
	}

	public boolean isValidPrintStatement(String printStatement) {

			//extract code between parentheses, will be 'printBlock'
			int startIndex = printStatement.indexOf('(') + 1;
			int endIndex = printStatement.indexOf(')');

			// print statement should be a string literal enclosed by quotations
			String printBlock = printStatement.substring(startIndex, endIndex);
			//for string literal
			Matcher quoteMatcher = Pattern.compile("^\".*\"$").matcher(printBlock);

			//for variable
			Matcher validVarMatcher = Pattern.compile("^\\w+$").matcher(printBlock);

			if (quoteMatcher.find()) {
				return true;
			}

			if(validVarMatcher.find()) {
				return declaredVariables.containsKey(printBlock);
			}

			//check if it's a valid operation
			if(printBlock.matches("^\\s*('\\w'|\\d+|\\w+|\\d*.\\d+)\\s*[+%/*-]\\s*('\\w'" +
				"|\\d+|\\w+|\\d*.\\d+)\\s*([+%/*-]\\s*('\\w'|\\d+|\\w+|\\d*.\\d+)\\s*)*$")) {
					printBlock += ';';

				return isValidOperation(printBlock);
			}

			else throw new ParserException(String.format("%s is not a valid print statement", printStatement));
	}

	/**
	 * isValidVariableDeclaration
	 * 
	 */
	public boolean isValidVariableDeclaration(String varDeclaration) {
		//initialize data type for adding to declaredVariables map
		DataType type = null;

		//if statement is a simple declaration return true, add variable and its type to map
		if (varDeclaration.matches("^\\s*(int|double|boolean|char)\\s+[A-z$_][A-z0-9$_]*;$")) {

			//split declaration statement into type and var name, excluding semicolon
			String [] statementTokens = varDeclaration.split(" ");
			String newVarType = statementTokens[0].trim();
			String newVar = statementTokens[1].trim().substring(0, statementTokens[1].indexOf(';'));

			// assign its data type and add to the map, then return true
			switch (newVarType) {
			case "int" -> type = DataType.INT;
			case "double" -> type = DataType.DOUBLE;
			case "boolean" -> type = DataType.BOOLEAN;
			case "char" -> type = DataType.CHAR;
			}

			//add variable to map, if successful return true
			return addVariable(newVar, type);
		}
		else return false;
	}

	
	/*
	* @param string to be checked for assignment statement
	* @return true if a valid assignment statement
	 */
	public boolean isValidAssignmentStatement(String assignmentStatement) {
		//parsing for assignment, if an '=' is present in the string

		if(assignmentStatement.matches("^[^=]+\\s*=[^=]+;$")){

			// split by '=' into left and right sides of string, trim to remove any
			// leading/trailing whitespace
			String[] arr = assignmentStatement.split("=");
			String leftOfEquals = arr[0].trim();
			String rightOfEquals = arr[1].substring(0, arr[1].length()).trim();

			String[] varParams = leftOfEquals.split(" ");

			/*
			 * if varParams.length is 2, then a declaration is happening along with
			 * assignment checks if the variable name is a valid variable name, otherwise
			 * will not pass if valid variable name, then it's added to the map
			 */
			if (varParams.length == 2 && varParams[1].matches("[A-z$_][A-z0-9$_]*")) {
				if (!declaredVariables.containsKey(varParams[1]) && !reservedKeywords.contains(varParams[1])) {
					DataType varType = null;
					switch (varParams[0]) {
					case "boolean":
						if (isValidBoolExpression(rightOfEquals.substring(0, rightOfEquals.length() - 1))) {
							varType = DataType.BOOLEAN;
							break;
						}
						else return false;

					case "char":
						if (rightOfEquals.matches("^'[\\x00-\\x7F]';$") | isValidOperation(rightOfEquals)) {
							varType = DataType.CHAR;
							break;
						}
						else return false;

					case "int":
						if (rightOfEquals.matches("^[0-9]+;$") | isValidOperation(rightOfEquals)) {
							varType = DataType.INT;
							break;
						}
						else return false;

					case "double":
						if (rightOfEquals.matches("^([0-9]+\\.[0-9]+|[0-9]+);$") | isValidOperation(rightOfEquals)) {
							varType = DataType.DOUBLE;
							break;
						}
						else return false;
					}
					//variable added to map if it was valid assignment
					addVariable(varParams[1], varType);
					return true;
				}
			}

			/*
			 * if varParams.length is 1, then check to see if the variable being assigned is
			 * in the map. if it is, grab its type and check accordingly
			 */

			if (varParams.length == 1 && varParams[0].trim().matches("[A-z$_][A-z0-9$_]*")) {
				if ((reservedKeywords.contains(varParams[0]))) {
					return false;
					// throw new ParserException(String.format("%s: variable name cannot be a
					// reserved keyword, invalid assignment", varParams[0]));
				}

				if (declaredVariables.containsKey(varParams[0])) {
					DataType varType = declaredVariables.get(varParams[0]);

					switch (varType) {
					case BOOLEAN:
						if (isValidBoolExpression(rightOfEquals.substring(0, rightOfEquals.length() - 1)))
							return true;

					case CHAR:
						if (rightOfEquals.matches("^'[\\x00-\\x7F]';$") | isValidOperation(rightOfEquals))
							return true;

					case INT:
						if (rightOfEquals.matches("^[0-9]+;$") | isValidOperation(rightOfEquals))
							return true;

					case DOUBLE:
						if (rightOfEquals.matches("^([0-9]+\\.[0-9]+|[0-9]+);$") | isValidOperation(rightOfEquals))
							return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * tests for valid operation, allows for +, -, *, /, % allows for character
	 * arithmetic as well as combining number/char
	 */
	public boolean isValidOperation(String operation) {

		String process = " ";
		if (operation.matches("^\\s*('\\w'|\\d+|\\w+|\\d*.\\d+)\\s*[+%/*-]\\s*('\\w'" +
				"|\\d+|\\w+|\\d*.\\d+)\\s*([+%/*-]\\s*('\\w'|\\d+|\\w+|\\d*.\\d+)\\s*)*;$")) {

			String[] opArray = operation.split("[+%/*;-]");

			for (String s : opArray) {
				process = s.trim();
				if (declaredVariables.containsKey(process) && getType(process) == DataType.BOOLEAN) {
					throw new ParserException(String.format("%s is a boolean variable, not valid operation!", process));
				}

				if ((process.matches("[^0-9]+")) && !declaredVariables.containsKey(process)) {
					if (!process.matches("'[^0-9]+'"))
						throw new ParserException(String.format("%s is not a valid declared variable!", process));
				}
			}
			return true;
		}

		// check if inline operation, eg 'i++;'
		if (operation.matches("\\s*[A-z$_][A-z0-9$_]*\\s*(\\+\\+|--);")) {

			String varCheck = operation.substring(0, operation.length() - 3).trim();
			if(declaredVariables.containsKey(varCheck) && getType(varCheck) == DataType.INT)
				return true;

			else throw new ParserException(String.format("%s is not a valid post increment or decrement!", operation));
		}
		return false;
	}

	/**
	 * Todo: Jon Returns true if the string passed is a valid method signature
	 * 
	 * @param methodSignature
	 * @return True if the method signature is valid
	 */
	public boolean isValidMethodSignature(String methodSignature) {

		// basic structure for method signature
		if (methodSignature.matches("\\s*((public|private|protected)\\s+)?\\s*static\\s+void\\s+[a-zA-z]+\\s*\\(\\s*\\)\\s*")) {

			// get substring up to opening parentheses of method declaration, split it into
			// tokens
			String subString = methodSignature.substring(methodSignature.indexOf("void"), methodSignature.indexOf("("));
			String[] arr = subString.split("\\s");

			// arr[1] should be the method name, if it is a reserved keyword throw exception
			if (reservedKeywords.contains(arr[1])) {
				throw new ParserException(String.format("%s is a reserved keyword.", arr[1]));
			} else
				return true;
		}
		throw new ParserException(String.format("%s is not a valid method declaration", methodSignature));
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
		if (!ifBlock.matches("\\A\\s*if\\s?\\([\\s\\S]+\\)[\\s\\S]*")) {
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
				String codeBlock = remainingIf.substring(1, indexOfClosingBrace - 1).trim();
				if (!isValidCodeBlock(codeBlock)) {
					throw new ParserException("I don't know how you got here, so congratulations on that.");
				}
			}
		} else {
			// Remaining code in block is assumed to be a simple statement
			if (!isValidSimpleStatement(remainingIf)) {
				throw new ParserException("I don't know how you got here, so congratulations on that.");
			}
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
	 * @author Katie Tracy 
	 * @param switchBlock
	 * @return True if Switch statement is valid
	 */
	public boolean isValidSwitch(String switchBlock) {
		// Check types in block, do they all match?
		String switchPattern = "\\A\\s*switch\\s?\\([\\s\\S]+\\)\\s?\\{[case [\\s\\S]*:[[\\s\\S]*[break;]?]]*[default:[\\s\\S]?]}";
		// is the syntax correct?
		if (!switchBlock.matches(switchPattern)) {
			throw new ParserException(String.format("Invalid switch statement: \"%s\".", switchBlock));
		}
		if (switchBlock.matches(switchPattern)) {
			//check if valid condition - data type - char or int
			String remainingSwitch = switchBlock;
			int start = switchBlock.indexOf('(') + 1; //Get the index of the paren for switch condish
			int end = switchBlock.indexOf(')'); //Get the index of the closing paren for switch condish
			String switchCondish = switchBlock.substring(start, end);
			//Check if condition variable is in declaredVariables Map
			if (!declaredVariables.containsKey(switchCondish)) {
				throw new ParserException(String.format("Condition has not been declared previously: \"%s\".", switchCondish));
			}
			if (declaredVariables.containsKey(switchCondish)) {
				DataType type = declaredVariables.get(switchCondish); //get data type
				//System.out.println("condish type: " + type);
				if (type != DataType.INT && type != DataType.CHAR) {
					throw new ParserException(String.format("Not a valid Data Type: \"%s\".", switchCondish));
				}
			}
			
			//Check if case data type matches DataType of switch condition
			remainingSwitch = remainingSwitch.substring(end+1, switchBlock.length());
			
			String checkRemainingSwitch = remainingSwitch;
			
			int caseCount = 0;
			String casePatt = "case";
			//get number of cases 
			for (int i = 0; i < checkRemainingSwitch.length(); i++) {
				//System.out.println(checkRemainingSwitch);
				if (checkRemainingSwitch.contains(casePatt)) {
					int index = checkRemainingSwitch.indexOf(casePatt);
					
					caseCount++;
					checkRemainingSwitch = checkRemainingSwitch.substring(index+4, checkRemainingSwitch.length()).trim();
					
				}
				
			}
			
			//For each of the cases, check if their data type matches the switch condition data type
			for (int i = 0; i < caseCount; i++) {
				
				start = remainingSwitch.indexOf("case:");
				
				start = remainingSwitch.indexOf('e')+1;
				end = remainingSwitch.indexOf(':');
				
				String switchCase = remainingSwitch.substring(start, end).trim();
				
				//otherwise, we check the conditions of the remaining "case" cases
				DataType caseType = getType(switchCase);
				
				if (declaredVariables.get(switchCondish)!=caseType) {
					throw new ParserException(String.format("Condition and case type do not match: \"%s\".", remainingSwitch));
				}
				
				
				int newEnd = remainingSwitch.indexOf("break;") + 6;
				
				if (newEnd < remainingSwitch.length()) {
					remainingSwitch = remainingSwitch.substring(newEnd, remainingSwitch.length()).trim();
				}
				else {
					throw new ParserException(String.format("Reached the end of the switch statement without checking all cases @ \"%s\".", remainingSwitch));
				}
			
			
			}
			
		}
		
		
		return true;
	}

	/**
	 * Returns true if the string passed is a valid While Loop TODO Katie
	 * @author Katie Tracy 
	 * @param whileLoop
	 * @return True if while loop is valid
	 */
	public boolean isValidWhile(String whileLoop) {
		
		String whilePatt = "\\A\\s*while\\s?\\([\\s\\S]+\\)\\s*\\{[\\s\\S]*\\}";
		
		//check if the While statement does not match the pattern while()
		if (!whileLoop.matches(whilePatt)) {
				throw new ParserException(String.format("Invalid while statement: \"%s\".", whileLoop));
		}
		
		//proceed with check if while statement matches the pattern while()
		if (whileLoop.matches(whilePatt)) {
		// Check if it contains a valid boolean expression
		int startIndex = whileLoop.indexOf('(') + 1;
		int endIndex = whileLoop.indexOf(')');
		
		//System.out.println(whileLoop.substring(startIndex, endIndex));
		if (!isValidBoolExpression(whileLoop.substring(startIndex, endIndex))) {
			throw new ParserException(String.format("Invalid while condition - must be boolean: \"%s\".", whileLoop));
		}
		
		// Katie process rest of line after closing paren of while block, throw errors where appropriate 
		/*code below, up to 'return true' comes from sam's ifBlock method, with renaming of variables for this method*/
		// remove while(....) from whileLoop
		String remainingWhile = whileLoop.substring(whileLoop.indexOf(')') + 1).trim();
		
		// Get block of code if it exists and check if valid code block
		if (remainingWhile.charAt(0) == '{') {
			//added start and end checks by Katie, edited from Sam's code for if 
			int indexOfClosingBrace = getPositionOfClosingBrace(remainingWhile);
			
			int start = 1;
			int end = indexOfClosingBrace - 1;
			int length = end - start;
			if (length > 2) { //valid code block
				String codeBlock = remainingWhile.substring(start, end+1).trim();
				
				if(!isValidCodeBlock(codeBlock)) {
					throw new ParserException("I don't know how you got here, so congratulations on that.");
				}
				if(!isValidSimpleStatement(codeBlock)) {
					throw new ParserException("I don't know how you got here, so congratulations on that.");
				}
			}
			else {
				
				return true;
			}

		} else {
			// Remaining code in block is assumed to be a simple statement
			if (!isValidSimpleStatement(remainingWhile)) {
				throw new ParserException("I don't know how you got here, so congratulations on that.");
			}
		}
		return true;
		}	
		//thrown if string did not match while loop declaration structure at all
		throw new ParserException(String.format("%s : not a valid while loop declaration", whileLoop));

	}
	
	/**
	 * Returns true if the string passed is a valid While Loop
	 * TODO Katie Tracy 
	 * @param doWhileLoop
	 * @return True if doWhileLoop is valid
	 */

	public boolean isValidDoWhile(String doWhileLoop) {
		//Author Katie
		String doWhilePattern = "\\A\\s*do\\s?\\{[\\s\\S]*\\}\\s?while\\s?\\([\\s\\S]+\\);";
		if (doWhileLoop.matches(doWhilePattern)){
			// inc first index, exc last index
		
			// TODO Katie process rest of line after closing paren of do block, throw errorswhere appropriate
			// TODO will change when we have a isValidCodeBlock method
			int indexOfOpeningBrace = doWhileLoop.indexOf('{');

			int indexOfClosingBrace = getPositionOfClosingBrace(doWhileLoop.substring(indexOfOpeningBrace));
			
			int start = indexOfOpeningBrace+1;
	
			int end = indexOfClosingBrace+2;
			
			int length = end - start;
			if (length > 2) { //valid code block
				String codeBlock = doWhileLoop.substring(start, end).trim();
				if(!isValidCodeBlock(codeBlock)) {
					throw new ParserException("I don't know how you got here, so congratulations on that.");
				}
			}
			else {
				// Check if it contains a valid boolean expression in while condition
				String remainingDoWhile = doWhileLoop.substring(indexOfClosingBrace+3, doWhileLoop.length()-1).trim();
				
				
				int openParens = remainingDoWhile.indexOf('(') + 1;
				
				int endParens = remainingDoWhile.indexOf(')');
				
				if (!isValidBoolExpression(remainingDoWhile.substring(openParens, endParens))) {
					throw new ParserException(String.format("Invalid do-while condition: \"%s\".", remainingDoWhile));
				}
				return true;
			}
		}
			return true;
		
	}

	/*
	 * Returns true if the string passed is a valid code block.
	 *
	 * @param String codeBlock
	 * 
	 * @return True if the code block is valid.
	 */
	public boolean isValidCodeBlock(String codeBlock) {
		// Author Sam
		codeBlock = codeBlock.trim(); // white space removal
		String remainingCodeBlock = ""; // setup var for remaining code to process
		String statementToCheck = "";

		// BASE CASE
		if(codeBlock.isBlank()) {
			return true;
		}
		
		// Construct matchers to check if is a complex statement...
		// ... with a simple statement
		Matcher withSimpleMatcher = Pattern
				.compile("\\A(\\s*(while|switch|if|for)\\s*\\([\\w\\s\\=\\;\\>\\<\\+\\-\\$]*\\)|\\s*do\\s+)")
				.matcher(codeBlock);
		// ... with a code block
		Matcher withCodeBlockMatcher = Pattern
				.compile("\\A(\\s*(while|switch|if|for)\\s*\\([\\w\\s\\=\\;\\>\\<\\+\\-\\$]*\\)\\s*\\{|\\s*do\\s+\\{)")
				.matcher(codeBlock);

		if (withCodeBlockMatcher.find()) { // check if we have a complex statement containing a code block
			// We found a match to a reserved keyword for a complex statement that we can process
			// get end of statement
			// find closing curlybrace
			int indexOfOpeningBrace = codeBlock.indexOf('{');
			int endOfStatement = getPositionOfClosingBrace(codeBlock.substring(codeBlock.indexOf('{'))) + indexOfOpeningBrace;
			// if it is a do-while, get the while at the end
			if(codeBlock.startsWith("do")) {
				// starting at the index of the closing curly brace
				// search for the next ;
				// update closing index to this index
				endOfStatement = codeBlock.indexOf(';', endOfStatement);
			}
			// get substring
			statementToCheck = codeBlock.substring(0, endOfStatement + 1);
			// pass to isComplexStatement
			if (!isValidComplexStatement(statementToCheck)) {
				throw new ParserException("I don't know how you got here, so congratulations on that.");
			}
			
			// construct remainingCodeBlock
			remainingCodeBlock = codeBlock.substring(endOfStatement + 1);
		} else if(withSimpleMatcher.find()) { // we have a complex statement containing a simple statement
			int endOfStatement;
			
			// check if it's a do-while
			if(codeBlock.startsWith("do")) {
				// there is a nested ; that we need to ignore
				// the code block ends with the second ;
				int indexOfFirstSemi = codeBlock.indexOf(';');
				endOfStatement = codeBlock.indexOf(';', indexOfFirstSemi + 1);
			} else {
				// find ; at end of simple statement contained in this complex statement
				endOfStatement = codeBlock.indexOf(';');
			}
			statementToCheck = codeBlock.substring(0, endOfStatement + 1);
			
			// pass to isValidComplexStatement
			if (!isValidComplexStatement(statementToCheck)) {
				throw new ParserException("I don't know how you got here, so congratulations on that.");
			}
			// construct remaining code block
			remainingCodeBlock = codeBlock.substring(endOfStatement + 1);
		} else {
			// The next part of the codeblock to process is not a complex statement, assume it is a simple statement
			if(codeBlock.substring(0,2).equals("/*")) { // Check if it's a block comment
				statementToCheck = codeBlock.substring(0, codeBlock.indexOf("*/") + 2).trim();
				remainingCodeBlock = codeBlock.substring(codeBlock.indexOf("*/") + 2);
			}
			else if (codeBlock.charAt(0) == '/') { // Check if it's an inline comment
				//condition for when a comment is the last statement in a code block, because the newline is trimmed
				if(codeBlock.indexOf('\n') == -1) {
					statementToCheck = codeBlock + "\n"; //append a newline to check its validity as a comment
					remainingCodeBlock = "";
				}
				else {
					statementToCheck = codeBlock.substring(0, codeBlock.indexOf('\n') + 1);
					remainingCodeBlock = codeBlock.substring(codeBlock.indexOf('\n') + 1);
				}
			}
			else { // It's not a block or an in line comment, cut out the simple statement and parse
				statementToCheck = codeBlock.substring(0, codeBlock.indexOf(';') + 1).trim();
				remainingCodeBlock = codeBlock.substring(codeBlock.indexOf(';') + 1);
			}
						
			if (!isValidSimpleStatement(statementToCheck)) {
				throw new ParserException("I don't know how you got here, so congratulations on that.");
			}
		}

		// recurse with remaining code block
		return true && isValidCodeBlock(remainingCodeBlock);
	}

	public boolean isValidComplexStatement(String statement) {
		// do while switch if for
		if(statement.startsWith("do")) {
			return isValidDoWhile(statement);
		} else if (statement.startsWith("while")){
			return isValidWhile(statement);
		} else if (statement.startsWith("switch")){
			return isValidSwitch(statement);
		} else if (statement.startsWith("if")){
			return isValidIf(statement);
		} else if (statement.startsWith("for")){
			return isValidFor(statement);
		} 
		throw new ParserException("Invalid complex statement, error unknown.");
	}
	
	

}
