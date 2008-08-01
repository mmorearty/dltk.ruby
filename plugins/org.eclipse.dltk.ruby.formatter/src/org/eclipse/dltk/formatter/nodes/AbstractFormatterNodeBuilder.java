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
package org.eclipse.dltk.formatter.nodes;

import java.util.Stack;

public class AbstractFormatterNodeBuilder {

	private final Stack stack = new Stack();

	protected void start(IFormatterContainerNode root) {
		stack.clear();
		stack.push(root);
	}

	protected IFormatterContainerNode peek() {
		return (IFormatterContainerNode) stack.peek();
	}

	protected void push(IFormatterContainerNode node) {
		IFormatterContainerNode parentNode = peek();
		if (parentNode.getEndOffset() < node.getStartOffset()) {
			parentNode.addChild(createTextNode(node.getDocument(), parentNode
					.getEndOffset(), node.getStartOffset()));
		}
		parentNode.addChild(node);
		stack.push(node);
	}

	protected void checkedPop(IFormatterContainerNode expected, int bodyEnd) {
		if (stack.pop() != expected) {
			throw new IllegalStateException();
		}
		if (expected.getEndOffset() < bodyEnd) {
			expected.addChild(createTextNode(expected.getDocument(), expected
					.getEndOffset(), bodyEnd));
		}
	}

	/**
	 * @return
	 */
	protected IFormatterTextNode createTextNode(IFormatterDocument document,
			int startIndex, int endIndex) {
		return new FormatterTextNode(document, startIndex, endIndex);
	}
}
