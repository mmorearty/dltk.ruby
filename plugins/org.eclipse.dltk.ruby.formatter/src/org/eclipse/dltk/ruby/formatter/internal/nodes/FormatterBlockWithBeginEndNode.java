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
package org.eclipse.dltk.ruby.formatter.internal.nodes;

import org.eclipse.dltk.formatter.nodes.FormatterBlockNode;
import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterTextNode;
import org.eclipse.dltk.formatter.nodes.IFormatterVisitor;

public abstract class FormatterBlockWithBeginEndNode extends FormatterBlockNode {

	/**
	 * @param document
	 */
	public FormatterBlockWithBeginEndNode(IFormatterDocument document) {
		super(document);
	}

	private IFormatterTextNode begin;
	private IFormatterTextNode end;

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		if (begin != null) {
			visitor.visit(context, begin);
		}
		context.incIndent();
		super.accept(context, visitor);
		context.decIndent();
		if (end != null) {
			visitor.visit(context, end);
		}
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(IFormatterTextNode begin) {
		this.begin = begin;
	}

	/**
	 * @param node
	 */
	public void setEnd(IFormatterTextNode node) {
		this.end = node;
	}

	/*
	 * @see
	 * org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#getStartOffset()
	 */
	public int getStartOffset() {
		if (begin != null) {
			return begin.getStartOffset();
		}
		return super.getStartOffset();
	}

	/*
	 * @see
	 * org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#getEndOffset()
	 */
	public int getEndOffset() {
		if (end != null) {
			return end.getEndOffset();
		}
		if (!super.isEmpty()) {
			return super.getEndOffset();
		}
		if (begin != null) {
			return begin.getEndOffset();
		}
		return DEFAULT_OFFSET;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#isEmpty()
	 */
	public boolean isEmpty() {
		return begin == null && end == null && super.isEmpty();
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#toString()
	 */
	public String toString() {
		return begin + super.toString();
	}

}
