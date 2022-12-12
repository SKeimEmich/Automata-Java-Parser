package junits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({

	AddVariableTests.class,
	GetTypeTests.class,
	IsValidIfTests.class,
	IsValidBoolExpressionTests.class,
	isValidSimpleStatementTests.class,
	isValidMethodSigTests.class,
	isValidAssignmentTests.class,
	isValidForTests.class,
	isValidOperationTest.class,
	isValidWhileLoopTests.class, 
	isValidSwitchTests.class
})

public class TestSuite{}
