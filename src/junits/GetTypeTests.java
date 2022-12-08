package junits;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import exceptions.ParserException;
import main.DataType;
import main.Validator;

class GetTypeTests {

	Validator validator = new Validator("NoFileProvided");

	@Test
	void testInt() {
		String data = "123";
		assertEquals(DataType.INT, validator.getType(data));
	}

	@Test
	void testIntNegative() {
		String data = "-123";
		assertEquals(DataType.INT, validator.getType(data));
	}

	@Test
	void testDouble() {
		String data = "3.14";
		assertEquals(DataType.DOUBLE, validator.getType(data));
	}

	@Test
	void testDoubleNegative() {
		String data = "-3.14";
		assertEquals(DataType.DOUBLE, validator.getType(data));
	}

	@Test
	void testBoolean() {
		String data = "true";
		assertEquals(DataType.BOOLEAN, validator.getType(data));
	}

	@Test
	void testChar() {
		String data = "\'a\'";
		assertEquals(DataType.CHAR, validator.getType(data));
	}

	@Test
	void testDeclared() {
		String variableName = "pi";
		validator.addVariable(variableName, DataType.DOUBLE);
		assertEquals(DataType.DOUBLE, validator.getType(variableName));
	}

	@Test
	void testInvalidData() {
		String data = "TRUE3.14";
		testInvalid(data, String.format("Invalid data type: %s.", data));
	}

	@Test
	void testEmpty() {
		testInvalid("   \n", "Invalid data type, data was blank.");
	}

	void testInvalid(String data, String expected) {
		Exception e = assertThrows(ParserException.class, () -> {
			validator.getType(data);
		});
		String actual = e.getMessage();
		assertTrue(actual.contains(expected));
	}

}
