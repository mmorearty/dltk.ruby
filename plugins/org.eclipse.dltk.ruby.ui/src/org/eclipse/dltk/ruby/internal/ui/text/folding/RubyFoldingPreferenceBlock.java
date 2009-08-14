/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text.folding;

import java.util.List;

import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore.OverlayKey;
import org.eclipse.dltk.ui.text.folding.SourceCodeFoldingPreferenceBlock;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Group;

/**
 * Ruby source code folding preferences.
 */
public class RubyFoldingPreferenceBlock extends
		SourceCodeFoldingPreferenceBlock {

	public RubyFoldingPreferenceBlock(OverlayPreferenceStore store,
			PreferencePage page) {
		super(store, page);
	}

	@Override
	protected void addOverlayKeys(List<OverlayKey> keys) {
		super.addOverlayKeys(keys);
		keys.add(new OverlayPreferenceStore.OverlayKey(
				OverlayPreferenceStore.BOOLEAN,
				RubyPreferenceConstants.EDITOR_FOLDING_INIT_REQUIRES));
	}

	protected void addInitiallyFoldOptions(Group group) {
		super.addInitiallyFoldOptions(group);
		createCheckBox(group,
				RubyFoldingMessages.RubyFoldingPreferenceBlock_initRequires,
				RubyPreferenceConstants.EDITOR_FOLDING_INIT_REQUIRES);
	}

	// TODO: add addtional folding options

}
