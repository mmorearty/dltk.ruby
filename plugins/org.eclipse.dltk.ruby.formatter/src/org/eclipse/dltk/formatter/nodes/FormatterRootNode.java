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

import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.formatter.FormatterUtils;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterRequireNode;

public class FormatterRootNode extends FormatterBlockNode {

	/**
	 * @param document
	 */
	public FormatterRootNode(IFormatterDocument document) {
		super(document);
	}

	protected void acceptNodes(final List nodes, IFormatterContext context,
			IFormatterVisitor visitor) throws Exception {
		boolean wasRequire = false;
		for (Iterator i = nodes.iterator(); i.hasNext();) {
			IFormatterNode node = (IFormatterNode) i.next();
			context.enter(node);
			if (node instanceof FormatterRequireNode) {
				if (wasRequire) {
					context.setBlankLines(0);
				}
			} else if (wasRequire
					&& (node instanceof IFormatterContainerNode || !FormatterUtils
							.isEmptyText(node))) {
				context
						.setBlankLines(getInt(RubyFormatterConstants.LINES_FILE_AFTER_REQUIRE));
				wasRequire = false;
			}
			node.accept(context, visitor);
			context.leave(node);
			if (node instanceof FormatterRequireNode) {
				wasRequire = true;
			}
		}
	}

}
