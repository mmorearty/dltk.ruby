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
package org.eclipse.dltk.ruby.internal.parser;

public class RubySpacedParensFixer {

	private static boolean isMethodNameChar(char inputChar, char prevChar) {
		return (((inputChar >= 'a') && (inputChar <= 'z'))
				|| ((inputChar >= '0') && (inputChar <= '9'))
				|| ((inputChar >= 'A') && (inputChar <= 'Z'))
				|| (inputChar == '_')
				|| ((inputChar == '?') && isMethodNameChar(prevChar, '@')) || ((inputChar == '!') && isMethodNameChar(
				prevChar, '@')));
	}

	private static boolean isPrefixKeyword(char[] content, int endOffset) {
		boolean isPrefixKeyword = false;
		int startOffset = -1;

		if (endOffset >= 5) {
			startOffset = (endOffset - 5);
			isPrefixKeyword = "return".equals(String.valueOf(content, startOffset, 6)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "unless".equals(String.valueOf(content, startOffset, 6)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 4) {
			startOffset = (endOffset - 4);
			isPrefixKeyword = "while".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "elsif".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$
			}

			if (!isPrefixKeyword) {
				isPrefixKeyword = "until".equals(String.valueOf(content, startOffset, 5)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 3) {
			startOffset = (endOffset - 3);
			isPrefixKeyword = "then".equals(String.valueOf(content, startOffset, 4)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "case".equals(String.valueOf(content, startOffset, 4)); //$NON-NLS-1$
			}
		}

		if (!isPrefixKeyword && endOffset >= 2) {
			startOffset = (endOffset - 2);
			isPrefixKeyword = "and".equals(String.valueOf(content, startOffset, 3)); //$NON-NLS-1$
		}

		if (!isPrefixKeyword && endOffset >= 1) {
			startOffset = (endOffset - 1);
			isPrefixKeyword = "if".equals(String.valueOf(content, startOffset, 2)); //$NON-NLS-1$

			if (!isPrefixKeyword) {
				isPrefixKeyword = "or".equals(String.valueOf(content, startOffset, 2)); //$NON-NLS-1$
			}
		}

		if (isPrefixKeyword) {
			isPrefixKeyword = ((startOffset == 0) || !isMethodNameChar(
					content[startOffset - 1],
					(startOffset > 1) ? content[startOffset - 2] : '@'));
		}

		return isPrefixKeyword;
	}

	public static char[] fixSpacedParens(char[] content) {
		char[] fixedContent = new char[content.length];
		System.arraycopy(content, 0, fixedContent, 0, content.length);

		boolean inComment = false;
		boolean inSingleString = false;
		boolean inDoubleString = false;
		boolean inBackQuoteString = false;
		boolean inBraceString = false;
		boolean inInterpString = false;
		boolean inRegexp = false;
		for (int cnt = 0, max = fixedContent.length; cnt < max; cnt++) {
			// ssanders - If there is a space between a method name and its
			// opening parenthesis
			if ((cnt > 1) && !inComment && !inSingleString && !inDoubleString
					&& !inBackQuoteString && !inBraceString && !inRegexp
					&& (fixedContent[cnt] == '(')
					&& (fixedContent[cnt - 1] == ' ')) {
				if (isMethodNameChar(fixedContent[cnt - 2],
						(cnt > 2) ? fixedContent[cnt - 3] : '@')) {
					if (!isPrefixKeyword(fixedContent, (cnt - 2))) {
						// ssanders - Invert the space and parenthesis to
						// correct warning and position info
						fixedContent[cnt - 1] = '(';
						fixedContent[cnt] = ' ';
					}
				}
			} else if (fixedContent[cnt] == '#') {
				if ((inSingleString || inDoubleString || inBackQuoteString
						|| inBraceString || inRegexp)
						&& (fixedContent[cnt + 1] == '{')) {
					inInterpString = true;
				} else if (!inSingleString && !inDoubleString
						&& !inBackQuoteString && !inBraceString) {
					inComment = true;
				}
			} else if ((fixedContent[cnt] == '\r')
					|| (fixedContent[cnt] == '\n')) {
				inComment = false;
			} else if (fixedContent[cnt] == '"') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment & !inInterpString) {
						inDoubleString = !inDoubleString;
					}
				}
			} else if (fixedContent[cnt] == '\'') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						inSingleString = !inSingleString;
					}
				}
			} else if (fixedContent[cnt] == '`') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						inBackQuoteString = !inBackQuoteString;
					}
				}
			} else if (fixedContent[cnt] == '{') {
				if ((cnt < 1) || (fixedContent[cnt - 1] == '%')) {
					if (!inComment) {
						inBraceString = true;
					}
				}
			} else if (fixedContent[cnt] == '}') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment) {
						if (inInterpString) {
							inInterpString = false;
						} else {
							inBraceString = false;
						}
					}
				}
			} else if (fixedContent[cnt] == '/') {
				if ((cnt < 1) || (fixedContent[cnt - 1] != '\\')) {
					if (!inComment && !inSingleString && !inDoubleString
							&& !inBackQuoteString && !inBraceString) {
						inRegexp = !inRegexp;
					}
				}
			}
		}

		return fixedContent;
	}

}
