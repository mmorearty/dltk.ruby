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

import java.util.Stack;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.AbstractTestingEngineValidateVisitor;
import org.eclipse.dltk.ruby.testing.internal.Messages;
import org.eclipse.dltk.ruby.testing.internal.ResolverUtils;
import org.eclipse.dltk.ruby.testing.internal.RubyTestingLaunchConfigurationDelegate;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.osgi.util.NLS;

public class RspecTestingEngine extends AbstractRubyTestingEngine {

	static class RSpecValidateVisitor extends
			AbstractTestingEngineValidateVisitor {

		private static final String RSPEC = "spec"; //$NON-NLS-1$

		private int weight = 0;

		static final int REQUIRE_WEIGHT = 9;
		static final int TEST_WEIGHT = 3;

		public boolean visitGeneral(ASTNode node) throws Exception {
			if (node instanceof CallExpression) {
				final CallExpression call = (CallExpression) node;
				if (isRequire(call, RSPEC)) {
					weight += REQUIRE_WEIGHT;
				} else if (isMethodCall(call, RSpecUtils.CONTEXT_METHODS)
						|| isMethodCall(call, RSpecUtils.SHARED_GROUP)) {
					contextCalls.push(node);
				} else if (!contextCalls.isEmpty()
						&& (isMethodCall(call, RSpecUtils.TEST_METHODS) || isMethodCall(
								call, RSpecUtils.TEST_SHARED))) {
					weight += TEST_WEIGHT;
				}
			}
			return super.visitGeneral(node);
		}

		public void endvisitGeneral(ASTNode node) throws Exception {
			if (!contextCalls.isEmpty()) {
				if (contextCalls.peek() == node) {
					contextCalls.pop();
				}
			}
			super.endvisitGeneral(node);
		}

		private final Stack contextCalls = new Stack();

		public IStatus getStatus() {
			if (weight >= REQUIRE_WEIGHT + TEST_WEIGHT) {
				return Status.OK_STATUS;
			}
			if (weight >= TEST_WEIGHT) {
				return createStatus(IStatus.INFO,
						Messages.validate_probablyRSpec);
			}
			return createStatus(IStatus.WARNING, Messages.validate_notRSpec);
		}
	}

	public IStatus validateSourceModule(ISourceModule module) {
		final ModuleDeclaration declaration = ResolverUtils.parse(module);
		if (declaration == null) {
			return createStatus(IStatus.WARNING, Messages.validate_sourceErrors);
		}
		final RSpecValidateVisitor visitor = new RSpecValidateVisitor();
		try {
			declaration.traverse(visitor);
		} catch (Exception e) {
			return createStatus(IStatus.WARNING, NLS.bind(
					Messages.validate_runtimeError, e.getMessage()));
		}
		return visitor.getStatus();
	}

	static final String RSPEC_RUNNER = "dltk-rspec-runner.rb"; //$NON-NLS-1$

	public String getMainScriptPath(ILaunchConfiguration configuration,
			IEnvironment scriptEnvironment) throws CoreException {
		return getRunnerFile(getBundle(), RUNNER_PATH, RSPEC_RUNNER).getPath();
	}

	public void configureLaunch(InterpreterConfig config,
			ILaunchConfiguration configuration, ILaunch launch)
			throws CoreException {
		// select port number
		final String strPort = String.valueOf(allocatePort());
		launch.setAttribute(DLTKTestingConstants.ATTR_PORT, strPort);
		config.addEnvVar(RUBY_TESTING_PORT, strPort);
		final String failureNames = configuration.getAttribute(
				DLTKTestingConstants.ATTR_FAILURES_NAMES, Util.EMPTY_STRING);
		if (failureNames.length() != 0) {
			config.addScriptArg("-e"); //$NON-NLS-1$
			config.addScriptArg(failureNames);
		}
		if (!RubyTestingLaunchConfigurationDelegate
				.isContainerMode(configuration)) {
			final String mainScript = AbstractScriptLaunchConfigurationDelegate
					.getMainScriptName(configuration);
			config.addScriptArg(mainScript);
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
				config.addScriptArg("."); //$NON-NLS-1$
			} else {
				config.addScriptArg(path.toOSString());
			}
		}
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestingEngine#getTestRunnerUI()
	 */
	public ITestRunnerUI getTestRunnerUI(IScriptProject project,
			ILaunchConfiguration configuration) {
		return new RSpecTestRunnerUI(this, project);
	}

}
