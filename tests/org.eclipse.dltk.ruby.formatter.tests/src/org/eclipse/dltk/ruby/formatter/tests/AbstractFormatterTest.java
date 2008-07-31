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
package org.eclipse.dltk.ruby.formatter.tests;

import junit.framework.TestCase;

import org.eclipse.dltk.ruby.formatter.RubyFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public abstract class AbstractFormatterTest extends TestCase {

	/**
	 * @param input
	 * @return
	 */
	protected String format(String input) {
		RubyFormatter f = new RubyFormatter();
		final TextEdit edit = f.format(input, 0, input.length(), 0);
		if (edit != null) {
			final IDocument document = new Document(input);
			try {
				edit.apply(document);
			} catch (BadLocationException e) {
				throw new RuntimeException(e);
			}
			return document.get();
		}
		return input;
	}

}
