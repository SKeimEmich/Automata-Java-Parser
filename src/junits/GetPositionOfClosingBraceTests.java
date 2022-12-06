package junits;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exceptions.ParserException;
import main.Validator;

class GetPositionOfClosingBraceTests {

	Validator validator = new Validator("NoFileProvided");
	
	@Test
	void testValidCurly() {
		assertEquals(28, validator.getPositionOfClosingBrace("{sdf[(sdf[[{{sdf}}]sfd])sdf]}sdfdsfsdf"));
		assertEquals(33, validator.getPositionOfClosingBrace("     {sdf[(sdf[[     {{sdf}}]sfd])sdf]}sdfdsfsdf"));
		assertEquals(24, validator.getPositionOfClosingBrace("{sdf[(sdf[[{{}}]])sdf]		}sdfdsfsdf"));
		assertEquals(30, validator.getPositionOfClosingBrace("{sdf[(sdf[[{{sdf}}]sfd])sdf\n\n]}sdfdsfsdf"));
	}
//	
//	@Test
//	void testValidParen() {
//		assertTrue(validator.isValidBoolExpression("0 > 2"));
//		assertTrue(validator.isValidBoolExpression("4<=4"));
//		assertTrue(validator.isValidBoolExpression("9>=     8"));		
//		assertTrue(validator.isValidBoolExpression("4!='c'"));	
//	}
//	
//
//	@Test
//	void testValidSquare() {
//		assertTrue(validator.isValidBoolExpression("0 > 2 || true"));
//		assertTrue(validator.isValidBoolExpression("4<=4&false"));
//		assertTrue(validator.isValidBoolExpression("9>=     8 && 9 > 29"));		
//		assertTrue(validator.isValidBoolExpression("4!='c' || 7>   'c'"));
//	}
//	
//	
//	
//	@Test
//	void testInvalidBraces() {
//		testInvalid("true ||","Invalid data type, data was blank.");
//		testInvalid("true ||        fal","Invalid data type: fal.");
//		testInvalid("true false || false","Invalid data type: true false.");
//	}
//	
//	void testInvalid(String data, String expected) {
//		Exception e = assertThrows(ParserException.class, () -> {
//			validator.isValidBoolExpression(data);
//		});
//		String actual = e.getMessage();
//		assertTrue(actual.contains(expected));
//	}
}
