package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class UnaryPlusTest extends ScriptedTest {

	public static TestSuite suite() {
		return new UnaryPlusTest().createScriptedSuite("scripts/unaryplus.rb");
	}

}
