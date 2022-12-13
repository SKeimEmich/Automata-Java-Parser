package junits;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;
import main.DataType;
import main.Validator;

public class isValidSwitchTests {
	Validator validator = new Validator();

	@Test
	void testSwitchStatement() {
		validator.addVariable("month", DataType.INT);
		validator.addVariable("vowel", DataType.CHAR);
		assertTrue(validator.isValidSwitch("switch (month){"
				+ "case 1: System.out.print(\"January\"); break;"
				+ "case 2: System.out.print(\"February\"); break;"
				+ "default: System.out.print(\"This is not a valid month\");}"));
		assertTrue(validator.isValidSwitch("switch (vowel){"
				+ "case 'a': System.out.print(\"January\"); break;"
				+ "case 'e': System.out.print(\"February\"); break;"
				+ "default: System.out.print(\"This is not a valid month\");}"));	
		assertTrue(validator.isValidSwitch("switch (vowel){"
						+ "case 'i': System.out.print(\"January\"); break;"
						+ "case 'o': System.out.print(\"February\"); break;"
						+ "default: System.out.print(\"This is not a valid month\");}"));
		assertTrue(validator.isValidSwitch("switch (month){"
				+ "case 1: System.out.print(\"January\"); break;"
				+ "case 2: System.out.print(\"February\"); break;}"));
		assertTrue(validator.isValidSwitch("switch (month){"
				+ "case 1: System.out.print(\"January\"); break;"
				+ "case 2: System.out.print(\"February\"); break;}"));
	
	}
	
	
	  void testInvalidSwitch() { validator.addVariable("month", DataType.INT);
	  assertThrows(ParserException.class, () ->
	  validator.isValidSwitch("switch (letter){" +
	  "case 1: System.out.print(\"January\"); break;" +
	  "case 2: System.out.print(\"February\"); break;" +
	  "default: System.out.print(\"This is not a valid month\");}"));
	  assertThrows(ParserException.class, () ->
	  validator.isValidSwitch("switch (month){" +
	  "case 1: System.out.print(\"January\") break;" +
	  "case \"two\": System.out.print(\"February\")"));
	  assertThrows(ParserException.class, ()
	  ->validator.isValidSwitch("switch (month){" +
	  "case 1: System.out.print(\"January\") break;" +
	  "case 2: System.out.print(\"February\");")); }
	 
}
