package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class RDocTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(RDocTest.class.getName(), "scripts/rdoc.rb");
	}
}
