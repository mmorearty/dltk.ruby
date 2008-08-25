package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class CommentsTest extends ScriptedTest {

	public static TestSuite suite() {
		return new CommentsTest().createScriptedSuite("scripts/comments.rb");
	}

}
