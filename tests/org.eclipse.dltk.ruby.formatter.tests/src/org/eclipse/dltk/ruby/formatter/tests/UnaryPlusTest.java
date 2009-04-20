package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class UnaryPlusTest extends ScriptedTest {

	public static TestSuite suite() {
		return new UnaryPlusTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/unaryplus.rb");
	}

}
