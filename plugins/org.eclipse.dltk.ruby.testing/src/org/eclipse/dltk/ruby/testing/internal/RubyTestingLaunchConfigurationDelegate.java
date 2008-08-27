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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.ruby.launching.RubyLaunchConfigurationDelegate;
import org.eclipse.dltk.ruby.testing.ITestingEngine;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.DLTKTestingPlugin;

public class RubyTestingLaunchConfigurationDelegate extends
		RubyLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	protected void runRunner(ILaunchConfiguration configuration,
			IInterpreterRunner runner, InterpreterConfig config,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		// initialize testing model
		DLTKTestingPlugin.getModel().start();
		// identify engine
		final String engineId = configuration.getAttribute(
				DLTKTestingConstants.ATTR_ENGINE_ID, Util.EMPTY_STRING);
		final ITestingEngine engine = TestingEngineManager
				.getEngine(engineId);
		if (engine != null) {
			engine.run(config, configuration, launch);
		}
		super.runRunner(configuration, runner, config, launch, monitor);
	}

}
