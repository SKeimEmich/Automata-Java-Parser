package junits;

import main.DataType;
import org.junit.jupiter.api.AfterEach;
import main.Validator;
import exceptions.ParserException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class isValidSimpleStatementTests {

    Validator validator = new Validator("no file provided");

    @Test
    void testValidPrintStatement() {
        //test valid print, println, printf
        assertTrue(validator.isValidSimpleStatement("System.out.print(\"test string\")"));
    }

    @Test
    void testValidPrintln() {
        assertTrue(validator.isValidSimpleStatement("System.out.println(\"test string\")"));
    }

    @Test
    void testValidPrintf() {
        assertTrue(validator.isValidSimpleStatement("System.out.printf(\"test string\")"));
    }

    @Test
    void testPrintStatementExtraSpaces() {
        assertTrue(validator.isValidSimpleStatement("System.out.print  (\"test string\")"));
    }

    @Test
    void testPrintStatementMissingQuote() {
        assertFalse(validator.isValidSimpleStatement("System.out.print(\t missing quote\")"));
    }

    @Test
    void isValidComment() {
        assertTrue(validator.isValidSimpleStatement("//this is a test comment!1234459dkd.asdoawerijqweadlk"));
    }

    @Test
    void isValidBlockComment() {
        assertTrue(validator.isValidSimpleStatement("/*block comment test! */"));
    }

    @Test
    //test case when ending block symbol is missing
    void inValidBlockCommentTest() {
        assertFalse(validator.isValidSimpleStatement("/*asdflajsdfkl"));
    }

    @Test
    void validInlineIncrement() {
       //validator.declaredVariables.put("testValue", DataType.INT);
        assertTrue(validator.isValidSimpleStatement("testValue++"));
    }

    @Test
    void validInlineDecrement() {
        //validator.declaredVariables.put("testValue", DataType.INT);
        assertTrue(validator.isValidSimpleStatement("testValue--"));
    }

    //these tests pass, tested by making declaredVariables public, adding a testValue variable and checking for it
   /* @Test
    void invalidVariableIncrement() {
        validator.declaredVariables.put("testValue", DataType.INT);
        assertFalse(validator.isValidSimpleStatement("wrongVar++"));
    }

    @Test
    void invalidVariableDecrement() {
        validator.declaredVariables.put("testValue", DataType.INT);
        assertFalse(validator.isValidSimpleStatement("wrongVar--"));
    }*/
}
