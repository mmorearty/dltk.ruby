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
package org.eclipse.dltk.ruby.testing.internal.rspec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.NumericLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.corext.SourceRange;
import org.eclipse.dltk.ruby.ast.RubyASTUtil;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestRunnerUI;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.ResolverUtils;
import org.eclipse.dltk.ruby.testing.internal.RubyTestingPlugin;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.DLTKTestingPlugin;
import org.eclipse.dltk.testing.TestElementResolution;
import org.eclipse.dltk.testing.model.ITestCaseElement;
import org.eclipse.dltk.testing.model.ITestElement;
import org.eclipse.dltk.testing.model.ITestElementPredicate;
import org.eclipse.dltk.testing.model.ITestRunSession;
import org.eclipse.dltk.testing.model.ITestSuiteElement;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultLineTracker;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.osgi.util.NLS;

public class RSpecTestRunnerUI extends AbstractRubyTestRunnerUI {

	private static final char PATH_BEGIN = '<';

	/**
	 * @param testingEngine
	 * @param project
	 */
	public RSpecTestRunnerUI(RspecTestingEngine testingEngine,
			IScriptProject project) {
		super(testingEngine, project);
	}

	/*
	 * @see AbstractTestRunnerUI#getTestCaseLabel(ITestCaseElement, boolean)
	 */
	public String getTestCaseLabel(ITestCaseElement caseElement, boolean full) {
		final String testName = caseElement.getTestName();
		int index = testName.lastIndexOf(PATH_BEGIN);
		if (index >= 0) {
			if (full) {
				final String template = DLTKTestingMessages.TestSessionLabelProvider_testMethodName_className;
				return NLS.bind(template, testName.substring(index + 1),
						testName.substring(0, index));
			} else {
				return testName.substring(0, index);
			}
		}
		return testName;
	}

	/*
	 * @see AbstractTestRunnerUI#getTestStartedMessage(ITestCaseElement)
	 */
	public String getTestStartedMessage(ITestCaseElement caseElement) {
		final String testName = caseElement.getTestName();
		int index = testName.lastIndexOf(PATH_BEGIN);
		if (index >= 0) {
			final String template = DLTKTestingMessages.TestRunnerViewPart_message_started;
			return NLS.bind(template, testName.substring(index + 1), testName
					.substring(0, index));
		}
		return testName;
	}

	private static class RSpecLocator extends
			AbstractTestingEngineValidateVisitor {

		protected ASTNode collectArgs(final CallArgumentsList args,
				final List texts) {
			ASTNode lastArg = null;
			for (Iterator i = args.getChilds().iterator(); i.hasNext();) {
				final Object arg = i.next();
				if (arg instanceof RubyCallArgument) {
					final ASTNode value = ((RubyCallArgument) arg).getValue();
					final String text = toText(value);
					if (text != null) {
						texts.add(text);
						lastArg = value;
					}
				}
			}
			return lastArg;
		}

		/**
		 * @param value
		 * @return
		 */
		private String toText(ASTNode value) {
			if (value instanceof StringLiteral) {
				return ((StringLiteral) value).getValue().trim();
			}
			if (value instanceof NumericLiteral) {
				return ((NumericLiteral) value).getValue();
			}
			return RubyASTUtil.resolveReference(value);
		}

		/**
		 * @param value
		 * @return
		 */
		protected boolean isMatched(String value, List texts) {
			final StringBuffer sb = new StringBuffer();
			for (Iterator i = texts.iterator(); i.hasNext();) {
				if (sb.length() != 0) {
					sb.append(' ');
				}
				sb.append(i.next());
			}
			return value.equals(sb.toString());
		}

		public void process(final ISourceModule module) {
			final ModuleDeclaration declaration = ResolverUtils.parse(module);
			if (declaration != null) {
				try {
					declaration.traverse(this);
				} catch (Exception e) {
					RubyTestingPlugin.error("Error in resolveTestSuite", e); //$NON-NLS-1$
				}
			}
		}

	}

	static class RSpecContextLocator extends RSpecLocator {

		private final String contextName;

		private ISourceRange range = null;

		public RSpecContextLocator(String contextName) {
			this.contextName = contextName;
		}

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (range == null) {
				if (node instanceof CallExpression) {
					final CallExpression call = (CallExpression) node;
					if (isMethodCall(call, RSpecUtils.CONTEXT_METHODS)) {
						final CallArgumentsList args = call.getArgs();
						if (args.getChilds().size() >= 1) {
							final List texts = new ArrayList();
							final ASTNode lastArg = collectArgs(args, texts);
							if (!texts.isEmpty()
									&& isMatched(contextName, texts)) {
								assert (lastArg != null);
								range = new SourceRange(call.sourceStart(),
										lastArg.sourceEnd()
												- call.sourceStart());
							}
						}
					}
				}
			}
			return super.visitGeneral(node);
		}

	}

	private static class RSpecTestLocator extends RSpecLocator {
		private final String contextName;
		private final String testName;

		private ISourceRange range = null;

		public RSpecTestLocator(String contextName, String testName) {
			this.contextName = contextName;
			this.testName = testName;
		}

		private static class State {
			final ASTNode callNode;
			final boolean isMatched;

			public State(ASTNode callNode, boolean isMatched) {
				this.callNode = callNode;
				this.isMatched = isMatched;
			}

		}

		private final Stack states = new Stack();

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (range == null) {
				if (node instanceof CallExpression) {
					final CallExpression call = (CallExpression) node;
					final CallArgumentsList args = call.getArgs();
					if (args.getChilds().size() >= 1) {
						if (isMethodCall(call, RSpecUtils.CONTEXT_METHODS)) {
							boolean matched = false;
							final List texts = new ArrayList();
							final ASTNode lastArg = collectArgs(args, texts);
							if (!texts.isEmpty()
									&& isMatched(contextName, texts)) {
								assert (lastArg != null);
								matched = true;
								// range = new SourceRange(call.sourceStart(),
								// lastArg.sourceEnd()
								// - call.sourceStart());
							}
							states.push(new State(node, matched));
						} else if (isMatchingContext()
								&& isMethodCall(call, RSpecUtils.TEST_METHODS)) {
							final List texts = new ArrayList();
							final ASTNode lastArg = collectArgs(args, texts);
							if (!texts.isEmpty() && isMatched(testName, texts)) {
								assert (lastArg != null);
								range = new SourceRange(call.sourceStart(),
										lastArg.sourceEnd()
												- call.sourceStart());
							}
						}
					}
				}
			}
			return super.visitGeneral(node);
		}

		private boolean isMatchingContext() {
			if (!states.isEmpty()) {
				final State state = (State) states.peek();
				return state.isMatched;
			}
			return false;
		}

		public void endvisitGeneral(ASTNode node) throws Exception {
			if (!states.isEmpty()) {
				final State state = (State) states.peek();
				if (state.callNode == node) {
					states.pop();
				}
			}
			super.endvisitGeneral(node);
		}
	}

	private static class MethodRequestor extends SearchRequestor {

		final Set resources = new HashSet();

		public void acceptSearchMatch(SearchMatch match) throws CoreException {
			if (match.getResource() != null) {
				resources.add(match.getResource());
			}
		}

	}

	protected TestElementResolution resolveTestSuite(ITestSuiteElement element) {
		final ITestElement[] children = element.getChildren();
		final Set locations = new HashSet();
		for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof ITestCaseElement) {
				final ITestCaseElement caseElement = (ITestCaseElement) children[i];
				final String testName = caseElement.getTestName();
				final int index = testName.lastIndexOf(PATH_BEGIN);
				if (index > 0) {
					final String location = testName.substring(index + 1);
					final Matcher matcher = STACK_FRAME_PATTERN
							.matcher(location);
					if (matcher.matches()) {
						locations.add(matcher.group(1));
					}
				}
			}
		}
		final Set processedResources = new HashSet();
		final RSpecContextLocator locator = new RSpecContextLocator(element
				.getSuiteTypeName());
		for (Iterator i = locations.iterator(); i.hasNext();) {
			final ISourceModule module = findSourceModule((String) i.next());
			if (module != null) {
				if (module.getResource() != null) {
					processedResources.add(module.getResource());
				}
				locator.process(module);
				if (locator.range != null) {
					return new TestElementResolution(module, locator.range);
				}
			}
		}
		final IDLTKSearchScope scope = getSearchScope();
		TestElementResolution resolution;
		resolution = searchMethodReferences(scope, locator,
				RSpecUtils.DESCRIBE, processedResources);
		if (resolution != null) {
			return resolution;
		}
		resolution = searchMethodReferences(scope, locator, RSpecUtils.CONTEXT,
				processedResources);
		if (resolution != null) {
			return resolution;
		}
		return null;
	}

	private TestElementResolution searchMethodReferences(
			final IDLTKSearchScope scope, final RSpecContextLocator locator,
			final String methodName, final Set processedResources) {
		final Set describeReferences = findMethodReferences(scope, methodName);
		describeReferences.removeAll(processedResources);
		for (Iterator i = describeReferences.iterator(); i.hasNext();) {
			final IFile file = (IFile) i.next();
			processedResources.add(file);
			final ISourceModule module = (ISourceModule) DLTKCore.create(file);
			if (module != null) {
				locator.process(module);
				if (locator.range != null) {
					return new TestElementResolution(module, locator.range);
				}
			}
		}
		return null;
	}

	private Set findMethodReferences(final IDLTKSearchScope scope,
			final String methodName) {
		final SearchPattern pattern = SearchPattern.createPattern(methodName,
				IDLTKSearchConstants.METHOD, IDLTKSearchConstants.REFERENCES,
				SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
				scope.getLanguageToolkit());
		final MethodRequestor requestor = new MethodRequestor();
		try {
			new SearchEngine().search(pattern,
					new SearchParticipant[] { SearchEngine
							.getDefaultSearchParticipant() }, scope, requestor,
					null);
		} catch (CoreException e) {
			final String msg = "Error in search method references {0})"; //$NON-NLS-1$
			RubyTestingPlugin.error(NLS.bind(msg, methodName), e);
		}
		final Set resources = requestor.resources;
		return resources;
	}

	protected TestElementResolution resolveTestCase(ITestCaseElement element) {
		if (!(element.getParentContainer() instanceof ITestSuiteElement)) {
			return null;
		}
		final String testName = element.getTestName();
		final int index = testName.lastIndexOf(PATH_BEGIN);
		if (index < 0) {
			return null;
		}
		final String location = testName.substring(index + 1);
		final Matcher matcher = STACK_FRAME_PATTERN.matcher(location);
		if (!matcher.matches()) {
			return null;
		}
		final ISourceModule module = findSourceModule(matcher.group(1));
		if (module == null) {
			return null;
		}
		final RSpecTestLocator locator = new RSpecTestLocator(
				((ITestSuiteElement) element.getParentContainer())
						.getSuiteTypeName(), testName.substring(0, index));
		locator.process(module);
		if (locator.range != null) {
			return new TestElementResolution(module, locator.range);
		}
		final String source;
		try {
			source = module.getSource();
		} catch (ModelException e) {
			return null;
		}
		final ILineTracker lineTracker = new DefaultLineTracker();
		lineTracker.set(source);
		final int lineNumber;
		try {
			lineNumber = Integer.parseInt(matcher.group(2));
		} catch (NumberFormatException e) {
			return null;
		}
		org.eclipse.jface.text.IRegion line;
		try {
			line = lineTracker.getLineInformation(lineNumber - 1);
		} catch (BadLocationException e) {
			return null;
		}
		return new TestElementResolution(module, ResolverUtils.adjustRange(
				source, line.getOffset(), line.getOffset() + line.getLength()));
	}

	private ISourceModule findSourceModule(String path) {
		final Object result = RubyFileHyperlink.findSourceModule(path);
		if (result instanceof ISourceModule) {
			return (ISourceModule) result;
		}
		if (result instanceof IFile) {
			IModelElement element = DLTKCore.create((IFile) result);
			if (element instanceof ISourceModule) {
				return (ISourceModule) element;
			}
		}
		return null;
	}

	private static String buildRegex() {
		final String slash = "[\\\\/]"; //$NON-NLS-1$
		return slash + "gems" + slash + "rspec-[\\w\\.]+" + slash + "lib" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ slash;
	}

	private static final Pattern GEM_RSPEC_LIB = Pattern.compile(buildRegex());

	protected boolean selectLine(String line) {
		final String filename = extractFileName(line);
		if (filename == null) {
			return true;
		}
		if (filename.endsWith(RspecTestingEngine.RSPEC_RUNNER)) {
			return false;
		}
		if (GEM_RSPEC_LIB.matcher(filename).find()) {
			return false;
		}
		return true;
	}

	/*
	 * @see org.eclipse.dltk.testing.AbstractTestRunnerUI#canRerunFailures()
	 */
	public boolean canRerunFailures() {
		return true;
	}

	/*
	 * @see AbstractTestRunnerUI#collectFailures(ITestElement[])
	 */
	public String collectFailures(ITestRunSession testRunSession)
			throws CoreException {
		try {
			final File file = File.createTempFile("rspecTestFailures", ".txt"); //$NON-NLS-1$ //$NON-NLS-2$
			file.deleteOnExit();
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(file));
				final ITestElement[] failures = testRunSession
						.getFailedTestElements(new ITestElementPredicate() {
							public boolean matches(ITestElement testElement) {
								return testElement instanceof ITestCaseElement;
							}
						});
				for (int i = 0; i < failures.length; i++) {
					final ITestElement failure = failures[i];
					if (failure instanceof ITestCaseElement
							&& failure.getParentContainer() instanceof ITestSuiteElement) {
						final ITestSuiteElement suite = (ITestSuiteElement) failure
								.getParentContainer();
						final String exampleName = suite.getSuiteTypeName()
								+ " " //$NON-NLS-1$
								+ getTestCaseLabel((ITestCaseElement) failure,
										false);
						bw.write(exampleName);
						bw.newLine();
						// TODO handle "automatic" example names
					}
				}
			} finally {
				if (bw != null) {
					bw.close();
				}
			}
			return file.getAbsolutePath();
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR,
					DLTKTestingPlugin.PLUGIN_ID, IStatus.ERROR, "", e)); //$NON-NLS-1$
		}
	}
}
