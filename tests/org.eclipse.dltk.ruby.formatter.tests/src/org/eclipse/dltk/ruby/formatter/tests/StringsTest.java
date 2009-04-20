package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class StringsTest extends ScriptedTest {

	public static TestSuite suite() {
		return new StringsTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/strings.rb");
	}

}
