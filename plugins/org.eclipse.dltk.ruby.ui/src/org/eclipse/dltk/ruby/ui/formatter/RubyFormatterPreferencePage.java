package org.eclipse.dltk.ruby.ui.formatter;

import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage;
import org.eclipse.dltk.ui.preferences.PreferenceKey;

/**
 * Preference page for Ruby debugging engines
 */
public class RubyFormatterPreferencePage extends
		AbstractFormatterPreferencePage {

	private static final PreferenceKey FORMATTER = new PreferenceKey(
			RubyUI.PLUGIN_ID, RubyPreferenceConstants.FORMATTER_ID);

	protected void setDescription() {
		setDescription(Messages.RubyFormatterPreferencePage_description);
	}

	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	protected PreferenceKey getFormatterPreferenceKey() {
		return FORMATTER;
	}

}
