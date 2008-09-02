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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.corext.SourceRange;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.core.utils.RubySyntaxUtils;
import org.eclipse.dltk.ruby.debug.RubyFilenameLinenumberResolver;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.ResolverUtils;
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

	public String getTestCaseLabel(ITestCaseElement caseElement) {
		final String testName = caseElement.getTestName();
		int index = testName.indexOf('(');
		if (index > 0) {
			while (index > 0
					&& Character.isWhitespace(testName.charAt(index - 1))) {
				--index;
			}
			return testName.substring(0, index);
		} else {
			return testName;
		}
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

	private static final String SHOULDA_TEST_PREFIX = "test:"; //$NON-NLS-1$

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
				return new TestElementResolution(method, ResolverUtils
						.getSourceRange(method));
			}
		}
		final List types = findClasses(className);
		if (types == null) {
			return null;
		}
		if (methodName.startsWith(SHOULDA_TEST_PREFIX)) {
			String shouldName = methodName.substring(
					SHOULDA_TEST_PREFIX.length()).trim();
			if (shouldName.length() != 0
					&& shouldName.charAt(shouldName.length() - 1) == '.') {
				shouldName = shouldName.substring(0, shouldName.length() - 1)
						.trim();
			}
			if (shouldName.length() != 0) {
				final Set resources = new HashSet();
				for (Iterator i = types.iterator(); i.hasNext();) {
					final IType type = (IType) i.next();
					final IResource resource = type.getResource();
					if (resource != null && resource instanceof IFile) {
						resources.add(resource);
					}
				}
				if (resources.isEmpty()) {
					return null;
				}
				for (Iterator i = resources.iterator(); i.hasNext();) {
					final ISourceModule module = (ISourceModule) DLTKCore
							.create((IFile) i.next());
					final TestElementResolution resolution = findShould(module,
							className, shouldName);
					if (resolution != null) {
						return resolution;
					}
				}
			}
		}
		return null;
	}

	private static class ShouldLocator extends
			AbstractTestingEngineValidateVisitor {

		private static final String TWO_COLONS = "::"; //$NON-NLS-1$

		private final String className;
		private final String shouldName;

		private ISourceRange range = null;

		/**
		 * @param className
		 * @param shouldName
		 */
		public ShouldLocator(String className, String shouldName) {
			this.className = className;
			this.shouldName = shouldName;
		}

		final Stack typeMatches = new Stack();

		public boolean visit(TypeDeclaration s) throws Exception {
			final String enclosingName = s.getEnclosingTypeName();
			final String fullName;
			if (enclosingName.length() == 0) {
				fullName = s.getName();
			} else {
				fullName = enclosingName.replaceAll("\\$", TWO_COLONS) + TWO_COLONS + s.getName(); //$NON-NLS-1$
			}
			typeMatches.push(Boolean.valueOf(className.equals(fullName)));
			return true;
		}

		public boolean endvisit(TypeDeclaration s) throws Exception {
			typeMatches.pop();
			return true;
		}

		private boolean isMatchedType() {
			for (int i = 0, size = typeMatches.size(); i < size; ++i) {
				Boolean value = (Boolean) typeMatches.get(i);
				if (value.booleanValue()) {
					return true;
				}
			}
			return false;
		}

		final Stack calls = new Stack();

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (isMatchedType() && range == null) {
				if (node instanceof CallExpression) {
					final CallExpression call = (CallExpression) node;
					if (isMethodCall(call, ShouldaUtils.METHODS)
							&& call.getArgs().getChilds().size() >= 1) {
						final Object arg0 = call.getArgs().getChilds().get(0);
						if (arg0 instanceof RubyCallArgument) {
							final RubyCallArgument callArg = (RubyCallArgument) arg0;
							if (callArg.getValue() instanceof StringLiteral) {
								calls.push(call);
								if (isShouldMatched()) {
									range = new SourceRange(call.sourceStart(),
											callArg.sourceEnd()
													- call.sourceStart());
								}
							}
						}
					}
				}
			}
			return super.visitGeneral(node);
		}

		/**
		 * @return
		 */
		private boolean isShouldMatched() {
			if (isShouldMatched(shouldName)) {
				return true;
			}
			final String noTestClassName = className.replaceAll(
					"Test", Util.EMPTY_STRING); //$NON-NLS-1$
			if (startsWith(shouldName, noTestClassName)) {
				return isShouldMatched(shouldName.substring(
						noTestClassName.length()).trim());
			}
			return false;
		}

		private boolean startsWith(final String value, final String substring) {
			return value.length() > substring.length()
					&& value.startsWith(substring)
					&& Character.isWhitespace(value.charAt(substring.length()));
		}

		/**
		 * @param value
		 * @return
		 */
		private boolean isShouldMatched(String value) {
			for (int i = 0; i < calls.size(); ++i) {
				final CallExpression call = (CallExpression) calls.get(i);
				if (ShouldaUtils.SHOULD.equals(call.getName())) {
					if (!startsWith(value, ShouldaUtils.SHOULD)) {
						return false;
					}
					value = value.substring(ShouldaUtils.SHOULD.length())
							.trim();
					final RubyCallArgument callArg = (RubyCallArgument) call
							.getArgs().getChilds().get(0);
					final String literal = ((StringLiteral) callArg.getValue())
							.getValue();
					if (value.equals(literal)) {
						return true;
					}
				} else if (ShouldaUtils.CONTEXT.equals(call.getName())) {
					final RubyCallArgument callArg = (RubyCallArgument) call
							.getArgs().getChilds().get(0);
					final String literal = ((StringLiteral) callArg.getValue())
							.getValue().trim();
					if (!startsWith(value, literal)) {
						return false;
					}
					value = value.substring(literal.length()).trim();
				}
			}
			return false;
		}

		public void endvisitGeneral(ASTNode node) throws Exception {
			if (!calls.isEmpty() && calls.peek() == node) {
				calls.pop();
			}
			super.endvisitGeneral(node);
		}
	}

	/**
	 * @param module
	 * @param className
	 * @param shouldName
	 * @return
	 */
	private TestElementResolution findShould(ISourceModule module,
			String className, String shouldName) {
		final ModuleDeclaration declaration = SourceParserUtil
				.getModuleDeclaration(module);
		if (declaration != null
				&& !(declaration instanceof FakeModuleDeclaration)) {
			try {
				final ShouldLocator locator = new ShouldLocator(className,
						shouldName);
				declaration.traverse(locator);
				if (locator.range != null) {
					final ISourceRange range = ResolverUtils.adjustRange(module
							.getSource(), locator.range);
					return new TestElementResolution(module, range);
				}
			} catch (Exception e) {
				final String msg = "Error in findShould()"; //$NON-NLS-1$
				RubyTestingPlugin.error(msg, e);
			}
		}
		return null;
	}

	private TestElementResolution resolveTestSuite(ITestSuiteElement element) {
		final String className = element.getSuiteTypeName();
		if (RubySyntaxUtils.isValidClass(className)) {
			final List types = findClasses(className);
			if (types != null) {
				final IType type = (IType) types.get(0);
				return new TestElementResolution(type, ResolverUtils
						.getSourceRange(type));
			}
		}
		return null;
	}

	private static final class TypeSearchRequestor extends SearchRequestor {
		final List types = new ArrayList();

		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			types.add(match.getElement());
		}
	}

	private static final class MethodRequestor extends SearchRequestor {
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
