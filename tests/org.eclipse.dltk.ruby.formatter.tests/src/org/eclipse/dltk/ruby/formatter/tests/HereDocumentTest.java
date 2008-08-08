package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class HereDocumentTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(HereDocumentTest.class,
				"scripts/heredocument.rb", 0);
	}

}
