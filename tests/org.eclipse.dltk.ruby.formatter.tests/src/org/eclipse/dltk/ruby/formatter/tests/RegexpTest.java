package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class RegexpTest extends ScriptedTest {

	public static TestSuite suite() {
		return new RegexpTest().createScriptedSuite("scripts/regexp.rb");
	}

}
