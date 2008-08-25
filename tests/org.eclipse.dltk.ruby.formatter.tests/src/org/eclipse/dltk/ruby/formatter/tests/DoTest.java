package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class DoTest extends ScriptedTest {

	public static TestSuite suite() {
		return new DoTest().createScriptedSuite("scripts/do.rb");
	}

}
