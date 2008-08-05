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

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class FormatterWriter implements IFormatterVisitor {

	private final Writer writer = new StringWriter();

	private final StringBuffer indent = new StringBuffer();

	private boolean lineStarted = false;
	private char lastChar = 0;
	private final StringBuffer emptyLines = new StringBuffer();

	public void preVisit(IFormatterContext context, IFormatterTextNode node)
			throws Exception {
		if (!lineStarted) {
			startLine(context);
		}
	}

	public void visit(IFormatterContext context, IFormatterTextNode node)
			throws Exception {
		write(context, node.getText());
	}

	protected void write(IFormatterContext context, String text)
			throws IOException {
		for (int i = 0; i < text.length(); ++i) {
			write(context, text.charAt(i));
		}
	}

	/**
	 * @param context
	 * @param charAt
	 * @throws IOException
	 */
	protected void write(IFormatterContext context, char ch) throws IOException {
		if (ch == '\n' || ch == '\r') {
			if (lineStarted) {
				writer.write(ch);
				lineStarted = false;
			} else if (ch == '\n' && lastChar == '\r') {
				if (emptyLines.length() == 0) {
					writer.write(ch); // windows EOL = "\r\n"
				} else {
					emptyLines.append(ch);
				}
			} else {
				indent.setLength(0);// add option "trim empty lines"
				emptyLines.append(ch);
			}
		} else if (!lineStarted) {
			if (Character.isWhitespace(ch)) {
				indent.append(ch);
			} else {
				startLine(context);
				writer.write(ch);
			}
		} else {
			writer.write(ch);
		}
		lastChar = ch;
	}

	private void startLine(IFormatterContext context) throws IOException {
		if (emptyLines.length() != 0) {
			writer.write(emptyLines.toString());
			emptyLines.setLength(0);
		}
		if (context.isIndenting()) {
			writeIndent(context);
		} else {
			writer.write(indent.toString());
		}
		indent.setLength(0);
		lineStarted = true;
	}

	/**
	 * @param context
	 * @throws IOException
	 */
	protected void writeIndent(IFormatterContext context) throws IOException {
		for (int i = 0, indent = context.getIndent(); i < indent; ++i) {
			writer.write('\t');
		}
	}

	public String getOutput() {
		return writer.toString();
	}

}
