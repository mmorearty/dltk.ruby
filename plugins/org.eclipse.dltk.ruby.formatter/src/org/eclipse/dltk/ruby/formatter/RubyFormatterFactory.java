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
package org.eclipse.dltk.ruby.formatter;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.IPreferencesSaveDelegate;
import org.eclipse.dltk.ruby.formatter.internal.RubyFormatterPlugin;
import org.eclipse.dltk.ruby.formatter.preferences.RubyFormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.AbstractScriptFormatterFactory;
import org.eclipse.dltk.ui.formatter.IFormatterDialogOwner;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;

public class RubyFormatterFactory extends AbstractScriptFormatterFactory {

	private static final String[] KEYS = {
			RubyFormatterConstants.FORMATTER_TAB_CHAR,
			RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
			RubyFormatterConstants.INDENT_CLASS,
			RubyFormatterConstants.INDENT_MODULE,
			RubyFormatterConstants.INDENT_METHOD,
			RubyFormatterConstants.INDENT_BLOCKS,
			RubyFormatterConstants.INDENT_IF,
			RubyFormatterConstants.INDENT_CASE,
			RubyFormatterConstants.INDENT_WHEN,
			RubyFormatterConstants.LINES_BEFORE_CLASS,
			RubyFormatterConstants.LINES_AFTER_CLASS,
			RubyFormatterConstants.LINES_BEFORE_METHOD,
			RubyFormatterConstants.LINES_AFTER_METHOD,
			RubyFormatterConstants.LINES_PRESERVE };

	public String getPreferenceQualifier() {
		return RubyFormatterPlugin.PLUGIN_ID;
	}

	public String[] getPreferenceKeys() {
		return KEYS;
	}

	public Map retrievePreferences(IPreferencesLookupDelegate delegate) {
		final String qualifier = getPreferenceQualifier();
		final Map result = new HashMap();
		for (int i = 0; i < KEYS.length; ++i) {
			final String key = KEYS[i];
			result.put(key, delegate.getString(qualifier, key));
		}
		return result;
	}

	public void savePreferences(Map preferences,
			IPreferencesSaveDelegate delegate) {
		final String qualifier = getPreferenceQualifier();
		for (int i = 0; i < KEYS.length; ++i) {
			final String key = KEYS[i];
			if (preferences.containsKey(key)) {
				final String value = (String) preferences.get(key);
				delegate.setString(qualifier, key, value);
			}
		}
	}

	public IScriptFormatter createFormatter(String lineDelimiter,
			Map preferences) {
		return new RubyFormatter(preferences);
	}

	public String getPreviewContent() {
		final URL resource = getClass().getResource("formatterPreview.rb");
		if (resource != null) {
			try {
				return new String(Util.getInputStreamAsCharArray(resource
						.openConnection().getInputStream(), -1, "ISO-8859-1"));
			} catch (IOException e) {
				RubyFormatterPlugin.warn("Error reading preview resource", e);
			}
		} else {
			RubyFormatterPlugin.warn("Preview resource not found");
		}
		return null;
	}

	public IFormatterModifyDialog createDialog(IFormatterDialogOwner dialogOwner) {
		return new RubyFormatterModifyDialog(dialogOwner);
	}

}
