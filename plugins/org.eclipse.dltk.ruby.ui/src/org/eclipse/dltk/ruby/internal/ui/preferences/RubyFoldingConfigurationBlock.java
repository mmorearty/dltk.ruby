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

import org.eclipse.dltk.ruby.internal.ui.text.folding.RubyFoldingPreferenceBlock;
import org.eclipse.dltk.ui.preferences.FoldingConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.IFoldingPreferenceBlock;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;

public class RubyFoldingConfigurationBlock extends FoldingConfigurationBlock {

	public RubyFoldingConfigurationBlock(OverlayPreferenceStore store,
			PreferencePage prefPage) {
		super(store, prefPage);
	}

	protected void createCommentsFoldingCheckbox(Composite composite) {
		// empty
	}

	protected IFoldingPreferenceBlock createFoldingPreferenceBlock() {
		return new RubyFoldingPreferenceBlock(fStore);
	}

}
