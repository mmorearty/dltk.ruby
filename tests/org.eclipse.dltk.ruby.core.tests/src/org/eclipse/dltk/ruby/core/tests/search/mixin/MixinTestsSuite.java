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
import java.util.Iterator;
import java.util.StringTokenizer;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.MixinModel;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.ruby.core.RubyLanguageToolkit;
import org.eclipse.dltk.ruby.core.tests.Activator;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinElementInfo;
import org.eclipse.dltk.utils.TextUtils;

public class MixinTestsSuite extends TestSuite {

	public static Test suite() {
		return new MixinTestsSuite("/workspace/automixins/src");
	}

	private static final class MixinTestCase extends TestCase {
		private final String path;

		private MixinTestCase(String name, String path) {
			super(name);
			this.path = path;
		}

		protected void runTest() throws Throwable {
			final Collection assertions = new ArrayList();
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
						assertions.add(new GetElementAssertion(key));
					} else {
						// continue;
						// Assert.isLegal(false);
					}
				}
				lineOffset += lines[i].length() + 1;
			}

			assertTrue(assertions.size() > 0);
			for (Iterator iter = assertions.iterator(); iter.hasNext();) {
				IAssertion assertion = (IAssertion) iter.next();
				assertion.check();
			}
		}

		private CharSequence loadContent(String path) throws IOException {
			return CharBuffer.wrap(Util.getInputStreamAsCharArray(Activator
					.openResource(path), -1, null));
		}
	}

	private static class GetElementAssertion implements IAssertion {

		private final String key;

		public GetElementAssertion(String key) {
			this.key = key;
		}

		public void check() throws Exception {
			final MixinModel model = new MixinModel(RubyLanguageToolkit
					.getDefault());
			try {
				IMixinElement mixinElement = model.get(key);
				if (mixinElement == null) {
					throw new AssertionFailedError("Key " + key + " not found");
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
						throw new AssertionFailedError("Key " + key
								+ " has info with a null object at index " + i
								+ " (kind=" + info.getKind() + ")");
				}
			} finally {
				model.stop();
			}
		}

	}

	public MixinTestsSuite(String testsDirectory) {
		super(testsDirectory);

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
			final int pos = path.lastIndexOf('/');
			final String name = pos >= 0 ? path.substring(pos + 1) : path;

			addTest(new MixinTestCase(name, path));
		}
	}

	private static class SuiteSetupTeardown extends AbstractModelTests {

		private static final String SRC_PROJECT = "automixins";

		public SuiteSetupTeardown() {
			super(Activator.PLUGIN_ID, Util.EMPTY_STRING);
		}

		public void setUpSuite() throws Exception {
			super.setUpSuite();
			setUpScriptProject(SRC_PROJECT);
			waitUntilIndexesReady();
		}

		public void tearDownSuite() throws Exception {
			deleteProject(SRC_PROJECT);
			super.tearDownSuite();
		}

	}

	/*
	 * @see junit.framework.TestSuite#run(junit.framework.TestResult)
	 */
	public void run(TestResult result) {
		final SuiteSetupTeardown setupTeardown = new SuiteSetupTeardown();
		try {
			setupTeardown.setUpSuite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.run(result);
		try {
			setupTeardown.tearDownSuite();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
