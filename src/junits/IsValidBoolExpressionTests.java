package junits;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import exceptions.ParserException;
import main.Validator;

class IsValidBoolExpressionTests {

	Validator validator = new Validator("NoFileProvided");
	
	@Test
	void testValidAndOrOperators() {
		assertTrue(validator.isValidBoolExpression("true"));
		assertTrue(validator.isValidBoolExpression("true ||        false"));
		assertTrue(validator.isValidBoolExpression("true && false | false"));		
		assertTrue(validator.isValidBoolExpression("true&false"));		
		assertTrue(validator.isValidBoolExpression("true||false"));		
	}
	
	@Test
	void testValidOperators() {
		assertTrue(validator.isValidBoolExpression("0 > 2"));
		assertTrue(validator.isValidBoolExpression("4<=4"));
		assertTrue(validator.isValidBoolExpression("9>=     8"));		
		assertTrue(validator.isValidBoolExpression("4!='c'"));	
	}
	

	@Test
	void testValidMultipleOperators() {
		assertTrue(validator.isValidBoolExpression("0 > 2 || true"));
		assertTrue(validator.isValidBoolExpression("4<=4&false"));
		assertTrue(validator.isValidBoolExpression("9>=     8 && 9 > 29"));		
		assertTrue(validator.isValidBoolExpression("4!='c' || 7>   'c'"));
	}
	
	
	
	@Test
	void testInvalidAndOrOperators() {
		testInvalid("true ||","Invalid data type, data was blank.");
		testInvalid("true ||        fal","Invalid data type: fal.");
		testInvalid("true false || false","Invalid data type: true false.");
	}

	@Test
	void testInvalidOperators() {
		testInvalid("0 => 2","Invalid data type: 0 =.");
		testInvalid("<=4","Invalid data type, data was blank.");
		testInvalid("9=     8","Invalid data type: 9=     8.");		
		testInvalid("true!='c'","Error, cannot compare booleans.");
	}
	
	void testInvalid(String data, String expected) {
		Exception e = assertThrows(ParserException.class, () -> {
			validator.isValidBoolExpression(data);
		});
		String actual = e.getMessage();
		assertTrue(actual.contains(expected));
	}
}
