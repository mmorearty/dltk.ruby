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

import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterVisitor;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;

public class FormatterRescueElseNode extends FormatterBlockWithBeginNode {

	/**
	 * @param document
	 */
	public FormatterRescueElseNode(IFormatterDocument document) {
		super(document);
	}

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		if (getBegin() != null) {
			final boolean indenting = isIndenting();
			if (indenting) {
				context.decIndent();
			}
			visitor.visit(context, getBegin());
			if (indenting) {
				context.incIndent();
			}
		}
		acceptBody(context, visitor);
	}

	protected boolean isIndenting() {
		return getDocument().getBoolean(RubyFormatterConstants.INDENT_BLOCKS);
	}

}
