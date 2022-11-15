package junits;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.DataType;
import main.Validator;

class AddVariableTests {
	
	Validator validator = new Validator("NoFileProvided");

	@Test
	void testValid() {
		assertTrue(validator.addVariable("i", DataType.INT));
		assertTrue(validator.addVariable("$i", DataType.INT));
		assertTrue(validator.addVariable("$", DataType.INT));
		assertTrue(validator.addVariable("$123", DataType.INT));
		assertTrue(validator.addVariable("$abc123", DataType.INT));
	}
	@Test
	void testInvalidStartingCharacter() {
		assertFalse(validator.addVariable("314", DataType.INT));
	}
	@Test
	void testInvalidCharacter() {
		assertFalse(validator.addVariable("pi3.14", DataType.INT));
	}
	@Test
	void testInvalidKeyword() {
		assertFalse(validator.addVariable("if", DataType.INT));
	}
	@Test
	void testInvalidBlank() {
		assertFalse(validator.addVariable("      ", DataType.INT));
	}
	@Test
	void testInvalidAlreadyExists() {
		validator.addVariable("alreadyDeclared", DataType.INT);
		assertFalse(validator.addVariable("alreadyDeclared", DataType.INT));
	}

}
