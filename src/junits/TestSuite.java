package junits;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	AddVariableTests.class,
	GetTypeTests.class
})
public class TestSuite{}