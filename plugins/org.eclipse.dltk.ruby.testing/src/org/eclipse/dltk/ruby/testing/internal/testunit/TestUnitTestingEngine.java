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

import java.io.File;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.ruby.ast.RubyClassDeclaration;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.Messages;
import org.eclipse.dltk.ruby.testing.internal.ResolverUtils;
import org.eclipse.dltk.ruby.testing.internal.RubyTestingLaunchConfigurationDelegate;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.osgi.util.NLS;

public class TestUnitTestingEngine extends AbstractRubyTestingEngine {

	static class TestUnitValidateVisitor extends
			AbstractTestingEngineValidateVisitor {

		private static final String TEST_UNIT = "test/unit"; //$NON-NLS-1$
		private static final String SHOULDA = "shoulda"; //$NON-NLS-1$
		private static final String TEST_UNIT_TEST_CASE = "Test::Unit::TestCase"; //$NON-NLS-1$
		private static final String TEST = "test"; //$NON-NLS-1$

		private ISourceModule module;
		private int testUnitWeight = 0;
		private int shouldaWeight = 0;

		static final int REQUIRE_WEIGHT = 10;
		static final int TESTCASE_WEIGHT = 10;
		static final int METHOD_WEIGHT = 1;

		public TestUnitValidateVisitor(ISourceModule module) {
			this.module = module;
		}

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (node instanceof CallExpression) {
				final CallExpression call = (CallExpression) node;
				if (isRequire(call, TEST_UNIT)) {
					testUnitWeight += REQUIRE_WEIGHT;
				} else if (isRequire(call, SHOULDA)) {
					shouldaWeight += REQUIRE_WEIGHT;
				} else if (isMethodCall(call, ShouldaUtils.METHODS)) {
					shouldaWeight += METHOD_WEIGHT;
				}
			} else if (node instanceof RubyClassDeclaration) {
				if (isSuperClassOf(module, (RubyClassDeclaration) node,
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
		final ModuleDeclaration declaration = ResolverUtils.parse(module);
		if (declaration == null) {
			return createStatus(IStatus.WARNING, Messages.validate_sourceErrors);
		}
		final TestUnitValidateVisitor visitor = new TestUnitValidateVisitor(module);
		try {
			declaration.traverse(visitor);
		} catch (Exception e) {
			return createStatus(IStatus.WARNING, NLS.bind(
					Messages.validate_runtimeError, e.getMessage()));
		}
		return visitor.getStatus();
	}

	static final String TEST_UNIT_RUNNER = "dltk-testunit-runner.rb"; //$NON-NLS-1$

	public void configureLaunch(InterpreterConfig config,
			ILaunchConfiguration configuration, ILaunch launch)
			throws CoreException {
		// select port number
		final String strPort = String.valueOf(allocatePort());
		launch.setAttribute(DLTKTestingConstants.ATTR_PORT, strPort);
		config.addEnvVar(RUBY_TESTING_PORT, strPort);
		// add runner
		if (!RubyTestingLaunchConfigurationDelegate
				.isContainerMode(configuration)) {
			if (config.getEnvironment().isLocal()) {
				final String runnerName = TEST_UNIT_RUNNER;
				if (!isDevelopmentMode(config, runnerName)) {
					final File runnerFile = getRunnerFile(getBundle(),
							RUNNER_PATH, runnerName);
					config.addInterpreterArg("-r"); //$NON-NLS-1$
					config.addInterpreterArg(runnerFile.getPath());
				}
			}
		} else {
			final String containerHandle = configuration
					.getAttribute(DLTKTestingConstants.ATTR_TEST_CONTAINER,
							Util.EMPTY_STRING);
			Assert.isLegal(containerHandle.length() != 0);
			IModelElement element = DLTKCore.create(containerHandle);
			Assert.isNotNull(element);
			IResource resource = element.getUnderlyingResource();
			Assert.isNotNull(resource);
			final IPath path = resource.getProjectRelativePath();
			if (path.isEmpty()) {
				config.addEnvVar(RUBY_TESTING_PATH, "."); //$NON-NLS-1$
			} else {
				config.addEnvVar(RUBY_TESTING_PATH, path.toOSString());
			}
		}
	}

	public String getMainScriptPath(ILaunchConfiguration configuration,
			IEnvironment scriptEnvironment) throws CoreException {
		if (RubyTestingLaunchConfigurationDelegate
				.isContainerMode(configuration)) {
			return getRunnerFile(getBundle(), RUNNER_PATH, TEST_UNIT_RUNNER)
					.getPath();
		} else {
			return null;
		}
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestingEngine#getTestRunnerUI()
	 */
	public ITestRunnerUI getTestRunnerUI(IScriptProject project,
			ILaunchConfiguration configuration) {
		return new TestUnitTestRunnerUI(this, project);
	}

}
