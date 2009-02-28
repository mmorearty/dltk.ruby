package org.eclipse.dltk.ruby.internal.ui.text.folding;

import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.DocumentationFoldingPreferenceBlock;
import org.eclipse.jface.preference.PreferencePage;

public class RubyDocFoldingPreferenceBlock extends
		DocumentationFoldingPreferenceBlock {

	public RubyDocFoldingPreferenceBlock(OverlayPreferenceStore store,
			PreferencePage page) {
		super(store, page);
	}

	protected String getInitiallyFoldDocsText() {
		return RubyFoldingMessages.RubyFoldingPreferenceBlock_initFoldRDoc;
	}

	protected boolean supportsDocFolding() {
		return true;
	}
}
