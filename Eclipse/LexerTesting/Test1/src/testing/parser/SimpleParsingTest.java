package testing.parser;

import com.intellij.testFramework.ParsingTestCase;

import simula.lang.SimulaParserDefinition;
//import org.intellij.sdk.language.SimpleParserDefinition;

public class SimpleParsingTest extends ParsingTestCase {
  public SimpleParsingTest() {
    // Parameters: baseDir, file extension, and parser definition
//    super("", "simple", new SimpleParserDefinition());
    super("", "sim", new SimulaParserDefinition());
  }
  
  public void doSetup() {
	  try {
		super.setUp();
	  } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
  }

  @Override
  protected String getTestDataPath() {
	  System.out.println("SimpleParsingTest.getTestDataPath: ");
	  Thread.dumpStack();
	  // Path relative to the plugin module root
	  return "src/test/testData";
  }

  public void testParsingTestData(String text) {
	  System.out.println("SimpleParsingTest.testParsingTestData: ");
    // Automatically looks for testData/ParsingTestData.simple
    doTest(true, "src/test/testData/ParsingTestData"); 
  }

  @Override
  protected boolean includeRanges() {
	  System.out.println("SimpleParsingTest.includeRanges: ");
    return true; // Optional: includes text ranges in the PSI tree output
  }
}
