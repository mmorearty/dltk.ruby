package org.eclipse.dltk.ruby.internal.ui.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.ruby.internal.ui.actions.messages"; //$NON-NLS-1$
	public static String RubyOpenTypeAction_anExceptionOccurredWhileOpeningTheClassModule;
	public static String RubyOpenTypeAction_openClassModule;
	public static String RubyOpenTypeAction_selectAClassModuleToOpen;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
