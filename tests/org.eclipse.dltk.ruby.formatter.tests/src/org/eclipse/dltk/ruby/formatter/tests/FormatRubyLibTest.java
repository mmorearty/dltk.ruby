/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.dltk.ruby.formatter.tests;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.dltk.compiler.util.Util;

public class FormatRubyLibTest extends AbstractFormatterTest {

	static final String CHARSET = "ISO-8859-1"; //$NON-NLS-1$

	private static final String SCRIPTS_ZIP = "zip/ruby-lib.zip"; //$NON-NLS-1$

	public static TestSuite suite() {
		final TestSuite suite = new TestSuite(FormatRubyLibTest.class.getName());
		final URL scripts = getResourceBundle().getResource(SCRIPTS_ZIP);
		if (scripts == null) {
			suite.addTest(new TestCase("error") { //$NON-NLS-1$
						protected void runTest() throws Throwable {
							fail(SCRIPTS_ZIP + " is not found"); //$NON-NLS-1$
						}
					});
		} else {
			try {
				final ZipInputStream zipInputStream = new ZipInputStream(
						scripts.openStream());
				try {
					int count = 0;
					ZipEntry entry;
					while ((entry = zipInputStream.getNextEntry()) != null) {
						if (!entry.isDirectory() && isRubyFile(entry.getName())) {
							final InputStream entryStream = new FilterInputStream(
									zipInputStream) {
								public void close() throws IOException {
									// empty
								}
							};
							final char[] content = Util
									.getInputStreamAsCharArray(entryStream,
											(int) entry.getSize(), CHARSET);
							final String testName = ++count
									+ "-" + entry.getName(); //$NON-NLS-1$
							suite.addTest(new FormatRubyLibTest(testName,
									content));
							zipInputStream.closeEntry();
						}
					}
				} finally {
					try {
						zipInputStream.close();
					} catch (IOException e) {
						// 
					}
				}
			} catch (final IOException e) {
				suite.addTest(new TestCase("IOException") { //$NON-NLS-1$
							protected void runTest() throws Throwable {
								throw e;
							}
						});
			}
		}
		return suite;
	}

	/**
	 * @param name
	 * @return
	 */
	private static boolean isRubyFile(String name) {
		return name.endsWith(".rb") || name.toLowerCase().endsWith(".rb");
	}

	private final char[] content;

	public FormatRubyLibTest(String name, char[] content) {
		setName(name);
		this.content = content;
	}

	protected void runTest() throws Throwable {
		final String input = new String(content);
		final String output = format(input);
		assertTrue(compareIgnoreBlanks(new StringReader(input),
				new StringReader(output)));
	}

}
