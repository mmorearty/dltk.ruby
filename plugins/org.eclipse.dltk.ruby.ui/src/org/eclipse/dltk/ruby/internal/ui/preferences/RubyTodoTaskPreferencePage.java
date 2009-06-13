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
package org.eclipse.dltk.ruby.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.ui.PreferencesAdapter;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.TodoTaskOptionsBlock;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class RubyTodoTaskPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		setDescription(RubyPreferencesMessages.TodoTaskDescription);
	}

	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new TodoTaskOptionsBlock(newStatusChangedListener, project,
				container, RubyPlugin.PLUGIN_ID);
	}

	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	protected String getProjectHelpId() {
		return null;
	}

	protected void setPreferenceStore() {
		setPreferenceStore(new PreferencesAdapter(RubyPlugin.getDefault()
				.getPluginPreferences()));
	}

	protected String getPreferencePageId() {
		return "org.eclipse.dltk.ruby.preferences.todo"; //$NON-NLS-1$
	}

	protected String getPropertyPageId() {
		return "org.eclipse.dltk.ruby.propertyPage.todo"; //$NON-NLS-1$
	}

}
