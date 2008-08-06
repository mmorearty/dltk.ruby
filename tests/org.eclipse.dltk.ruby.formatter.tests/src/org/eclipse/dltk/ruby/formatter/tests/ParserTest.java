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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.formatter.nodes.IFormatterContainerNode;
import org.eclipse.dltk.formatter.nodes.IFormatterNode;
import org.eclipse.dltk.ruby.formatter.RubyFormatter;
import org.eclipse.dltk.ruby.formatter.internal.RubyParser;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterBlockWithBeginEndNode;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.ListNode;
import org.jruby.ast.Node;
import org.jruby.ast.StrNode;
import org.jruby.ast.visitor.AbstractVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.parser.RubyParserResult;

public class ParserTest extends AbstractFormatterTest {

	public void testEndKeyword() {
		final String input = "class Test" + Util.LINE_SEPARATOR + "end"
				+ Util.LINE_SEPARATOR;
		IFormatterContainerNode root = new RubyFormatter()
				.buildFormattingTree(input);
		assertNotNull(root);
		List children = root.getChildren();
		assertEquals(2, children.size());
		final FormatterBlockWithBeginEndNode classNode = (FormatterBlockWithBeginEndNode) children
				.get(0);
		IFormatterNode endNode = classNode.getEnd();
		assertEquals(3, endNode.getEndOffset() - endNode.getStartOffset());
		assertEquals(input.indexOf("end"), endNode.getStartOffset());
	}

	public void testEmptyHereDoc() {
		final String input = "a = <<THIS" + Util.LINE_SEPARATOR + "THIS"
				+ Util.LINE_SEPARATOR;
		RubyParserResult result = RubyParser.parse(input);
		assertNotNull(result);
	}

	public void _testHereDocParser() {
		final String id1 = "THIS";
		final String[] hereDoc1 = new String[] { "Line 1=#$a", "Line 2" };
		final String id2 = "THAT";
		final String[] hereDoc2 = new String[] { "Line 3=#$b", "Line 4" };
		List lines = new ArrayList();
		lines.add("def myfunc");
		lines.add("print <<\"" + id1 + "\", <<\"" + id2
				+ "\",1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19");
		lines.addAll(Arrays.asList(hereDoc1));
		lines.add(id1);
		lines.addAll(Arrays.asList(hereDoc2));
		lines.add(id2);
		lines.add("end");
		String input = joinLines(lines);
		RubyParserResult result = RubyParser.parse(input);
		assertNotNull(result);
		final List stringNodes = new ArrayList();
		result.getAST().accept(new AbstractVisitor() {

			protected Instruction visitNode(Node visited) {
				if (visited instanceof StrNode) {
					stringNodes.add(visited);
				}
				visitChildren(visited);
				return null;
			}

			private void visitChildren(Node visited) {
				List children = visited.childNodes();
				for (Iterator i = children.iterator(); i.hasNext();) {
					final Node child = (Node) i.next();
					visitChild(child);
				}
			}

			private void visitChild(final Node child) {
				if (child != null && isVisitable(child)) {
					child.accept(this);
				}
			}

			private boolean isVisitable(Node node) {
				return !(node instanceof ArgumentNode)
						&& node.getClass() != ListNode.class;
			}

		});
		assertEquals(2, stringNodes.size());
		final StrNode str1 = (StrNode) stringNodes.get(0);
		System.out.println(str1);
		System.out.println(str1.getPosition());
		System.out.println(input.indexOf(id1));
		System.out.println(input.indexOf("<<"));
		System.out.println(input.indexOf(hereDoc1[0]));
		final String str1value = str1.getValue().toString();
		assertTrue(str1value.indexOf(hereDoc1[0]) >= 0);
		assertTrue(str1value.indexOf(hereDoc1[1]) >= 0);
		final StrNode str2 = (StrNode) stringNodes.get(1);
		System.out.println(str2);
		System.out.println(str2.getPosition());
		System.out.println(input.indexOf(id2));
		System.out.println(input.lastIndexOf("<<"));
		System.out.println(input.indexOf(hereDoc2[0]));
		final String str2value = str2.getValue().toString();
		assertTrue(str2value.indexOf(hereDoc2[0]) >= 0);
		assertTrue(str2value.indexOf(hereDoc2[1]) >= 0);
	}

}
