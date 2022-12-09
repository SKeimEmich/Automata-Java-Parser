package main;
/**
 * Parser Project for Eastern Michigan University
 * COSC 444/541 Foundations of Automata and Languages
 * Fall 2022
 * @authors (alphabetical by last name) Jonathan Espinosa, Sam Keim, Katie Tracy
 *
 */

public class RunMe {
	
	public static void main(String[] args) {
		// Run with file provided by args, if available
		// If no file provided, ask user for filename
		// Check if file contains valid code

		Validator validator = new Validator("./Automata-Java-Parser/Code.txt");
		validator.validate();
		if(validator.isValid()) {
			// Print result
			System.out.println("The code provided in this file is valid.\nThank you for using our parser.");
		}
	}

}