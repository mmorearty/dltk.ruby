/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *     xored software, Inc. - todo task parser added (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.compiler.task.ITaskReporter;
import org.eclipse.dltk.compiler.task.TodoTaskAstParser;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.ruby.internal.parser.visitors.RubySourceElementRequestor;

public class RubySourceElementParser extends AbstractSourceElementParser {

	/*
	 * @see org.eclipse.dltk.core.AbstractSourceElementParser#createVisitor()
	 */
	protected SourceElementRequestVisitor createVisitor() {
		return new RubySourceElementRequestor(getRequestor());
	}

	/*
	 * @see org.eclipse.dltk.core.AbstractSourceElementParser#getNatureId()
	 */
	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	public void parseSourceModule(char[] contents, ISourceModuleInfo astCache,
			char[] filename) {
		ModuleDeclaration moduleDeclaration = SourceParserUtil
				.getModuleDeclaration(filename, contents, getNatureId(),
						getProblemReporter(), astCache);
		final SourceElementRequestVisitor requestor = createVisitor();
		try {
			moduleDeclaration.traverse(requestor);
		} catch (Exception e) {
			DLTKCore.error("Unexpected error", e); //$NON-NLS-1$
		}
		if (getProblemReporter() != null) {
			final ITaskReporter taskReporter = (ITaskReporter) getProblemReporter()
					.getAdapter(ITaskReporter.class);
			if (taskReporter != null) {
				taskReporter.clearTasks();
				parseTasks(taskReporter, contents, moduleDeclaration);
			}
		}
	}

	protected void parseTasks(ITaskReporter taskReporter, char[] content,
			ModuleDeclaration moduleDeclaration) {
		final TodoTaskPreferences preferences = new TodoTaskPreferences(
				RubyPlugin.getDefault().getPluginPreferences());
		if (preferences.isEnabled()) {
			final TodoTaskAstParser taskParser = new TodoTaskAstParser(
					taskReporter, preferences, moduleDeclaration);
			if (taskParser.isValid()) {
				taskParser.parse(content);
			}
		}
	}

}
