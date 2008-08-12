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
package org.eclipse.dltk.formatter;

import org.eclipse.dltk.formatter.nodes.IFormatterNode;
import org.eclipse.dltk.formatter.nodes.IFormatterTextNode;

public class FormatterUtils {

	public static boolean isSpace(char c) {
		return c == '\t' || c == ' ';
	}

	/**
	 * @param node
	 * @return
	 */
	public static boolean isEmptyText(IFormatterNode node) {
		if (node instanceof IFormatterTextNode) {
			final String text = ((IFormatterTextNode) node).getText();
			for (int i = 0; i < text.length(); ++i) {
				char c = text.charAt(i);
				if (!Character.isWhitespace(c)) {
					return false;
				}
			}
		}
		return true;
	}

}
