/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.debug.tests.console;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;

import org.eclipse.dltk.core.tests.model.SuiteOfTestCases;
import org.eclipse.dltk.ruby.debug.RubyFilenameLinenumberResolver;

public class RubyFilenameLinenumberTests extends SuiteOfTestCases {

	public static Test suite() {
		return new Suite(RubyFilenameLinenumberTests.class);
	}

	private final Pattern pattern;

	public RubyFilenameLinenumberTests(String name) {
		super(name);
		pattern = RubyFilenameLinenumberResolver.createPattern();
	}

	public void testSimple() throws Exception {
		assertTrue(pattern.matcher("script.rb:2").matches());
	}

	public void testRoot() throws Exception {
		assertTrue(pattern.matcher("/script.rb:2").matches());
	}

	public void testFullWindows() throws Exception {
		assertTrue(pattern.matcher("C:/runtime/A/script.rb:2").matches());
	}

	public void testFullUnix() throws Exception {
		assertTrue(pattern.matcher("/home/user/runtime/A/script.rb:2")
				.matches());
	}

	public void testFrom() throws Exception {
		Matcher m = pattern
				.matcher("from c:/ruby/lib/ruby/site_ruby/1.8/rubygems.rb:61:in `require_gem'");
		assertTrue(m.find());
		assertEquals(2, m.groupCount());
		assertEquals("61", m.group(2));
		final String filename = m.group(1);
		assertTrue(filename.startsWith("c:/"));
		assertTrue(filename.endsWith("rubygems.rb"));
	}

	public void testShoulda() {
		final String input = "c:/ruby/lib/ruby/gems/1.8/gems/Shoulda-1.2.0/lib/shoulda.rb:226:in `call'";
		Matcher m = pattern.matcher(input);
		assertTrue(m.find());
	}

	public void testNegativeMatch() {
		// Matcher m = pattern
		// .matcher("C:0:Warning: require_gem is obsolete.  Use gem instead.");
		// assertFalse(m.find());
	}

}
