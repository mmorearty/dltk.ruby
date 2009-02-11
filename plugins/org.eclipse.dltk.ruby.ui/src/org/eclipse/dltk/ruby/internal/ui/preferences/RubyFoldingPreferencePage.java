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
import org.eclipse.dltk.ruby.internal.ui.text.folding.RubyDocFoldingPreferenceBlock;
import org.eclipse.dltk.ruby.internal.ui.text.folding.RubyFoldingPreferenceBlock;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.DefaultFoldingPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.text.folding.IFoldingPreferenceBlock;
import org.eclipse.jface.preference.PreferencePage;

public class RubyFoldingPreferencePage extends
		AbstractConfigurationBlockPreferencePage {

	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		return new DefaultFoldingPreferenceConfigurationBlock(
				overlayPreferenceStore, this) {

			protected IFoldingPreferenceBlock createDocumentationBlock(
					OverlayPreferenceStore store, PreferencePage page) {
				return new RubyDocFoldingPreferenceBlock(store, page);
			}

			protected IFoldingPreferenceBlock createSourceCodeBlock(
					OverlayPreferenceStore store, PreferencePage page) {
				return new RubyFoldingPreferenceBlock(store, page);
			}
		};
	}

	protected String getHelpId() {
		return null;
	}

	protected void setDescription() {
		// setDescription(RubyPreferencesMessages.EditorFoldingPreferencePageDescription);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(RubyUI.getDefault().getPreferenceStore());
	}

}
