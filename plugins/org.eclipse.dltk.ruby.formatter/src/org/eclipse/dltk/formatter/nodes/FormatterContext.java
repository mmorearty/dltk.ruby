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

public class FormatterContext implements IFormatterContext {

	private int indent;

	public FormatterContext() {
		this(0);
	}

	public FormatterContext(int indent) {
		this.indent = indent;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#copy()
	 */
	public IFormatterContext copy() {
		return new FormatterContext(indent);
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#decIndent()
	 */
	public void decIndent() {
		--indent;
		assert indent >= 0;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#incIndent()
	 */
	public void incIndent() {
		++indent;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#resetIndent()
	 */
	public void resetIndent() {
		indent = 0;
	}

	/*
	 * @see org.eclipse.dltk.ruby.formatter.node.IFormatterContext#getIndent()
	 */
	public int getIndent() {
		return indent;
	}

}
