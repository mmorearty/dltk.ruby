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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.ruby.ast.RubyClassDeclaration;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.Messages;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.osgi.util.NLS;

public class TestUnitTestingEngine extends AbstractRubyTestingEngine {

	static class TestUnitValidateVisitor extends
			AbstractTestingEngineValidateVisitor {

		private static final String TEST_UNIT = "test/unit"; //$NON-NLS-1$
		private static final String SHOULDA = "shoulda"; //$NON-NLS-1$
		private static final String TEST_UNIT_TEST_CASE = "Test::Unit::TestCase"; //$NON-NLS-1$
		private static final String TEST = "test"; //$NON-NLS-1$

		private int testUnitWeight = 0;
		private int shouldaWeight = 0;

		static final int REQUIRE_WEIGHT = 10;
		static final int TESTCASE_WEIGHT = 10;
		static final int METHOD_WEIGHT = 1;

		private static final String[] SHOULDA_METHODS = { "context", //$NON-NLS-1$
				"should", //$NON-NLS-1$
				"should_eventually" //$NON-NLS-1$
		};

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (node instanceof CallExpression) {
				final CallExpression call = (CallExpression) node;
				if (isRequire(call, TEST_UNIT)) {
					testUnitWeight += REQUIRE_WEIGHT;
				} else if (isRequire(call, SHOULDA)) {
					shouldaWeight += REQUIRE_WEIGHT;
				} else if (isMethodCall(call, SHOULDA_METHODS)) {
					shouldaWeight += METHOD_WEIGHT;
				}
			} else if (node instanceof RubyClassDeclaration) {
				if (isSuperClassOf((RubyClassDeclaration) node,
						TEST_UNIT_TEST_CASE)) {
					testUnitWeight += TESTCASE_WEIGHT;
				}
			} else if (node instanceof MethodDeclaration) {
				if (isNodeOnStack(RubyClassDeclaration.class)
						&& isMethodPrefix((MethodDeclaration) node, TEST)) {
					testUnitWeight += METHOD_WEIGHT;
				}
			}
			return super.visitGeneral(node);
		}

		public IStatus getStatus() {
			if (testUnitWeight + shouldaWeight > Math.min(REQUIRE_WEIGHT,
					TESTCASE_WEIGHT)) {
				return Status.OK_STATUS;
			}
			if (testUnitWeight + shouldaWeight >= METHOD_WEIGHT) {
				return createStatus(IStatus.INFO,
						Messages.validate_probablyTestUnit);
			}
			return createStatus(IStatus.WARNING, Messages.validate_notTestUnit);
		}
	}

	public IStatus validateSourceModule(ISourceModule module) {
		final ModuleDeclaration declaration = SourceParserUtil
				.getModuleDeclaration(module);
		if (declaration == null || declaration instanceof FakeModuleDeclaration) {
			return createStatus(IStatus.WARNING, Messages.validate_sourceErrors);
		}
		final TestUnitValidateVisitor visitor = new TestUnitValidateVisitor();
		try {
			declaration.traverse(visitor);
		} catch (Exception e) {
			return createStatus(IStatus.WARNING, NLS.bind(
					Messages.validate_runtimeError, e.getMessage()));
		}
		return visitor.getStatus();
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestingEngine#getTestRunnerUI()
	 */
	public ITestRunnerUI getTestRunnerUI(IScriptProject project,
			ILaunchConfiguration configuration) {
		return new TestUnitTestRunnerUI(this, project);
	}

}
