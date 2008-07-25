package org.eclipse.dltk.ruby.ui.bot.tests;

import org.eclipse.dltk.ruby.ui.bot.tests.editor.RubyEditorTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	public static final String PLUGIN_ID = "org.eclipse.dltk.ruby.ui.bot.tests";

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for " + PLUGIN_ID);
		suite.addTest(RubyEditorTests.suite());
		return suite;
	}
}
