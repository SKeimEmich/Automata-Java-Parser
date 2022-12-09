package junits;

import static org.junit.jupiter.api.Assertions.*;
import main.Validator;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;

class isValidMethodSigTests {

    Validator validator = new Validator("no file provided");


    @Test
    void testValidMethodSig() {
        assertTrue(validator.isValidMethodSignature("public static void testMethod()"));
    }

    @Test
    void testVoidReturnVoid() {
        assertThrows(ParserException.class, () -> validator.isValidMethodSignature("public static int testMethod()"));
    }

    @Test
    void testSpaceBetweenNameAndReturnType() {
        assertThrows(ParserException.class, () -> validator.isValidMethodSignature("inttestMethod()"));
    }

    @Test
    void testForZeroMethodArgs() {
        assertThrows(ParserException.class, () -> validator.isValidMethodSignature("void testMethod(int arg"));

    }

    @Test
    void testExtraSpacesAllowedInSignature() {
        assertTrue(validator.isValidMethodSignature("public static void      testMethod     (   )"));

    }

    @Test
    void testForReservedKeyword() {
        assertThrows(ParserException.class, () -> validator.isValidMethodSignature("public static void enum()"));
    }

}