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
		assertTrue(validator.isValidIf("if (true)"));
		assertTrue(validator.isValidIf("	if(true)"));
		assertTrue(validator.isValidIf("if(true || false)"));
		assertTrue(validator.isValidIf("if(true)	   "));

	}

	@Test
	void testInvalidOpeningBrace() {
		testInvalid("iftrue)");
	}

	@Test
	void testInvalidBraces() {
		testInvalid("if{true})");
	}

	@Test
	void testInvalidClosingBrace() {
		testInvalid("if(true");
	}

	void testInvalid(String ifStatement) {
		Exception e = assertThrows(ParserException.class, () -> {
			validator.isValidIf(ifStatement);
		});
		String expected = String.format("Invalid if statement: \"%s\".", ifStatement);
		String actual = e.getMessage();
		assertTrue(actual.contains(expected));
	}

}
