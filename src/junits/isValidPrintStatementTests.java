package junits;

import main.DataType;
import main.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class isValidPrintStatementTests {
    Validator validator = new Validator();


 @Test
 void testStringLiteralPrint() {
     assertTrue(validator.isValidPrintStatement("System.out.print(\"hello\");"));
 }

 @Test
 void testValidOperationPrint() {
     validator.addVariable("testVar", DataType.INT);
     validator.addVariable("testDouble", DataType.DOUBLE);
     assertTrue(validator.isValidPrintStatement("System.out.print(2 + 5);"));
     assertTrue(validator.isValidPrintStatement("System.out.print(testVar * 5 - 4 / testVar);"));
     assertTrue(validator.isValidPrintStatement("System.out.print(testVar * 5 - 4 / testVar + testDouble);"));
 }

 @Test
    void testdeclaredVariablePrint() {
     validator.addVariable("test", DataType.BOOLEAN);
     validator.addVariable("testInt", DataType.INT);
     validator.addVariable("testDouble", DataType.DOUBLE);
     assertTrue(validator.isValidPrintStatement("System.out.print(test);"));
     assertTrue(validator.isValidPrintStatement("System.out.print(testInt);"));
     assertTrue(validator.isValidPrintStatement("System.out.print(testDouble);"));
 }






}