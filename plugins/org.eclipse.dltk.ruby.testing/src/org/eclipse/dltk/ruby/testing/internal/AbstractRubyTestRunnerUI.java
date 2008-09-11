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
package org.eclipse.dltk.ruby.testing.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.internal.launching.IPathEquality;
import org.eclipse.dltk.internal.launching.PathEqualityUtils;
import org.eclipse.dltk.ruby.debug.RubyFilenameLinenumberResolver;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.testing.AbstractTestRunnerUI;
import org.eclipse.dltk.testing.ITestElementResolver;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.dltk.testing.TestElementResolution;
import org.eclipse.dltk.testing.model.ITestCaseElement;
import org.eclipse.dltk.testing.model.ITestElement;
import org.eclipse.dltk.testing.model.ITestSuiteElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;

public abstract class AbstractRubyTestRunnerUI extends AbstractTestRunnerUI
		implements ITestRunnerUI, ITestElementResolver {

	protected static final Pattern STACK_FRAME_IN_PATTERN = Pattern
			.compile("(.+):(\\d+):in `(.+)'"); //$NON-NLS-1$

	protected static final Pattern STACK_FRAME_PATTERN = RubyFilenameLinenumberResolver
			.createPattern();

	protected final IScriptProject project;
	protected final AbstractRubyTestingEngine testingEngine;
	protected IPathEquality pathEquality;

	/**
	 * @param testingEngine
	 * @param project
	 */
	public AbstractRubyTestRunnerUI(AbstractRubyTestingEngine testingEngine,
			IScriptProject project) {
		this.testingEngine = testingEngine;
		this.project = project;
		// TODO use project environment specific entry
		this.pathEquality = PathEqualityUtils.getInstance();
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestRunnerUI#getDisplayName()
	 */
	public String getDisplayName() {
		return testingEngine.getName();
	}

	public boolean isStackFrame(String line) {
		return STACK_FRAME_IN_PATTERN.matcher(line).matches()
				|| STACK_FRAME_PATTERN.matcher(line).matches();
	}

	public IAction createOpenEditorAction(String line) {
		Matcher matcher = STACK_FRAME_IN_PATTERN.matcher(line);
		if (!matcher.matches()) {
			matcher = STACK_FRAME_PATTERN.matcher(line);
		}
		if (matcher.matches()) {
			Object element = RubyFileHyperlink.findSourceModule(matcher
					.group(1));
			if (element != null) {
				final int lineNumber;
				try {
					lineNumber = Integer.parseInt(matcher.group(2));
				} catch (NumberFormatException e) {
					return null;
				}
				return new RubyOpenEditorAction(element, lineNumber);
			}
		}
		return null;
	}

	/*
	 * @see AbstractTestRunnerUI#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (ITestElementResolver.class.equals(adapter)) {
			return this;
		} else {
			return super.getAdapter(adapter);
		}
	}

	protected final IDLTKSearchScope getSearchScope() {
		return SearchEngine.createSearchScope(project);
	}

	public TestElementResolution resolveElement(ITestElement element) {
		if (element instanceof ITestCaseElement) {
			return resolveTestCase((ITestCaseElement) element);
		} else if (element instanceof ITestSuiteElement) {
			return resolveTestSuite((ITestSuiteElement) element);
		}
		return null;
	}

	/**
	 * @param element
	 * @return
	 */
	protected abstract TestElementResolution resolveTestSuite(
			ITestSuiteElement element);

	/**
	 * @param element
	 * @return
	 */
	protected abstract TestElementResolution resolveTestCase(
			ITestCaseElement element);

	protected IPreferenceStore getPreferenceStore() {
		return RubyTestingPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * @see org.eclipse.dltk.testing.AbstractTestRunnerUI#canFilterStack()
	 */
	public boolean canFilterStack() {
		return true;
	}

	public String filterStackTrace(String trace) {
		BufferedReader reader = new BufferedReader(new StringReader(trace));
		try {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printer = new PrintWriter(stringWriter);
			String line;
			// first line contains the thrown exception
			line = reader.readLine();
			if (line != null) {
				printer.println(line);
				// the stack frames of the trace
				while ((line = reader.readLine()) != null) {
					if (isStackFrame(line)) {
						if (selectLine(line)) {
							printer.println(line);
						}
					} else {
						printer.println(line);
					}
				}
			}
			return stringWriter.toString();
		} catch (IOException e) {
			// should not happen actually
			return trace;
		}
	}

	/**
	 * Tests if the specified line should pass thru the filter.
	 * 
	 * @param line
	 * @return
	 */
	protected boolean selectLine(String line) {
		return true;
	}

	protected String extractFileName(String line) {
		Matcher matcher = STACK_FRAME_PATTERN.matcher(line);
		boolean matches = matcher.matches();
		if (!matches) {
			matcher = STACK_FRAME_IN_PATTERN.matcher(line);
			matches = matcher.matches();
		}
		if (matches) {
			return matcher.group(1);
		} else {
			return null;
		}
	}

}
