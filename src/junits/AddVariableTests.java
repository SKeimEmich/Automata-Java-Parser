package junits;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;
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
		String variableName = "314";
		DataType type = DataType.INT;
		String expected = String.format("Unknown error adding variable (%s, %s).", variableName, type);
		testInvalid(variableName, type, expected);
	}

	@Test
	void testInvalidCharacter() {
		String variableName = "pi3.14";
		DataType type = DataType.INT;
		String expected = String.format("Unknown error adding variable (%s, %s).", variableName, type);
		testInvalid(variableName, type, expected);
	}

	@Test
	void testInvalidKeyword() {
		String variableName = "if";
		DataType type = DataType.INT;
		String expected = String.format("%s is a reserved keyword.", variableName);
		testInvalid(variableName, type, expected);
	}

	@Test
	void testInvalidBlank() {
		String variableName = "      ";
		DataType type = DataType.INT;
		String expected = String.format("Unknown error adding variable (%s, %s).", variableName, type);
		testInvalid(variableName, type, expected);
	}

	@Test
	void testInvalidAlreadyExists() {
		String variableName = "alreadyDeclared";
		DataType type = DataType.INT;
		validator.addVariable(variableName, type);
		String expected = String.format("Variable %s was declared previously.", variableName);
		testInvalid(variableName, type, expected);
	}

	void testInvalid(String variableName, DataType type, String expected) {
		Exception e = assertThrows(ParserException.class, () -> {
			validator.addVariable(variableName, type);
		});
		String actual = e.getMessage();
		assertTrue(actual.contains(expected));
	}

}
