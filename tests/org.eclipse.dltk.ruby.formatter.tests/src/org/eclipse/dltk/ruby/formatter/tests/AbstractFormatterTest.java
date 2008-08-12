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

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import java.util.Collection;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.internal.corext.util.Strings;
import org.eclipse.dltk.ruby.formatter.RubyFormatter;
import org.eclipse.dltk.ruby.formatter.internal.RubyFormatterPlugin;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.osgi.framework.Bundle;

public abstract class AbstractFormatterTest extends TestCase {

	/**
	 * @param input
	 * @return
	 */
	protected static String format(String input) {
		RubyFormatter f = new RubyFormatter(Util.LINE_SEPARATOR, RubyFormatter
				.createTestingPreferences());
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

	protected static String joinLines(Collection lines) {
		return joinLines((String[]) lines.toArray(new String[lines.size()]));
	}

	protected static String joinLines(String[] lines) {
		return Strings.concatenate(lines, Util.LINE_SEPARATOR)
				+ Util.LINE_SEPARATOR;
	}

	/**
	 * @param lines
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	protected static String joinLines(String[] lines, int beginIndex,
			int endIndex) {
		final StringBuffer sb = new StringBuffer();
		for (int i = beginIndex; i < endIndex; ++i) {
			sb.append(lines[i]);
			sb.append(Util.LINE_SEPARATOR);
		}
		return sb.toString();
	}

	protected static Bundle getResourceBundle() {
		return RubyFormatterPlugin.getDefault().getBundle();
	}

	protected static char[] readResource(String resourceName)
			throws IOException {
		final URL resource = getResourceBundle().getResource(resourceName);
		assertNotNull(resourceName + " is not found", resource); //$NON-NLS-1$
		return Util.getInputStreamAsCharArray(resource.openStream(), -1,
				AllTests.CHARSET);
	}

	private static final String TEST_MARKER = "====";
	private static final String RESPONSE_MARKER = "==";

	public static TestSuite createScriptedSuite(Class suiteClass,
			String resourceName) {
		return createScriptedSuite(suiteClass.getName(), resourceName, 0);
	}

	public static TestSuite createScriptedSuite(Class suiteClass,
			String resourceName, int beginTestIndex) {
		return createScriptedSuite(suiteClass.getName(), resourceName,
				beginTestIndex);
	}

	public static TestSuite createScriptedSuite(String suiteName,
			String resourceName) {
		return createScriptedSuite(suiteName, resourceName, 0);
	}

	/**
	 * @param resourceName
	 * @return
	 */
	public static TestSuite createScriptedSuite(String suiteName,
			String resourceName, int beginTestIndex) {
		final TestSuite suite = new TestSuite(suiteName);
		try {
			final String content = new String(readResource(resourceName));
			final String[] lines = TextUtils.splitLines(content);
			String testName = "START";
			int testIndex = 0;
			int testBegin = 0;
			int responseBegin = -1;
			int i = 0;
			while (i < lines.length) {
				final String line = lines[i++];
				if (line.startsWith(TEST_MARKER)) {
					final int testEnd = i - 1;
					if (testEnd > testBegin) {
						if (responseBegin < 0) {
							throw new IllegalArgumentException(
									"No response marker - next test started on line "
											+ testEnd);
						}
						if (testIndex >= beginTestIndex) {
							suite.addTest(createTest(testName, lines,
									testBegin, responseBegin, testEnd));
						}
						++testIndex;
					}
					testBegin = i;
					responseBegin = -1;
					testName = line.substring(TEST_MARKER.length()).trim();
				} else if (line.startsWith(RESPONSE_MARKER)) {
					if (responseBegin >= 0) {
						throw new IllegalArgumentException(
								"Multiple response markers: line " + (i - 1)
										+ ", previous on line " + responseBegin);
					}
					responseBegin = i;
				}
			}
			if (lines.length > testBegin) {
				if (responseBegin < 0) {
					throw new IllegalArgumentException(
							"No response marker in last test");
				}
				if (testIndex >= beginTestIndex) {
					suite.addTest(createTest(testName, lines, testBegin,
							responseBegin, lines.length));
				}
			}
		} catch (final Throwable e) {
			suite.addTest(new TestCase(e.getClass().getName()) { //$NON-NLS-1$
						protected void runTest() throws Throwable {
							throw e;
						}
					});
		}
		return suite;
	}

	private static ScriptedTest createTest(String testName, String[] lines,
			int testBegin, int responseBegin, final int testEnd) {
		final String input = joinLines(lines, testBegin, responseBegin - 1);
		final String expected = joinLines(lines, responseBegin, testEnd);
		return new ScriptedTest(testName, input, expected);
	}

	protected boolean compareIgnoreBlanks(Reader inputReader,
			Reader outputReader) throws IOException {
		LineNumberReader input = new LineNumberReader(inputReader);
		LineNumberReader output = new LineNumberReader(outputReader);
		for (;;) {
			String inputLine;
			do {
				inputLine = input.readLine();
				if (inputLine != null) {
					inputLine = inputLine.trim();
				}
			} while (inputLine != null && inputLine.length() == 0);
			String outputLine;
			do {
				outputLine = output.readLine();
				if (outputLine != null) {
					outputLine = outputLine.trim();
				}
			} while (outputLine != null && outputLine.length() == 0);
			if (inputLine == null) {
				if (outputLine == null) {
					return true;
				} else {
					fail("Extra output " + output.getLineNumber() + ":"
							+ outputLine);
				}
			} else if (outputLine == null) {
				fail("Missing output " + input.getLineNumber() + ":"
						+ inputLine);
			} else if (!inputLine.equals(outputLine)) {
				throw new ComparisonFailure("Comparison failed", input
						.getLineNumber()
						+ ":" + inputLine, output.getLineNumber() + ":"
						+ outputLine);
			}
		}
	}

}
