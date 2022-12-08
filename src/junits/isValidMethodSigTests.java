package junits;

import static org.junit.jupiter.api.Assertions.*;
import main.Validator;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;

class isValidMethodSigTests {

    Validator validator = new Validator("no file provided");


    @Test
    void validMethodSig() {
        assertTrue(validator.isValidMethodSignature("public static void testMethod()"));
    }

    @Test
    void mustReturnVoid() {
        assertFalse(validator.isValidMethodSignature("public static int testMethod()"));
    }

    @Test
    void spaceBetweenNameAndReturnType() {
        assertFalse(validator.isValidMethodSignature("inttestMethod()"));
    }

    @Test
    void testForZeroMethodArgs() {
        assertFalse(validator.isValidMethodSignature("void testMethod(int arg"));
    }

    @Test
    void testExtraSpacesAllowedInSignature() {
        assertTrue(validator.isValidMethodSignature("public static void      testMethod     (   )"));

    }

    @Test
    void testForReservedKeywordName() {
        assertThrows(ParserException.class, () -> validator.isValidMethodSignature("public static void enum()"));
    }

}