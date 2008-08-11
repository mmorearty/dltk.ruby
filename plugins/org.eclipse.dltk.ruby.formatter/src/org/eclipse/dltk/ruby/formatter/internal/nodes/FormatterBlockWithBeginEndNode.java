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

import java.util.ArrayList;
import java.util.List;

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
		context.setBlankLines(getBlankLinesBefore());
		if (begin != null) {
			visitor.visit(context, begin);
		}
		context.resetBlankLines();
		final boolean indenting = isIndenting();
		if (indenting) {
			context.incIndent();
		}
		super.accept(context, visitor);
		if (indenting) {
			context.decIndent();
		}
		if (end != null) {
			visitor.visit(context, end);
		}
	}

	protected int getBlankLinesBefore() {
		return 0;
	}

	/**
	 * @return the begin
	 */
	public IFormatterTextNode getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(IFormatterTextNode begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public IFormatterTextNode getEnd() {
		return end;
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
	 * @see org.eclipse.dltk.formatter.nodes.FormatterBlockNode#getChildren()
	 */
	public List getChildren() {
		if (begin == null && end == null) {
			return super.getChildren();
		} else {
			List result = new ArrayList();
			if (begin != null) {
				result.add(begin);
			}
			result.addAll(super.getChildren());
			if (end != null) {
				result.add(end);
			}
			return result;
		}
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.FormatterBlockNode#toString()
	 */
	public String toString() {
		return begin + "\n" + super.toString() + "\n" + end;
	}

}
