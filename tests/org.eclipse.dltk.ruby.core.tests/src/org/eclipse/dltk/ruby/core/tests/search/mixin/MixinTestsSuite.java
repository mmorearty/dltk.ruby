/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.core.tests.search.mixin;

import java.io.IOException;
import java.net.URL;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.StringTokenizer;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.MixinModel;
import org.eclipse.dltk.ruby.core.RubyLanguageToolkit;
import org.eclipse.dltk.ruby.core.tests.Activator;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinElementInfo;
import org.eclipse.dltk.utils.TextUtils;

public class MixinTestsSuite extends TestSuite {

	private final MixinModel model;

	public MixinTestsSuite(String testsDirectory) {
		super(testsDirectory);

		model = new MixinModel(RubyLanguageToolkit.getDefault());
		final MixinTest tests = new MixinTest("Ruby Mixin Tests");
		try {
			tests.setUpSuite();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Enumeration entryPaths = Activator.getDefault().getBundle()
				.getEntryPaths(testsDirectory);
		while (entryPaths.hasMoreElements()) {
			final String path = (String) entryPaths.nextElement();
			URL entry = Activator.getDefault().getBundle().getEntry(path);
			try {
				entry.openStream().close();
			} catch (Exception e) {
				continue;
			}
			int pos = path.lastIndexOf('/');
			final String name = (pos >= 0 ? path.substring(pos + 1) : path);
			String x = path.substring(0, pos);
			pos = x.lastIndexOf('/');
			// final String folder = (pos >= 0 ? x.substring(pos + 1) : x);

			addTest(new TestCase(name) {

				private Collection assertions = new ArrayList();

				public void setUp() {
				}

				class GetElementAssertion implements IAssertion {

					private final String key;
					private MixinModel model;

					public GetElementAssertion(String key, MixinModel model) {
						this.key = key;
						this.model = model;
					}

					public void check() throws Exception {
						model = new MixinModel(RubyLanguageToolkit.getDefault());
						IMixinElement mixinElement = model.get(key);
						if (mixinElement == null) {
							throw new AssertionFailedError("Key " + key
									+ " not found");
						}
						Object[] allObjects = mixinElement.getAllObjects();
						if (allObjects == null || allObjects.length == 0)
							throw new AssertionFailedError("Key " + key
									+ " has null or empty object set");
						for (int i = 0; i < allObjects.length; i++) {
							if (allObjects[i] == null)
								throw new AssertionFailedError("Key " + key
										+ " has null object at index " + i);
							RubyMixinElementInfo info = (RubyMixinElementInfo) allObjects[i];
							if (info.getObject() == null)
								throw new AssertionFailedError(
										"Key "
												+ key
												+ " has info with a null object at index "
												+ i + " (kind="
												+ info.getKind() + ")");
						}
					}

				}

				protected void runTest() throws Throwable {
					CharSequence content = loadContent(path);
					String[] lines = TextUtils.splitLines(content);
					int lineOffset = 0;
					for (int i = 0; i < lines.length; i++) {
						String line = lines[i].trim();
						int pos = line.indexOf("##");
						if (pos >= 0) {
							StringTokenizer tok = new StringTokenizer(line
									.substring(pos + 2));
							String test = tok.nextToken();
							if ("exit".equals(test)) {
								return;
							}
							if ("get".equals(test)) {
								String key = tok.nextToken();
								assertions.add(new GetElementAssertion(key,
										MixinTestsSuite.this.model));
							} else {
								// continue;
								// Assert.isLegal(false);
							}
						}
						lineOffset += lines[i].length() + 1;
					}

					Assert.isLegal(assertions.size() > 0);

					tests.executeTest(assertions);
					// try {
					// } finally {
					// //tests.tearDownSuite();
					// }
				}

			});
		}
	}

	private CharSequence loadContent(String path) throws IOException {
		return CharBuffer.wrap(Util.getInputStreamAsCharArray(Activator
				.openResource(path), -1, null));
	}

}
