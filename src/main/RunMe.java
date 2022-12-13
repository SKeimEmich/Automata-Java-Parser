package main;

import java.io.File;
import java.util.Scanner;

/**
 * Parser Project for Eastern Michigan University
 * COSC 444/541 Foundations of Automata and Languages
 * Fall 2022
 * @authors (alphabetical by last name) Jonathan Espinosa, Sam Keim, Katie Tracy
 *
 */

public class RunMe {
	
	public static void main(String[] args) {
		Scanner scn = new Scanner(System.in);
		String fileName, userInput;
		boolean isContinue = true;

		System.out.println("Welcome to The Tigers' Java Parser program.");
		System.out.println("Authors:");
		System.out.println("\tKatie Tracy\n\tSam Keim\n\tJonathan Espinosa\n");

		do {
			System.out.print("Enter a file name or ENTER to run default file or Q to quit: ");
			userInput = scn.nextLine().trim();
			if(userInput.equalsIgnoreCase("Q")) {
				isContinue = false;
				break;
			} else if(userInput.isBlank()) {
				fileName = "code.txt";
			} else {
				fileName = userInput;
				File f = new File(fileName);
				if(!f.exists() || f.isDirectory()) {
					System.out.println("File cannot be found, please try again.");
					continue;
				}
			}
			Validator validator = new Validator(fileName);
			validator.validate();
			if(validator.isValid()) {
				// Print result
				System.out.printf("The code provided in %s is valid.%n", fileName);
			}
		} while(isContinue);
		scn.close();
		System.out.println("Thank you for using our parser!");
	}

}