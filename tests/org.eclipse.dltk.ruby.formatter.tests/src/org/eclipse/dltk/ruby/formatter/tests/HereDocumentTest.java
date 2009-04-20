package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class HereDocumentTest extends ScriptedTest {

	public static TestSuite suite() {
		return new HereDocumentTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/heredocument.rb");
	}

}
