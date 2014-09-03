package com.aconex.vanity;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for positive and negative cases of find vanity names.
 */
public class AppTest 
    extends TestCase
{
	VanityFinder vf = null;
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
        try {
			vf = new VanityFinder();
		} catch (Exception e) {
			fail("Unable to instantiate VanityFinder : " + e.getMessage());
		}
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * 
     */
    public void testSimpleMatch()
    {
    	List match = vf.findMatch("226639");
    	boolean matchFound = match.contains("ACONEX");
        assertTrue(matchFound);
    }
    
    public void testSingleDigitLeftOutPrefix() {
    	List match = vf.findMatch("1226639");
    	boolean matchFound = match.contains("1 ACONEX");
        assertTrue(matchFound);
    	
    }
    public void testSingleDigitLeftOutSuffix() {
    	List match = vf.findMatch("2266391");
    	boolean matchFound = match.contains("ACONEX 1");
        assertTrue(matchFound);
    	
    }
    public void testMultipleWordsWithDelimiter() {
    	List match = vf.findMatch("226639.52637");
    	boolean matchFound = match.contains("ACONEX - JAMES");
        assertTrue(matchFound);    	
    }
    
    public void testMultipleWordsWithDelimiterRecurse() {
    	List match = vf.findMatch("226639.2255.52637");
    	boolean matchFound = match.contains("ACONEX - CALL - JAMES");
        assertTrue(matchFound);    	
    }
    
    public void testIgnoreSplChars() {
    	List match = vf.findMatch("2266;39 1");
    	boolean matchFound = match.contains("ACONEX 1");
        assertTrue(matchFound);
    }
}
