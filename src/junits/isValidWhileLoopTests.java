package junits;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;
import main.DataType;
import main.Validator;

public class isValidWhileLoopTests {
	Validator validator = new Validator();

	
	 @Test 
	 void testWhileLoop() { 
		 validator.addVariable("n", DataType.INT); 
		 assertTrue(validator.isValidWhile("while(n < 3){}")); //
	  assertTrue(validator.isValidWhile("while(true){}")); //
	  assertTrue(validator.isValidWhile("while(true){System.out.print(\"Hi\");}"));} 

	 @Test // 
	 void testInvalidWhileLoop() { 
	  validator.addVariable("count", DataType.INT); 
	  assertThrows(ParserException.class, () -> validator.isValidWhile("whole(true){}")); 
	  assertThrows(ParserException.class, () -> validator.isValidWhile("while(count+3){}")); 
	  assertThrows(ParserException.class, () -> validator.isValidWhile("while(true){\"hi\"}")); 
	  }
	 	
	@Test
	void testdoWhileLoop( ) {
		validator.addVariable("count", DataType.INT);
		assertTrue(validator.isValidDoWhile("do{System.out.print(\"testing...\");}while(true);"));
		assertTrue(validator.isValidDoWhile("do{ }while(count < 5);"));
		
	}
	
	

	
}
