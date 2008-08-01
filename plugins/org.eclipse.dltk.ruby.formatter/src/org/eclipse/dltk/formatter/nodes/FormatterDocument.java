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

public class FormatterDocument implements IFormatterDocument {

	private final String text;

	/**
	 * @param text
	 */
	public FormatterDocument(String text) {
		this.text = text;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterDocument#getText()
	 */
	public String getText() {
		return text;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterDocument#getLength()
	 */
	public int getLength() {
		return text.length();
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterDocument#get(int,
	 * int)
	 */
	public String get(int startOffset, int endOffset) {
		return text.substring(startOffset, endOffset);
	}

}
