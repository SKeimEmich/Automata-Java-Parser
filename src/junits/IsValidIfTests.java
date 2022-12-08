package junits;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import exceptions.ParserException;
import main.Validator;

class IsValidIfTests {

	Validator validator = new Validator("NoFileProvided");

	@Test
	void testValid() {
		assertTrue(validator.isValidIf("if(true) {i = 2;}"));
		assertTrue(validator.isValidIf("if(true) i = 2;"));
		assertTrue(validator.isValidIf("if(true) i = 2;"));
		assertTrue(validator.isValidIf("if(true) 	{i = 2;}"));
		assertTrue(validator.isValidIf("if (true) i = 4;"));
		assertTrue(validator.isValidIf("	if(true) i = 4;"));
		assertTrue(validator.isValidIf("\nif(true || false) i = 4;"));
		assertTrue(validator.isValidIf("if(true)	    i = 4;"));
	}

	@Test
	void testInvalidOpeningBrace() {
		String ifStatement = "iftrue)";
		String expected = String.format("Invalid if statement: \"%s\".", ifStatement);
		testInvalid(ifStatement, expected);
	}

	@Test
	void testInvalidBraces() {
		String ifStatement = "if{true})";
		String expected = String.format("Invalid if statement: \"%s\".", ifStatement);
		testInvalid(ifStatement, expected);
	}

	@Test
	void testInvalidClosingBrace() {
		String ifStatement = "if(true";
		String expected = String.format("Invalid if statement: \"%s\".", ifStatement);
		testInvalid(ifStatement, expected);
	}
	
	@Test
	void testInvalidMissingCode() {
		String ifStatement = "if(true)";
		String expected = String.format("Invalid if statement %s, expected statement at end.", ifStatement);
		testInvalid(ifStatement, expected);
	}
	
	@Test
	void testInvalidDoesNotStartWithIf() {
		String ifStatement = "invalid\nif(true)";
		String expected = String.format("Invalid if statement: \"%s\".", ifStatement);
		testInvalid(ifStatement, expected);
	}

	
	void testInvalid(String ifStatement, String expected) {

		Exception e = assertThrows(ParserException.class, () -> {
			validator.isValidIf(ifStatement);
		});
		String actual = e.getMessage();
		assertTrue(actual.contains(expected));
	}

}
