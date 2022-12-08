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

    Validator validator = new Validator();

    @Test
    void testValidPrintStatement() {
        //test valid print, println, printf
        assertTrue(validator.isValidSimpleStatement("System.out.print(\"test string\");"));
    }

    @Test
    void testValidPrintln() {
        assertTrue(validator.isValidSimpleStatement("System.out.println(\"test string\");"));
    }

    @Test
    void testValidPrintf() {
        assertTrue(validator.isValidSimpleStatement("System.out.printf(\"test string\");"));
    }

    @Test
    void testPrintStatementExtraSpaces() {
        assertTrue(validator.isValidSimpleStatement("System.out.print  (\"test string\");"));
    }

    @Test
    void testPrintStatementMissingQuote() {
        assertFalse(validator.isValidSimpleStatement("System.out.print(\t missing quote\");"));
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
    void testAssignmentStatement() {
        assertTrue(validator.isValidSimpleStatement("int x = 4;"));
    }

    @Test
    void testOperationStatement() {
        assertTrue(validator.isValidSimpleStatement("2 + 5.0;"));
    }

}
