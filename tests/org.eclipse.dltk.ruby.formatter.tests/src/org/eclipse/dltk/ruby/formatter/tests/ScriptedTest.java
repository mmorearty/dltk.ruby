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

public class ScriptedTest extends AbstractFormatterTest {

	private final String input;
	private final String expected;

	/**
	 * @param expected
	 * @param input
	 */
	public ScriptedTest(String name, String input, String expected) {
		setName(name);
		this.expected = expected;
		this.input = input;
	}

	protected void runTest() throws Throwable {
		final String output = format(input);
		assertEquals(expected, output);
	}

}
