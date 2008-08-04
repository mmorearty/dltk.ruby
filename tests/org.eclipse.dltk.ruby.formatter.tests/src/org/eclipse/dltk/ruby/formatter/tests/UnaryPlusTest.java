package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class UnaryPlusTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(UnaryPlusTest.class, "scripts/unaryplus.rb");
	}

}
