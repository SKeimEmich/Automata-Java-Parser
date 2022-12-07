package junits;
import main.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class isValidOperationTest {
    Validator validator = new Validator("no file provided");

    @Test
    void testValidNumOp() {
        assertTrue(validator.isValidOperation("2 + 3;"));
        assertTrue(validator.isValidOperation("2 -2;"));
        assertTrue(validator.isValidOperation("2 / 2;"));
        assertTrue(validator.isValidOperation("2 %2;"));
        assertTrue(validator.isValidOperation("2 + 2;"));
        assertTrue(validator.isValidOperation("2 * 2;"));
    }

    @Test
    void testValidCharOp() {
        assertTrue(validator.isValidOperation("'3' + 'b';"));
        assertTrue(validator.isValidOperation("'3'+'b';"));
        assertTrue(validator.isValidOperation("'=' + 'k';"));
    }

    @Test
    void testCharAndNumOp() {
        assertTrue(validator.isValidOperation(" 11111 + 'b';"));
    }

    @Test
    void validInlineIncrement() {
        //validator.declaredVariables.put("testValue", DataType.INT);
        assertTrue(validator.isValidOperation("testValue++;"));
    }

    @Test
    void validInlineDecrement() {
        //validator.declaredVariables.put("testValue", DataType.INT);
        assertTrue(validator.isValidOperation("testValue--;"));
    }

    //these tests pass, tested by making declaredVariables public, adding a testValue variable and checking for it
   /* @Test
    void invalidVariableIncrement() {
        validator.declaredVariables.put("testValue", DataType.INT);
        assertFalse(validator.isValidOperation("wrongVar++"));
    }

    @Test
    void invalidVariableDecrement() {
        validator.declaredVariables.put("testValue", DataType.INT);
        assertFalse(validator.isValidOperation("wrongVar--"));
    }*/

}