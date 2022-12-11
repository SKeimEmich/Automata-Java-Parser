package junits;
import exceptions.ParserException;
import main.DataType;
import main.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class isValidOperationTest {
    Validator validator = new Validator();

    @Test
    void testValidNumOp() {
        assertTrue(validator.isValidOperation("2 + 3;"));
        assertTrue(validator.isValidOperation("2 -2;"));
        assertTrue(validator.isValidOperation("2 / 2;"));
        assertTrue(validator.isValidOperation("2 %2;"));
        assertTrue(validator.isValidOperation("2 * 2;"));
    }

    @Test
    void testValidOperationsWithVar() {
        validator.addVariable("testVar", DataType.INT);
        assertTrue(validator.isValidOperation("testVar + 2;"));
    }

    @Test
    void testInvalidBooleanOperation() {
        validator.addVariable("testVar", DataType.BOOLEAN);
        assertThrows(ParserException.class, () -> validator.isValidOperation("testVar + 2;"));
    }

    @Test
    void testValidCharOp() {
        assertTrue(validator.isValidOperation("'3' + 'b';"));
        assertTrue(validator.isValidOperation("'3'+'b';"));
    }

    @Test
    void testCharAndNumOp() {
        assertTrue(validator.isValidOperation(" 11111 + 'b';"));
    }

    @Test
    void validInlineOps() {
        validator.addVariable("testValue", DataType.INT);
        assertTrue(validator.isValidOperation("testValue++;"));
        assertTrue(validator.isValidOperation("testValue--;"));
    }

    @Test
    void testMultipleOperations() {
        validator.addVariable("test", DataType.INT);
        assertTrue(validator.isValidOperation("3 + 3 - 2 + test;"));
    }

    @Test
    void testInvalidInlineOps() {
        assertFalse(validator.isValidOperation("testValue-;"));
        assertFalse(validator.isValidOperation("testValue+;"));
        assertFalse(validator.isValidOperation("testValue;"));
        assertFalse(validator.isValidOperation("testValue++++++;"));
        assertFalse(validator.isValidOperation("testValue--------;"));
    }

}