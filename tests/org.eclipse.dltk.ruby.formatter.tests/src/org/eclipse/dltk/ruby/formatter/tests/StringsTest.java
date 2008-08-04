package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class StringsTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(StringsTest.class, "scripts/strings.rb");
	}

}
