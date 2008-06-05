package org.eclipse.dltk.ruby.fastdebugger.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.debug.ui.preferences.DebuggingEngineConfigOptionsBlock;
import org.eclipse.dltk.ruby.fastdebugger.FastDebuggerConstants;
import org.eclipse.dltk.ruby.fastdebugger.FastDebuggerPlugin;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class FastDebuggerConfigurationBlock extends
		DebuggingEngineConfigOptionsBlock {

	private static final PreferenceKey ENABLE_LOGGING = new PreferenceKey(
			FastDebuggerPlugin.PLUGIN_ID, FastDebuggerConstants.ENABLE_LOGGING);

	private static final PreferenceKey LOG_FILE_PATH = new PreferenceKey(
			FastDebuggerPlugin.PLUGIN_ID, FastDebuggerConstants.LOG_FILE_PATH);

	private static final PreferenceKey LOG_FILE_NAME = new PreferenceKey(
			FastDebuggerPlugin.PLUGIN_ID, FastDebuggerConstants.LOG_FILE_NAME);

	private static final PreferenceKey CHECK_RUBY_DEBUG = new PreferenceKey(
			FastDebuggerPlugin.PLUGIN_ID,
			FastDebuggerConstants.CHECK_RUBY_DEBUG);

	public FastDebuggerConfigurationBlock(IStatusChangeListener context,
			IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, getKeys(), container);
	}

	private static PreferenceKey[] getKeys() {
		return new PreferenceKey[] { ENABLE_LOGGING, LOG_FILE_PATH,
				LOG_FILE_NAME, CHECK_RUBY_DEBUG };
	}

	protected void createEngineBlock(Composite composite) {
		// no engine preferences, yet...
	}

	protected void createOtherBlock(Composite parent) {
		final Label noteLabel = new Label(parent, SWT.WRAP);
		noteLabel
				.setText(FastDebuggerPreferenceMessages.FastDebuggerConfigurationBlock_rubyDebugGemMustBeInstalled);
		GridData data = new GridData(SWT.FILL, GridData.BEGINNING, true, false);
		data.widthHint = 100;
		noteLabel.setLayoutData(data);

		Button checkRubyDebug = SWTFactory
				.createCheckButton(
						parent,
						FastDebuggerPreferenceMessages.FastDebuggerConfigurationBlock_rubyDebugCheckInstalled,
						null, false, 1);

		bindControl(checkRubyDebug, CHECK_RUBY_DEBUG, null);
	}

	protected PreferenceKey getEnableLoggingPreferenceKey() {
		return ENABLE_LOGGING;
	}

	protected PreferenceKey getLogFileNamePreferenceKey() {
		return LOG_FILE_NAME;
	}

	protected PreferenceKey getLogFilePathPreferenceKey() {
		return LOG_FILE_PATH;
	}
}
