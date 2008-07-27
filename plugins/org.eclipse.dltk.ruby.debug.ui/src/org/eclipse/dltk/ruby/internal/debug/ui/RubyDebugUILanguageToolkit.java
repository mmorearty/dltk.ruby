package org.eclipse.dltk.ruby.internal.debug.ui;

import org.eclipse.dltk.debug.ui.AbstractDebugUILanguageToolkit;
import org.eclipse.dltk.ruby.debug.RubyDebugConstants;
import org.eclipse.jface.preference.IPreferenceStore;

public class RubyDebugUILanguageToolkit extends AbstractDebugUILanguageToolkit {

	/*
	 * @see org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getDebugModelId()
	 */
	public String getDebugModelId() {
		return RubyDebugConstants.DEBUG_MODEL_ID;
	}
	
	/*
	 * @see org.eclipse.dltk.debug.ui.IDLTKDebugUILanguageToolkit#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		return RubyDebugUIPlugin.getDefault().getPreferenceStore();
	}

	/*
	 * @see org.eclipse.dltk.debug.ui.AbstractDebugUILanguageToolkit#getVariablesViewPreferencePages()
	 */
	public String[] getVariablesViewPreferencePages() {
		return new String[] { "org.eclipse.dltk.ruby.preferences.debug.detailFormatters" };
	}
}
