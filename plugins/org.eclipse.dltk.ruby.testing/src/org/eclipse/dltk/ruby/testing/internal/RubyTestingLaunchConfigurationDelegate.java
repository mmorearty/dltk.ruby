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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.ruby.launching.RubyLaunchConfigurationDelegate;
import org.eclipse.dltk.ruby.testing.ITestingEngine;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.DLTKTestingPlugin;

public class RubyTestingLaunchConfigurationDelegate extends
		RubyLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	static boolean isContainerMode(ILaunchConfiguration configuration)
			throws CoreException {
		final String containerHandle = configuration.getAttribute(
				DLTKTestingConstants.ATTR_TEST_CONTAINER, Util.EMPTY_STRING);
		return containerHandle.length() > 0;
	}

	protected void validateLaunchConfiguration(
			ILaunchConfiguration configuration, String mode, IProject project)
			throws CoreException {
		super.validateLaunchConfiguration(configuration, mode, project);
		final ITestingEngine engine = getTestingEngine(configuration);
		if (engine == null) {
			abort(DLTKTestingMessages.TestingNoEngineConfigured, null,
					ScriptLaunchConfigurationConstants.ERR_NO_TESTING_ENGINE);
		}
		if (isContainerMode(configuration)) {
			final String containerHandle = configuration
					.getAttribute(DLTKTestingConstants.ATTR_TEST_CONTAINER,
							Util.EMPTY_STRING);
			Assert.isLegal(containerHandle.length() != 0);
			final IModelElement containerElement = DLTKCore
					.create(containerHandle);
			if (containerElement == null) {
				abort(
						DLTKTestingMessages.JUnitLaunchConfigurationTab_error_noContainer,
						null,
						ScriptLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_SCRIPT);
			}
		}
	}

	protected String getScriptLaunchPath(ILaunchConfiguration configuration,
			IEnvironment scriptEnvironment) throws CoreException {
		if (isContainerMode(configuration)) {
			final ITestingEngine engine = getTestingEngine(configuration);
			return engine
					.getContainerLauncher(configuration, scriptEnvironment);
		} else {
			return super.getScriptLaunchPath(configuration, scriptEnvironment);
		}
	}

	protected IPath getDefaultWorkingDirectory(
			ILaunchConfiguration configuration) throws CoreException {
		if (isContainerMode(configuration)) {
			return new Path(getProjectLocation(configuration));
		} else {
			return super.getDefaultWorkingDirectory(configuration);
		}
	}

	protected void runRunner(ILaunchConfiguration configuration,
			IInterpreterRunner runner, InterpreterConfig config,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// initialize testing model
		DLTKTestingPlugin.getModel().start();
		final ITestingEngine engine = getTestingEngine(configuration);
		engine.configureLaunch(config, configuration, launch);
		super.runRunner(configuration, runner, config, launch, monitor);
	}

	private ITestingEngine getTestingEngine(ILaunchConfiguration configuration)
			throws CoreException {
		return TestingEngineManager.getEngine(configuration.getAttribute(
				DLTKTestingConstants.ATTR_ENGINE_ID, (String) null));
	}

}
