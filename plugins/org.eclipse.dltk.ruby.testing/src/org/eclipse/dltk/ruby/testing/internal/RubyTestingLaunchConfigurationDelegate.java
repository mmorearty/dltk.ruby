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

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.ruby.launching.RubyLaunchConfigurationDelegate;
import org.eclipse.dltk.ruby.testing.IRubyTestingEngine;
import org.eclipse.dltk.testing.DLTKTestingCore;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.DLTKTestingPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class RubyTestingLaunchConfigurationDelegate extends
		RubyLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	private static final String RUBY_TESTING_PORT = "RUBY_TESTING_PORT"; //$NON-NLS-1$

	private IRubyTestingEngine engine;

	protected InterpreterConfig createInterpreterConfig(
			ILaunchConfiguration configuration, ILaunch launch)
			throws CoreException {
		// We need to create correct execute script for this element.
		InterpreterConfig config = super.createInterpreterConfig(configuration,
				launch);
		IRubyTestingEngine[] engines = RubyTestingEngineManager.getEngines();
		String engineId = configuration.getAttribute(
				DLTKTestingConstants.ATTR_ENGINE_ID, Util.EMPTY_STRING);
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

	private static final String TEST_UNIT_RUNNER = "dltk-testunit-runner.rb"; //$NON-NLS-1$

	private static final String TEST_UNIT_RUNNER_FULL_PATH = "/testing/" + TEST_UNIT_RUNNER; //$NON-NLS-1$

	protected void runRunner(ILaunchConfiguration configuration,
			IInterpreterRunner runner, InterpreterConfig config,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		if (engine != null) {
			DLTKTestingCore.registerTestingProcessor(launch, engine
					.getProcessor(launch));
		}
		// initialize testing model
		DLTKTestingPlugin.getModel().start();
		final String strPort = String.valueOf(evaluatePort());
		launch.setAttribute(DLTKTestingConstants.ATTR_PORT,
				strPort);
		config.addEnvVar(RUBY_TESTING_PORT, strPort);
		if (config.getEnvironment().isLocal() && !isDevelopmentMode(config)) {
			URL runnerScript = Activator.getDefault().getBundle().getEntry(
					TEST_UNIT_RUNNER_FULL_PATH);
			if (runnerScript == null) {
				throw new CoreException(
						new Status(IStatus.ERROR, Activator.PLUGIN_ID,
								TEST_UNIT_RUNNER + " is not found"));
			}
			try {
				runnerScript = FileLocator.toFileURL(runnerScript);
				config.addInterpreterArg("-r"); //$NON-NLS-1$
				config.addInterpreterArg(new File(new URI(runnerScript
						.toString())).toString());
			} catch (IOException e) {
				throw new CoreException(new Status(IStatus.ERROR,
						Activator.PLUGIN_ID,
						TEST_UNIT_RUNNER + " is not found", e));
			} catch (URISyntaxException e) {
				throw new CoreException(new Status(IStatus.ERROR,
						Activator.PLUGIN_ID,
						TEST_UNIT_RUNNER + " is not found", e));
			}
		}
		super.runRunner(configuration, runner, config, launch, monitor);
	}

	private boolean isDevelopmentMode(InterpreterConfig config) {
		return config.getScriptFilePath() != null
				&& config.getScriptFilePath().lastSegment().equals(
						TEST_UNIT_RUNNER);
	}

	/**
	 * Returns a free port number on localhost, or -1 if unable to find a free
	 * port.
	 * 
	 * @return a free port number or -1
	 */
	private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;
	}

	private int evaluatePort() throws CoreException {
		int port = findFreePort();
		if (port == -1) {
			informAndAbort(
					DLTKTestingMessages.JUnitLaunchConfigurationDelegate_error_no_socket,
					null,
					ScriptLaunchConfigurationConstants.ERR_NO_SOCKET_AVAILABLE);
		}
		return port;
	}

	private void informAndAbort(String message, Throwable exception, int code)
			throws CoreException {
		IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, code,
				message, exception);
		if (showStatusMessage(status)) {
			// Status message successfully shown
			// -> Abort with INFO exception
			// -> Worker.run() will not write to log
			throw new CoreException(status);
		} else {
			// Status message could not be shown
			// -> Abort with original exception
			// -> Will write WARNINGs and ERRORs to log
			abort(message, exception, code);
		}
	}

	private boolean showStatusMessage(final IStatus status) {
		final boolean[] success = new boolean[] { false };
		getDisplay().syncExec(new Runnable() {
			public void run() {
				Shell shell = DLTKTestingPlugin.getActiveWorkbenchShell();
				if (shell == null)
					shell = getDisplay().getActiveShell();
				if (shell != null) {
					MessageDialog
							.openInformation(
									shell,
									DLTKTestingMessages.JUnitLaunchConfigurationDelegate_dialog_title,
									status.getMessage());
					success[0] = true;
				}
			}
		});
		return success[0];
	}

	private Display getDisplay() {
		Display display;
		display = Display.getCurrent();
		if (display == null)
			display = Display.getDefault();
		return display;
	}

}
