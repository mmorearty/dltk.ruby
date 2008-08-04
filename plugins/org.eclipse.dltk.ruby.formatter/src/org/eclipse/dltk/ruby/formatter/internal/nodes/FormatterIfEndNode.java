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

import org.eclipse.dltk.formatter.nodes.FormatterTextNode;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.jruby.lexer.yacc.ISourcePosition;

public class FormatterIfEndNode extends FormatterTextNode {

	/**
	 * @param document
	 * @param startOffset
	 * @param endOffset
	 */
	public FormatterIfEndNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	/**
	 * @param document
	 * @param position
	 */
	public FormatterIfEndNode(IFormatterDocument document,
			ISourcePosition position) {
		this(document, position.getStartOffset(), position.getEndOffset());
	}

}
