package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class RegexpTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(RegexpTest.class, "scripts/regexp.rb");
	}

}
