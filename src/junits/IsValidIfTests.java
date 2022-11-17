package junits;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import main.Validator;

class IsValidIfTests {
	
	Validator validator = new Validator("NoFileProvided");

	@Test
	void testValid() {
		assertTrue(validator.isValidIf("if(true)"));
		assertTrue(validator.isValidIf("if (true)"));
		assertTrue(validator.isValidIf("	if(true)"));
//		assertTrue(validator.isValidIf("if(true || false)"));
		assertTrue(validator.isValidIf("if(true)	   "));

	}
//	@Test
//	void testInvalid() {
//		assertFalse(validator.isValidIf("iftrue)"));
//		assertFalse(validator.isValidIf("if{true}"));
//		assertFalse(validator.isValidIf("if(true"));
//	}

}
