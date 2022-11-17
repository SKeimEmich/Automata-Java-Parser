package junits;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

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
		assertFalse(validator.isValidBoolExpression("true ||"));
		assertFalse(validator.isValidBoolExpression("true ||        fal"));
		assertFalse(validator.isValidBoolExpression("true false || false"));		
		
	}

	@Test
	void testInvalidOperators() {
		assertFalse(validator.isValidBoolExpression("0 => 2"));
		assertFalse(validator.isValidBoolExpression("<=4"));
		assertFalse(validator.isValidBoolExpression("9=     8"));		
		assertFalse(validator.isValidBoolExpression("true!='c'"));	
		
	}

}
