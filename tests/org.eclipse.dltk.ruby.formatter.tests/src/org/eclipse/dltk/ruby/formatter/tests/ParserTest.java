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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.formatter.nodes.IFormatterContainerNode;
import org.eclipse.dltk.formatter.nodes.IFormatterNode;
import org.eclipse.dltk.ruby.formatter.RubyFormatter;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterBlockWithBeginEndNode;

public class ParserTest extends TestCase {

	public void testEndKeyword() {
		final String input = "class Test" + "\r\n" + "end" + "\r\n";
		IFormatterContainerNode root = new RubyFormatter()
				.buildFormattingTree(input);
		assertNotNull(root);
		List children = root.getChildren();
		assertEquals(2, children.size());
		final FormatterBlockWithBeginEndNode classNode = (FormatterBlockWithBeginEndNode) children
				.get(0);
		IFormatterNode endNode = classNode.getEnd();
		assertEquals(3, endNode.getEndOffset() - endNode.getStartOffset());
	}

}
