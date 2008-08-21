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
import org.eclipse.dltk.ruby.testing.IRubyTestingEngine;
import org.eclipse.dltk.testing.DLTKTestingCore;
import org.eclipse.dltk.testing.IDLTKTestingConstants;

public class RubyTestingLaunchConfigurationDelegate extends
		RubyLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {
	private IRubyTestingEngine engine;

	protected InterpreterConfig createInterpreterConfig(
			ILaunchConfiguration configuration, ILaunch launch)
			throws CoreException {
		// We need to create correct execute script for this element.
		InterpreterConfig config = super.createInterpreterConfig(configuration,
				launch);
		IRubyTestingEngine[] engines = RubyTestingEngineManager.getEngines();
		String engineId = configuration.getAttribute(
				IDLTKTestingConstants.ENGINE_ID_ATR, Util.EMPTY_STRING);
		for (int i = 0; i < engines.length; i++) {
			if (engines[i].getId().equals(engineId)) {
				engines[i].correctLaunchConfiguration(config, configuration,
						launch);
				this.engine = engines[i];
				break;
			}
		}
		return config;
	}

	protected void runRunner(ILaunchConfiguration configuration,
			IInterpreterRunner runner, InterpreterConfig config,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		if (engine != null) {
			DLTKTestingCore.registerTestingProcessor(launch, engine
					.getProcessor(launch));
		}

		super.runRunner(configuration, runner, config, launch, monitor);
	}
}
