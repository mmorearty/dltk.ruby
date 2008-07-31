/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.formatter.internal;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.jface.preference.IPreferenceStore;

public class RubyFormatterPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = RubyUI.getDefault().getPreferenceStore();
		store.setDefault(RubyPreferenceConstants.FORMATTER_ID,
				"org.eclipse.dltk.ruby.formatter.RubyFormatterFactory"); //$NON-NLS-1$
	}
}
