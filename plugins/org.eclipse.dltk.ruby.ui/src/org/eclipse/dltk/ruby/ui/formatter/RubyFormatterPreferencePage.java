package org.eclipse.dltk.ruby.ui.formatter;

import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ruby.internal.ui.preferences.SimpleRubySourceViewerConfiguration;
import org.eclipse.dltk.ruby.internal.ui.text.IRubyPartitions;
import org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Preference page for Ruby debugging engines
 */
public class RubyFormatterPreferencePage extends
		AbstractFormatterPreferencePage {

	private static final PreferenceKey FORMATTER = new PreferenceKey(
			RubyUI.PLUGIN_ID, RubyPreferenceConstants.FORMATTER_ID);

	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	protected PreferenceKey getFormatterPreferenceKey() {
		return FORMATTER;
	}

	protected IDialogSettings getDialogSettings() {
		return RubyUI.getDefault().getDialogSettings();
	}

	protected String getPreferencePageId() {
		return "org.eclipse.dltk.ruby.preferences.formatter"; //$NON-NLS-1$
	}

	protected String getPropertyPageId() {
		return "org.eclipse.dltk.ruby.propertyPage.formatter"; //$NON-NLS-1$
	}

	protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, boolean configureFormatter) {
		return new SimpleRubySourceViewerConfiguration(colorManager,
				preferenceStore, editor, IRubyPartitions.RUBY_PARTITIONING,
				configureFormatter);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(RubyUI.getDefault().getPreferenceStore());
	}

}
