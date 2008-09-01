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
package org.eclipse.dltk.ruby.testing.internal.testunit;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.ruby.core.utils.RubySyntaxUtils;
import org.eclipse.dltk.ruby.debug.RubyFilenameLinenumberResolver;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.RubyOpenEditorAction;
import org.eclipse.dltk.ruby.testing.internal.RubyTestingPlugin;
import org.eclipse.dltk.testing.AbstractTestRunnerUI;
import org.eclipse.dltk.testing.ITestElementResolver;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.dltk.testing.TestElementResolution;
import org.eclipse.dltk.testing.model.ITestCaseElement;
import org.eclipse.dltk.testing.model.ITestElement;
import org.eclipse.dltk.testing.model.ITestSuiteElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.osgi.util.NLS;

public class TestUnitTestRunnerUI extends AbstractTestRunnerUI implements
		ITestRunnerUI, ITestElementResolver {

	private final IScriptProject project;
	private final AbstractRubyTestingEngine testingEngine;

	/**
	 * @param testingEngine
	 */
	public TestUnitTestRunnerUI(AbstractRubyTestingEngine testingEngine,
			IScriptProject project) {
		this.testingEngine = testingEngine;
		this.project = project;
	}

	public String filterStackTrace(String trace) {
		// TODO implement filtering
		return trace;
	}

	private static final Pattern STACK_FRAME_IN_PATTERN = Pattern
			.compile("(.+):(\\d+):in `(.+)'"); //$NON-NLS-1$

	private static final Pattern STACK_FRAME_PATTERN = RubyFilenameLinenumberResolver
			.createPattern();

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
	 * @see org.eclipse.dltk.testing.ITestRunnerUI#getDisplayName()
	 */
	public String getDisplayName() {
		return testingEngine.getName();
	}

	/*
	 * @see
	 * org.eclipse.dltk.testing.AbstractTestRunnerUI#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (ITestElementResolver.class.equals(adapter)) {
			return this;
		} else {
			return super.getAdapter(adapter);
		}
	}

	public TestElementResolution resolveElement(ITestElement element) {
		if (element instanceof ITestCaseElement) {
			return resolveTestCase((ITestCaseElement) element);
		} else if (element instanceof ITestSuiteElement) {
			return resolveTestSuite((ITestSuiteElement) element);
		}
		return null;
	}

	private TestElementResolution resolveTestCase(ITestCaseElement testCase) {
		final String testName = testCase.getTestName();
		if (testName.length() == 0) {
			return null;
		}
		final int pos = testName.indexOf('(');
		if (!(pos > 0 && testName.charAt(testName.length() - 1) == ')')) {
			return null;
		}
		final String className = testName.substring(pos + 1,
				testName.length() - 1);
		if (!RubySyntaxUtils.isValidClass(className)) {
			return null;
		}
		final String methodName = testName.substring(0, pos).trim();
		if (RubySyntaxUtils.isRubyMethodName(methodName)) {
			final IMethod method = findMethod(className, methodName);
			if (method != null) {
				return new TestElementResolution(method, getSourceRange(method));
			}
		}
		return null;
	}

	private TestElementResolution resolveTestSuite(ITestSuiteElement element) {
		final List types = findClasses(element.getSuiteTypeName());
		if (types != null) {
			final IType type = (IType) types.get(0);
			return new TestElementResolution(type, getSourceRange(type));
		}
		return null;
	}

	private ISourceRange getSourceRange(final IMember member) {
		try {
			final ISourceRange range = member.getNameRange();
			if (range != null && range.getLength() > 0) {
				return range;
			}
		} catch (ModelException e) {
			// ignore
		}
		try {
			return member.getSourceRange();
		} catch (ModelException e) {
			return null;
		}
	}

	private final class TypeSearchRequestor extends SearchRequestor {
		final List types = new ArrayList();

		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			types.add(match.getElement());
		}
	}

	private final class MethodRequestor extends SearchRequestor {
		IMethod method = null;

		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			method = (IMethod) match.getElement();
		}
	}

	/**
	 * @param className
	 * @param methodName
	 * @return
	 */
	private IMethod findMethod(String className, String methodName) {
		final IDLTKSearchScope scope = getSearchScope();
		final String sPattern = className + "::" + methodName; //$NON-NLS-1$
		SearchPattern pattern = SearchPattern.createPattern(sPattern,
				IDLTKSearchConstants.METHOD, IDLTKSearchConstants.DECLARATIONS,
				SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
				scope.getLanguageToolkit());
		try {
			final MethodRequestor requestor = new MethodRequestor();
			new SearchEngine().search(pattern,
					new SearchParticipant[] { SearchEngine
							.getDefaultSearchParticipant() }, scope, requestor,
					null);
			return requestor.method;
		} catch (CoreException e) {
			final String msg = "Error in findMethod({0},{0})"; //$NON-NLS-1$
			RubyTestingPlugin.error(NLS.bind(msg, className, methodName), e);
		}
		return null;
	}

	/**
	 * @param className
	 */
	private List findClasses(String className) {
		final IDLTKSearchScope scope = getSearchScope();
		SearchPattern pattern = SearchPattern.createPattern(className,
				IDLTKSearchConstants.TYPE, IDLTKSearchConstants.DECLARATIONS,
				SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
				scope.getLanguageToolkit());
		try {
			final TypeSearchRequestor requestor = new TypeSearchRequestor();
			new SearchEngine().search(pattern,
					new SearchParticipant[] { SearchEngine
							.getDefaultSearchParticipant() }, scope, requestor,
					null);
			if (!requestor.types.isEmpty()) {
				return requestor.types;
			}
		} catch (CoreException e) {
			final String msg = "Error in findClasses({0})"; //$NON-NLS-1$
			RubyTestingPlugin.error(NLS.bind(msg, className), e);
		}
		return null;
	}

	private IDLTKSearchScope getSearchScope() {
		return SearchEngine.createSearchScope(project);
	}
}
