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

import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.validators.core.AbstractTodoTaskBuildParticipantType;

public class RubyTodoParserType extends AbstractTodoTaskBuildParticipantType {

	private static final String ID = "org.eclipse.dltk.ruby.todo"; //$NON-NLS-1$
	private static final String NAME = "Ruby TODO task parser"; //$NON-NLS-1$

	public RubyTodoParserType() {
		super(ID, NAME);
	}

	protected Preferences getPreferences() {
		return RubyPlugin.getDefault().getPluginPreferences();
	}
}
