package org.eclipse.dltk.ruby.internal.debug.ui;

import org.eclipse.dltk.debug.ui.DLTKDebugUIPluginPreferenceInitializer;
import org.eclipse.dltk.ruby.core.RubyNature;

public class RubyDebugUIPreferenceInitializer extends
		DLTKDebugUIPluginPreferenceInitializer {

	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}
}
