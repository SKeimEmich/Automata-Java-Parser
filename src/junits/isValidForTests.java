package junits;

import main.DataType;
import main.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class isValidForTests {
    Validator validator = new Validator();

    @Test
    void testForLoop() {
        assertTrue(validator.isValidFor("for(int j = 0; j <10; j++){"));
    }

    @Test
    //declaredVariables.put("i", DataType.INT); LINE 121 TO TEST IT, otherwise will fail
    void testForWithExistingVar() {
        assertTrue(validator.isValidFor("for(i=0; i<10;i++){"));
    }

}