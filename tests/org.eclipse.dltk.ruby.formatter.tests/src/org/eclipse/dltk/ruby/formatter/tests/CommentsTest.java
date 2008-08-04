package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestSuite;

public class CommentsTest extends AbstractFormatterTest {

	public static TestSuite suite() {
		return createScriptedSuite(CommentsTest.class, "scripts/comments.rb");
	}

}
