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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
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

public abstract class AbstractRubyTestRunnerUI extends AbstractTestRunnerUI
		implements ITestRunnerUI, ITestElementResolver {

	private static final Pattern STACK_FRAME_IN_PATTERN = Pattern
			.compile("(.+):(\\d+):in `(.+)'"); //$NON-NLS-1$

	protected static final Pattern STACK_FRAME_PATTERN = RubyFilenameLinenumberResolver
			.createPattern();

	protected final IScriptProject project;
	protected final AbstractRubyTestingEngine testingEngine;

	/**
	 * @param testingEngine
	 * @param project
	 */
	public AbstractRubyTestRunnerUI(AbstractRubyTestingEngine testingEngine,
			IScriptProject project) {
		this.testingEngine = testingEngine;
		this.project = project;
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

}
