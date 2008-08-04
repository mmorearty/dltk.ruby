package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class DoTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(DoTest.class, "scripts/do.rb");
	}

}
