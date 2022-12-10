package junits;
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
    void validInlineOps() {
        //validator.declaredVariables.put("testValue", DataType.INT);
        assertTrue(validator.isValidOperation("testValue++;"));
        assertTrue(validator.isValidOperation("testValue--;"));
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