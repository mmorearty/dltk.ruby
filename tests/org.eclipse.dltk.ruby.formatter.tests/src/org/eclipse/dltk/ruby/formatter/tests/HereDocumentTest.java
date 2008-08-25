package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class HereDocumentTest extends ScriptedTest {

	public static TestSuite suite() {
		return new HereDocumentTest()
				.createScriptedSuite("scripts/heredocument.rb");
	}

}
