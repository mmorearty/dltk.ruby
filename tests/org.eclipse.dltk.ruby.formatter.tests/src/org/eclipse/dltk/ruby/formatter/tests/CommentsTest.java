package org.eclipse.dltk.ruby.formatter.tests;

import org.eclipse.dltk.formatter.tests.ScriptedTest;

import junit.framework.TestSuite;

public class CommentsTest extends ScriptedTest {

	public static TestSuite suite() {
		return new CommentsTest().createScriptedSuite(
				RubyFormatterTestsPlugin.CONTEXT, "scripts/comments.rb");
	}

}
