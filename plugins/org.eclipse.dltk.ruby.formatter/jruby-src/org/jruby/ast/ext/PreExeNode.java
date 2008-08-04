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

import java.util.List;

import org.jruby.ast.Node;
import org.jruby.ast.NodeTypes;
import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.lexer.yacc.ISourcePositionHolder;
import org.jruby.lexer.yacc.SourcePosition;

public class PreExeNode extends Node {

	private final ISourcePositionHolder keyword;
	private final ISourcePositionHolder leftBrace;
	private final Node body;
	private final ISourcePositionHolder rightBrace;

	/**
	 * @param position
	 * @param id
	 */
	public PreExeNode(ISourcePositionHolder keyword,
			ISourcePositionHolder leftBrace, Node body,
			ISourcePositionHolder rightBrace) {
		super(SourcePosition.combinePosition(keyword.getPosition(), rightBrace
				.getPosition()), NodeTypes.EXT_PRE_EXE_NODE);
		this.keyword = keyword;
		this.leftBrace = leftBrace;
		this.body = body;
		this.rightBrace = rightBrace;
	}

	/*
	 * @see org.jruby.ast.Node#accept(org.jruby.ast.visitor.NodeVisitor)
	 */
	public Instruction accept(NodeVisitor visitor) {
		return visitor.visitPreExeNode(this);
	}

	/*
	 * @see org.jruby.ast.Node#childNodes()
	 */
	public List childNodes() {
		return createList(body);
	}

	/**
	 * @return the keyword
	 */
	public ISourcePositionHolder getKeyword() {
		return keyword;
	}

	/**
	 * @return the leftBrace
	 */
	public ISourcePositionHolder getLeftBrace() {
		return leftBrace;
	}

	/**
	 * @return the body
	 */
	public Node getBody() {
		return body;
	}

	/**
	 * @return the rightBrace
	 */
	public ISourcePositionHolder getRightBrace() {
		return rightBrace;
	}

}
