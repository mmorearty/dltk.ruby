package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class RDocTest extends ScriptedTest {

	public static TestSuite suite() {
		return new RDocTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/rdoc.rb");
	}
}
