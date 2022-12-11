package junits;

import main.Validator;
import org.junit.jupiter.api.Test;
import main.DataType;

import static org.junit.jupiter.api.Assertions.*;

class isValidAssignmentTests {
    Validator validator = new Validator();

    @Test
    void testInvalidDeclaration() {
        assertFalse(validator.isValidAssignmentStatement("float test;"));
        assertFalse(validator.isValidAssignmentStatement("int y"));
        assertFalse(validator.isValidAssignmentStatement("String x;"));
        assertFalse(validator.isValidAssignmentStatement("double test"));

        //return false if variable name is reserved keyword
        assertFalse(validator.isValidAssignmentStatement("float return;"));
        assertFalse(validator.isValidAssignmentStatement("float float;"));
    }

    @Test
    void testValidBooleanAssignment() {
       assertTrue(validator.isValidAssignmentStatement("boolean x = false;"));
       assertTrue(validator.isValidAssignmentStatement("boolean y = true;"));
    }

    @Test
    void testValidCharAssignment() {
        assertFalse(validator.isValidAssignmentStatement("char a = 'a;"));
        assertTrue(validator.isValidAssignmentStatement("char b = 2 + 3;"));
    }

    @Test
    void testValidIntAssignment() {
        assertTrue(validator.isValidAssignmentStatement("int a = 1000;"));
        assertTrue(validator.isValidAssignmentStatement("int b = 234 % 5;"));
        assertTrue(validator.isValidAssignmentStatement("int j = 0;"));
    }

    @Test
    void testValidDoubleAssignment() {
        assertTrue(validator.isValidAssignmentStatement("double test = 234.0;"));
        assertTrue(validator.isValidAssignmentStatement("double x = 234;"));
        assertTrue(validator.isValidAssignmentStatement("double y = 234.0 / 5;"));
        assertTrue(validator.isValidAssignmentStatement("double z = 234 + 43.0;"));
    }

    @Test
    void testInvalidBooleanAssignment() {
        assertFalse(validator.isValidAssignmentStatement("boolean boolean = false;"));
    }

    @Test
    void testInvalidCharAssignment() {
        assertFalse(validator.isValidAssignmentStatement("char return = 'a';"));
    }

    @Test
    void testInvalidIntAssignment() {
        assertFalse(validator.isValidAssignmentStatement("int return = 1;"));
    }

    @Test void testInvalidDoubleAssignment() {
        assertFalse(validator.isValidAssignmentStatement("double int = 2.0;"));
    }

    @Test
    void testExistingVarAssignment() {
        addVariables();
        assertTrue(validator.isValidAssignmentStatement("doubleTest = 234.0;"));
        assertTrue(validator.isValidAssignmentStatement("intTest = 2;"));
        assertTrue(validator.isValidAssignmentStatement("charTest = 'a';"));
        assertTrue(validator.isValidAssignmentStatement("boolTest = true;"));
    }

    @Test
    void testExistingVarAssignOperations() {
        addVariables();
        assertTrue(validator.isValidAssignmentStatement("doubleTest = 234.0 + 10;"));
        assertTrue(validator.isValidAssignmentStatement("intTest = 2 + 5;"));
        assertTrue(validator.isValidAssignmentStatement("charTest = 'a' + 2;"));
        assertTrue(validator.isValidAssignmentStatement("boolTest = true;"));
    }

    @Test
    void testInvalidExistingVarAssignment() {
        assertFalse(validator.isValidAssignmentStatement("return = 234.0;"));
        assertFalse(validator.isValidAssignmentStatement("break = 222.0;"));
    }

    void addVariables() {
        validator.addVariable("doubleTest", DataType.DOUBLE);
        validator.addVariable("intTest", DataType.INT);
        validator.addVariable("charTest", DataType.CHAR);
        validator.addVariable("boolTest", DataType.BOOLEAN);
    }
}