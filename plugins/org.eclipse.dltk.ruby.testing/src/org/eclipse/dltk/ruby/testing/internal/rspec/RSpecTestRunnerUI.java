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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IFile;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.references.ConstantReference;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.corext.SourceRange;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.ast.RubyColonExpression;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestRunnerUI;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.RubyTestingPlugin;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.TestElementResolution;
import org.eclipse.dltk.testing.model.ITestCaseElement;
import org.eclipse.dltk.testing.model.ITestElement;
import org.eclipse.dltk.testing.model.ITestSuiteElement;
import org.eclipse.osgi.util.NLS;

public class RSpecTestRunnerUI extends AbstractRubyTestRunnerUI {

	private static final char CLASS_BEGIN = '<';

	/**
	 * @param testingEngine
	 * @param project
	 */
	public RSpecTestRunnerUI(RspecTestingEngine testingEngine,
			IScriptProject project) {
		super(testingEngine, project);
	}

	public String filterStackTrace(String trace) {
		// TODO implement filtering
		return trace;
	}

	/*
	 * @see AbstractTestRunnerUI#getTestCaseLabel(ITestCaseElement, boolean)
	 */
	public String getTestCaseLabel(ITestCaseElement caseElement, boolean full) {
		final String testName = caseElement.getTestName();
		int index = testName.lastIndexOf(CLASS_BEGIN);
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
		int index = testName.lastIndexOf(CLASS_BEGIN);
		if (index >= 0) {
			final String template = DLTKTestingMessages.TestRunnerViewPart_message_started;
			return NLS.bind(template, testName.substring(index + 1), testName
					.substring(0, index));
		}
		return testName;
	}

	protected TestElementResolution resolveTestCase(ITestCaseElement element) {
		final String testName = element.getTestName();
		final int index = testName.lastIndexOf(CLASS_BEGIN);
		if (index < 0) {
			return null;
		}
		final String location = testName.substring(index);
		final Matcher matcher = STACK_FRAME_PATTERN.matcher(location);
		if (!matcher.matches()) {
			return null;
		}
		final ISourceModule module = findSourceModule(matcher.group(1));
		if (module == null) {
			return null;
		}

		// TODO Auto-generated method stub
		return null;
	}

	private static class RSpecContextLocator extends
			AbstractTestingEngineValidateVisitor {

		private final String contextName;

		private ISourceRange range = null;

		/**
		 * @param className
		 * @param shouldName
		 */
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
							ASTNode arg0 = null;
							for (Iterator i = args.getChilds().iterator(); i
									.hasNext();) {
								final Object arg = i.next();
								if (arg instanceof RubyCallArgument) {
									final ASTNode value = ((RubyCallArgument) arg)
											.getValue();
									final String text = toText(value);
									if (text != null) {
										texts.add(text);
										arg0 = value;
									}
								}
							}
							if (!texts.isEmpty()
									&& isMatched(contextName, texts)) {
								assert (arg0 != null);
								range = new SourceRange(call.sourceStart(),
										arg0.sourceEnd() - call.sourceStart());
							}
						}
					}
				}
			}
			return super.visitGeneral(node);
		}

		/**
		 * @param value
		 * @return
		 */
		private String toText(ASTNode value) {
			if (value instanceof StringLiteral) {
				return ((StringLiteral) value).getValue();
			}
			if (value instanceof ConstantReference) {
				return ((ConstantReference) value).getName();
			}
			if (value instanceof RubyColonExpression) {
				final StringBuffer sb = new StringBuffer();
				if (collectColonExpression((RubyColonExpression) value, sb)) {
					return sb.toString();
				}
			}
			return null;
		}

		/**
		 * @param value
		 * @param sb
		 */
		private boolean collectColonExpression(RubyColonExpression value,
				StringBuffer sb) {
			final ASTNode left = value.getLeft();
			if (left instanceof RubyColonExpression) {
				if (!collectColonExpression((RubyColonExpression) left, sb)) {
					return false;
				}
			} else if (left instanceof ConstantReference) {
				sb.append(((ConstantReference) left).getName());
			} else if (left != null) {
				final String msg = "Unexpected node in colon-expression " + left.getClass().getName(); //$NON-NLS-1$
				RubyTestingPlugin.error(msg, null);
				return false;
			}
			if (sb.length() != 0) {
				sb.append("::"); //$NON-NLS-1$
			}
			sb.append(value.getName());
			return true;
		}

		/**
		 * @param value
		 * @return
		 */
		private boolean isMatched(String value, List texts) {
			final StringBuffer sb = new StringBuffer();
			for (Iterator i = texts.iterator(); i.hasNext();) {
				if (sb.length() != 0) {
					sb.append(' ');
				}
				sb.append(i.next());
			}
			return value.equals(sb.toString());
		}

	}

	protected TestElementResolution resolveTestSuite(ITestSuiteElement element) {
		final ITestElement[] children = element.getChildren();
		final Set locations = new HashSet();
		for (int i = 0; i < children.length; ++i) {
			if (children[i] instanceof ITestCaseElement) {
				final ITestCaseElement caseElement = (ITestCaseElement) children[i];
				final String testName = caseElement.getTestName();
				final int index = testName.lastIndexOf(CLASS_BEGIN);
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
		final RSpecContextLocator locator = new RSpecContextLocator(element
				.getSuiteTypeName());
		for (Iterator i = locations.iterator(); i.hasNext();) {
			final ISourceModule module = findSourceModule((String) i.next());
			if (module != null) {
				final ModuleDeclaration declaration = SourceParserUtil
						.getModuleDeclaration(module);
				if (declaration != null
						&& !(declaration instanceof FakeModuleDeclaration)) {
					try {
						declaration.traverse(locator);
						if (locator.range != null) {
							return new TestElementResolution(module,
									locator.range);
						}
					} catch (Exception e) {
						RubyTestingPlugin.error("Error in resolveTestSuite", e); //$NON-NLS-1$
					}
				}
			}
		}
		return null;
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
}
