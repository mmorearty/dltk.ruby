package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class DoTest extends ScriptedTest {

	public static TestSuite suite() {
		return new DoTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/do.rb");
	}

}
