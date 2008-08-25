package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class StringsTest extends ScriptedTest {

	public static TestSuite suite() {
		return new StringsTest().createScriptedSuite("scripts/strings.rb");
	}

}
