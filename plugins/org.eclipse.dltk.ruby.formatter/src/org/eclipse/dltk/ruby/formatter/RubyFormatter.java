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
package org.eclipse.dltk.ruby.formatter;

import org.eclipse.dltk.formatter.nodes.FormatterContext;
import org.eclipse.dltk.formatter.nodes.FormatterDocument;
import org.eclipse.dltk.formatter.nodes.FormatterWriter;
import org.eclipse.dltk.formatter.nodes.IFormatterContainerNode;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.ruby.formatter.internal.RubyFormatterNodeBuilder;
import org.eclipse.dltk.ruby.formatter.internal.RubyParser;
import org.eclipse.dltk.ui.formatter.AbstractScriptFormatter;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.jruby.parser.RubyParserResult;

public class RubyFormatter extends AbstractScriptFormatter {

	public TextEdit format(String source, int offset, int length, int indent) {
		final String input = source.substring(offset, offset + length);
		final RubyParserResult result = RubyParser.parse(input);
		if (result != null) {
			final String output = format(input, result);
			if (output != null && !input.equals(output)) {
				return new ReplaceEdit(offset, length, output);
			}
		}
		return null;
	}

	/**
	 * @param input
	 * @param result
	 * @return
	 */
	private String format(String input, RubyParserResult result) {
		final RubyFormatterNodeBuilder builder = new RubyFormatterNodeBuilder();
		IFormatterDocument document = new FormatterDocument(input);
		IFormatterContainerNode root = builder.build(result, document);
		FormatterContext context = new FormatterContext();
		FormatterWriter writer = new FormatterWriter();
		try {
			root.accept(context, writer);
			return writer.getOutput();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public IFormatterContainerNode buildFormattingTree(String input) {
		final RubyParserResult result = RubyParser.parse(input);
		if (result == null) {
			return null;
		}
		final RubyFormatterNodeBuilder builder = new RubyFormatterNodeBuilder();
		final IFormatterDocument document = new FormatterDocument(input);
		return builder.build(result, document);
	}

}
