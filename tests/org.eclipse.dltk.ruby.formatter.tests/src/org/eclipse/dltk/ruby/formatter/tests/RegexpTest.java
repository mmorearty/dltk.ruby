package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class RegexpTest extends ScriptedTest {

	public static TestSuite suite() {
		return new RegexpTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/regexp.rb");
	}

}
