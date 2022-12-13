package junits;

import exceptions.ParserException;
import main.DataType;
import main.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class isValidForTests {
    Validator validator = new Validator();

    @Test
    void testValidForLoop() {
        assertTrue(validator.isValidFor("for(int j = 0; j <10; j++) {j++;}"));
        assertTrue(validator.isValidFor("for(int i = 0; j <10; j--){}"));
    }

    @Test
    void testInvalidLoopCounter() {
        assertThrows(ParserException.class, () -> validator.isValidFor("for(int j + 0; j <10; j++){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(int i; i <10; i++){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(int k - 0; k <10; k++){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(s = l; s <10; s++){}"));
    }

    @Test
    void testInvalidLoopBool(){
        assertThrows(ParserException.class, () -> validator.isValidFor("for(int j = 0; j = 10; j++){"));
    }

    @Test
    void testInvalidCounterIncOrDec() {
        assertThrows(ParserException.class, () -> validator.isValidFor("for(int j = 0; j <10; j+){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(j = 0; j <10; j-){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(j = 0; j <10; j){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(j = 0; j <10; j++++){}"));
        assertThrows(ParserException.class, () -> validator.isValidFor("for(j = 0; j <10; j-------){}"));
    }

    @Test
    void testForWithExistingCounterVar() {
        validator.addVariable("i", DataType.INT);
        assertTrue(validator.isValidFor("for(i=0; i<10;i++){}"));
    }

}