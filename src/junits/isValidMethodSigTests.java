package junits;

import static org.junit.jupiter.api.Assertions.*;
import main.Validator;
import org.junit.jupiter.api.Test;
import exceptions.ParserException;

class isValidMethodSigTests {

    Validator validator = new Validator();


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

    @Test
    void testAccessModifiers() {
        assertTrue(validator.isValidMethodSignature("private static void testMethod()"));
        assertTrue(validator.isValidMethodSignature("public static void testMethod()"));
        assertTrue(validator.isValidMethodSignature("protected static void testMethod()"));
        assertTrue(validator.isValidMethodSignature("static void testMethod()"));
    }
}