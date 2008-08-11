/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.launching;

import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.internal.launching.InterpreterMessages;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.launching.RubyLaunchingPlugin;

public class RubyGenericInstallType extends AbstractInterpreterInstallType {
	private static final String CORRECT_INTERPRETER_PATTERN = "#DLTK INTERPRETER TEST:5";

	private static final String INSTALL_TYPE_NAME = "Generic Ruby"; //$NON-NLS-1$

	private static final String[] INTERPRETER_NAMES = {
			"ruby", "rubyw", "ruby18", "ruby19" }; //$NON-NLS-1$ //$NON-NLS-2$

	public String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	public String getName() {
		return INSTALL_TYPE_NAME;
	}

	protected String getPluginId() {
		return RubyLaunchingPlugin.PLUGIN_ID;
	}

	protected String[] getPossibleInterpreterNames() {
		return INTERPRETER_NAMES;
	}

	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new RubyGenericInstall(this, id);
	}

	protected IPath createPathFile(IDeployment deployment) throws IOException {
		return deployment.add(RubyLaunchingPlugin.getDefault().getBundle(),
				"scripts/path.rb"); //$NON-NLS-1$
	}

	public IStatus validateInstallLocation(IFileHandle installLocation) {
		if (!installLocation.exists() || !installLocation.isFile()) {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNonExistentOrInvalidInstallLocation,
					null);
		}
		IEnvironment environment = installLocation.getEnvironment();
		IExecutionEnvironment executionEnvironment = (IExecutionEnvironment) environment
				.getAdapter(IExecutionEnvironment.class);

		String output = ScriptLaunchUtil.runEmbeddedScriptReadContent(
				executionEnvironment, "scripts/test.rb", RubyLaunchingPlugin
						.getDefault().getBundle(), installLocation,
				new NullProgressMonitor());
		String[] lines = output.split("\\n");
		boolean correct = false;
		for (int i = 0; i < lines.length; i++) {
			if (CORRECT_INTERPRETER_PATTERN.equals(lines[i])) {
				correct = true;
			}
		}

		if (correct) {
			return createStatus(IStatus.OK, "", null); //$NON-NLS-1$
		} else {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNoInterpreterExecutablesFound, null);
		}
	}

	protected String getBuildPathDelimeter() {
		return ";:"; //$NON-NLS-1$
	}

	protected ILog getLog() {
		return RubyLaunchingPlugin.getDefault().getLog();
	}
}
