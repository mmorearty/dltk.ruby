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

public class FormatterTextNode extends AbstractFormatterNode implements
		IFormatterTextNode {

	private final int startOffset;
	private final int endOffset;

	/**
	 * @param text
	 */
	public FormatterTextNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document);
		this.startOffset = startOffset;
		this.endOffset = endOffset;
	}

	public String getText() {
		return getDocument().get(startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		visitor.visit(context, this);
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterNode#getEndOffset()
	 */
	public int getEndOffset() {
		return endOffset;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterNode#getStartOffset()
	 */
	public int getStartOffset() {
		return startOffset;
	}

	public String toString() {
		return getText().trim();
	}

}
