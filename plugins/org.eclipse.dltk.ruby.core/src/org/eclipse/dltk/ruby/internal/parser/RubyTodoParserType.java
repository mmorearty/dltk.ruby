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
package org.eclipse.dltk.ruby.internal.parser;

import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.validators.core.AbstractBuildParticipantType;
import org.eclipse.dltk.validators.core.IBuildParticipant;

public class RubyTodoParserType extends AbstractBuildParticipantType {

	private static final String ID = "org.eclipse.dltk.ruby.todo"; //$NON-NLS-1$
	private static final String NAME = "Ruby TODO task parser"; //$NON-NLS-1$

	public RubyTodoParserType() {
		super(ID, NAME);
	}

	protected IBuildParticipant createBuildParticipant(IScriptProject project) {
		final ITodoTaskPreferences prefs = new TodoTaskPreferences(RubyPlugin
				.getDefault().getPluginPreferences());
		if (prefs.isEnabled()) {
			final RubyTodoTaskAstParser parser = new RubyTodoTaskAstParser(
					prefs);
			if (parser.isValid()) {
				return parser;
			}
		}
		return null;
	}

}
