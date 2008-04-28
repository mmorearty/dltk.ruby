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

import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;

public class RubyFoldingPreferencePage extends
		AbstractConfigurationBlockPreferencePage {

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new RubyFoldingConfigurationBlock(overlayPreferenceStore, this);
	}

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		setDescription(RubyPreferencesMessages.EditorFoldingPreferencePageDescription);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(RubyUI.getDefault().getPreferenceStore());
	}

}
