package junits;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import main.DataType;
import main.Validator;

class GetTypeTests {

	Validator validator = new Validator("NoFileProvided");

	@Test
	void testEmpty() {
		String data = "   ";
		assertEquals(null, validator.getType(data));
	}
	
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
	void testInvalid() {
		String data = "TRUE3.14";
		assertEquals(null, validator.getType(data));
	}

	@Test
	void testDeclared() {
		String variableName = "pi";
		validator.addVariable(variableName, DataType.DOUBLE);
		assertEquals(DataType.DOUBLE, validator.getType(variableName));
	}

}
