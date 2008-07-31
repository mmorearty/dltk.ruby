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
		String input = "class Hello\n" + "\tputs \"hello, world\"\n" + "end\n";
		String output = format(input);
		assertEquals(input, output);
	}

}
