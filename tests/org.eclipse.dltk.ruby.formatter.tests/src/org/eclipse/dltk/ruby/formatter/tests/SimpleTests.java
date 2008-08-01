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
package org.eclipse.dltk.ruby.formatter.tests;

public class SimpleTests extends AbstractFormatterTest {

	public void test1() {
		String input = joinLines(new String[] { "class Hello",
				"\t" + "attr_accessor :var", "end" });
		String output = format(input);
		assertEquals(input, output);
	}

	public void test2() {
		String input = joinLines(new String[] { "class Hello",
				"attr_accessor :var1", "attr_accessor :var2",
				"attr_accessor :var3", "end" });
		String output = format(input);
		String expected = joinLines(new String[] { "class Hello",
				"\t" + "attr_accessor :var1", "\t" + "attr_accessor :var2",
				"\t" + "attr_accessor :var3", "end" });
		assertEquals(expected, output);
	}

	public void test3() {
		final String hw = "attr_accessor :var";
		final String tab2_hw = "\t\t\t\t" + hw;
		String input = joinLines(new String[] { "class Hello", tab2_hw,
				tab2_hw, tab2_hw, "end" });
		String output = format(input);
		final String tab_hw = "\t" + hw;
		String expected = joinLines(new String[] { "class Hello", tab_hw,
				tab_hw, tab_hw, "end" });
		assertEquals(expected, output);
	}

	public void test4() {
		String input = joinLines(new String[] { "class Hello", "def execute",
				"puts \"Hello, world\"", "end", "end" });
		String output = format(input);
		String expected = joinLines(new String[] { "class Hello",
				"\t" + "def execute", "\t\t" + "puts \"Hello, world\"",
				"\t" + "end", "end" });
		assertEquals(expected, output);
	}

}
