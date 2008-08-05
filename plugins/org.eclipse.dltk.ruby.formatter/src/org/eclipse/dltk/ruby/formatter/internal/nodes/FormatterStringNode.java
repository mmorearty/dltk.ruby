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
import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterVisitor;

public class FormatterStringNode extends FormatterTextNode {

	/**
	 * @param document
	 * @param startOffset
	 * @param endOffset
	 */
	public FormatterStringNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		visitor.preVisit(context, this);
		IFormatterContext strContext = context.copy();
		strContext.setIndenting(false);
		visitor.visit(strContext, this);
	}

}
