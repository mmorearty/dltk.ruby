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

import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;

public class FormatterModuleNode extends FormatterBlockWithBeginEndNode {

	/**
	 * @param document
	 */
	public FormatterModuleNode(IFormatterDocument document) {
		super(document);
	}

	protected boolean isIndenting() {
		return getDocument().getBoolean(RubyFormatterConstants.INDENT_MODULE);
	}

	protected int getBlankLinesBefore() {
		return getDocument().getInt(RubyFormatterConstants.LINES_BEFORE_CLASS);
	}

}