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
package org.jruby.ast.ext;

import java.util.Collections;
import java.util.List;

import org.jruby.ast.Node;
import org.jruby.ast.NodeTypes;
import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.ISourcePositionHolder;

public class HeredocNode extends Node {

	private final Node content;
	private final ISourcePositionHolder endMarker;
	private final boolean indent;

	/**
	 * @param position
	 *            start marker position
	 */
	public HeredocNode(ISourcePosition position, Node content,
			ISourcePositionHolder endMarker, boolean indent) {
		super(position, NodeTypes.EXT_HEREDOC_NODE);
		this.content = content;
		this.endMarker = endMarker;
		this.indent = indent;
	}

	/*
	 * @see org.jruby.ast.Node#accept(org.jruby.ast.visitor.NodeVisitor)
	 */
	public Instruction accept(NodeVisitor visitor) {
		return visitor.visitHeredocNode(this);
	}

	/*
	 * @see org.jruby.ast.Node#childNodes()
	 */
	public List childNodes() {
		return Collections.EMPTY_LIST;
	}

	/**
	 * @return the content
	 */
	public Node getContent() {
		return content;
	}

	public ISourcePosition getContentPosition() {
		return content.getPosition();
	}

	/**
	 * @return the endMarker
	 */
	public ISourcePositionHolder getEndMarker() {
		return endMarker;
	}

	public ISourcePosition getEndMarkerPosition() {
		return endMarker.getPosition();
	}

	/**
	 * @return the indent
	 */
	public boolean isIndent() {
		return indent;
	}

}
