/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.launching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.launching.model.InterpreterGeneratedContent;
import org.eclipse.dltk.launching.model.LaunchingModel;
import org.eclipse.dltk.launching.model.LaunchingModelFactory;
import org.eclipse.dltk.launching.model.util.GeneratedContentPredicate;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.launching.RubyLaunchingPlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

public class RubyGenericInstall extends AbstractInterpreterInstall {

	public class BuiltinsHelper {

		private static final int CACHE_LIFETIME = 24 * 60 * 60 * 1000;

		private static final String SCRIPT_NAME = "scripts/builtin.rb"; //$NON-NLS-1$

		private static final String PREFIX = "#### DLTK RUBY BUILTINS ####"; //$NON-NLS-1$

		private Map<String, String> sources;

		private List<String> generateLines() throws IOException, CoreException {
			IExecutionEnvironment exeEnv = getExecEnvironment();
			IDeployment deployment = exeEnv.createDeployment();
			if (deployment == null) {
				return null;
			}
			final IPath builder = deployment.add(RubyLaunchingPlugin
					.getDefault().getBundle(), SCRIPT_NAME);

			final List<String> lines = new ArrayList<String>();

			IFileHandle builderFile = deployment.getFile(builder);
			InterpreterConfig config = ScriptLaunchUtil
					.createInterpreterConfig(exeEnv, builderFile, builderFile
							.getParent());
			config.removeEnvVar("RUBYOPT"); //$NON-NLS-1$
			// config.addInterpreterArg("-KU"); //$NON-NLS-1$
			final Process process = ScriptLaunchUtil.runScriptWithInterpreter(
					exeEnv, RubyGenericInstall.this.getInstallLocation()
							.toOSString(), config);

			Thread readerThread = new Thread(new Runnable() {
				public void run() {
					BufferedReader input = null;
					try {
						input = new BufferedReader(new InputStreamReader(
								process.getInputStream()));

						String line = null;
						try {
							while ((line = input.readLine()) != null) {
								lines.add(line);
							}
						} catch (IOException e) {
							if (DLTKCore.DEBUG) {
								e.printStackTrace();
							}
						}

					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (IOException e) {
								if (DLTKCore.DEBUG) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			});
			try {
				readerThread.start();
				readerThread.join(10000);
			} catch (InterruptedException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			deployment.dispose();
			return lines;
		}

		private void parseLines(List<String> lines) {
			String fileName = null;
			StringBuffer sb = new StringBuffer();
			for (String line : lines) {
				int index = line.indexOf(PREFIX);
				if (index != -1) {
					if (fileName != null) {
						String old = sources.get(fileName);
						if (old == null)
							sources.put(fileName, sb.toString());
						else
							sources.put(fileName, old + "\n\n" + sb.toString()); //$NON-NLS-1$
						sb.setLength(0);
					}

					fileName = line.substring(index + PREFIX.length());

				} else {
					sb.append(line);
					sb.append("\n"); //$NON-NLS-1$
				}
			}
		}

		public synchronized Map<String, String> getSources() {
			if (sources == null) {
				sources = new HashMap<String, String>();
				load();
			}
			return sources;
		}

		private void load() {
			InterpreterGeneratedContent content = (InterpreterGeneratedContent) LaunchingModel
					.getInstance().find(RubyGenericInstall.this,
							new GeneratedContentPredicate(SCRIPT_NAME));
			if (content != null
					&& content.getValue() != null
					&& content.getLastModified() != null
					&& content.getInterpreterLastModified() != null
					&& content.getInterpreterLastModified().getTime() == getInstallLocation()
							.lastModified()) {
				if (content.getFetchedAt() != null
						&& content.getFetchedAt().getTime() + CACHE_LIFETIME > System
								.currentTimeMillis()) {
					parseLines(content.getValue());
					lastModified = content.getLastModified().getTime();
					return;
				}
			} else {
				content = null;
			}

			try {
				final List<String> lines = generateLines();
				if (lines != null) {
					parseLines(lines);
					if (content != null) {
						content = (InterpreterGeneratedContent) EcoreUtil
								.copy(content);
						content.setFetchedAt(new Date());
						if (!lines.equals(content.getValue())) {
							content.getValue().clear();
							content.getValue().addAll(lines);
							content.setLastModified(content.getFetchedAt());
						}
					} else {
						content = LaunchingModelFactory.eINSTANCE
								.createInterpreterGeneratedContent();
						content.setKey(SCRIPT_NAME);
						content.setFetchedAt(new Date());
						content.setLastModified(content.getFetchedAt());
						content.getValue().clear();
						content.getValue().addAll(lines);
						content.setInterpreterLastModified(new Date(
								getInstallLocation().lastModified()));
					}
					LaunchingModel.getInstance()
							.save(RubyGenericInstall.this,
									new GeneratedContentPredicate(SCRIPT_NAME),
									content);
					lastModified = content.getLastModified().getTime();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

		long lastModified;
	}

	private BuiltinsHelper helper = new BuiltinsHelper();

	public RubyGenericInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}

	public IInterpreterRunner getInterpreterRunner(String mode) {
		final IInterpreterRunner runner = super.getInterpreterRunner(mode);

		if (runner != null) {
			return runner;
		}

		if (mode.equals(ILaunchManager.RUN_MODE)) {
			return new RubyInterpreterRunner(this);
		}

		return null;
	}

	public String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	// Builtins
	public String getBuiltinModuleContent(String name) {
		final Map<String, String> sources = helper.getSources();
		return sources.get(name);
	}

	public long lastModified() {
		helper.getSources();
		return helper.lastModified;
	}

	public String[] getBuiltinModules() {
		final Map<String, String> sources = helper.getSources();
		return sources.keySet().toArray(new String[sources.size()]);
	}
}