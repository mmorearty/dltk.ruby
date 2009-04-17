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

import java.net.URL;
import java.util.Map;

import org.eclipse.dltk.ruby.formatter.internal.RubyFormatterPlugin;
import org.eclipse.dltk.ruby.formatter.preferences.RubyFormatterModifyDialog;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.formatter.AbstractScriptFormatterFactory;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialogOwner;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;
import org.eclipse.dltk.ui.preferences.PreferenceKey;

public class RubyFormatterFactory extends AbstractScriptFormatterFactory {

	public PreferenceKey getProfilesKey() {
		return new PreferenceKey(RubyFormatterPlugin.PLUGIN_ID,
				RubyFormatterConstants.FORMATTER_PROFILES);
	}

	public PreferenceKey getActiveProfileKey() {
		return new PreferenceKey(RubyFormatterPlugin.PLUGIN_ID,
				RubyFormatterConstants.FORMATTER_ACTIVE_PROFILE);
	}

	private static final String[] KEYS = {
			RubyFormatterConstants.FORMATTER_TAB_CHAR,
			RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
			RubyFormatterConstants.FORMATTER_TAB_SIZE,
			RubyFormatterConstants.INDENT_CLASS,
			RubyFormatterConstants.INDENT_MODULE,
			RubyFormatterConstants.INDENT_METHOD,
			RubyFormatterConstants.INDENT_BLOCKS,
			RubyFormatterConstants.INDENT_IF,
			RubyFormatterConstants.INDENT_CASE,
			RubyFormatterConstants.INDENT_WHEN,
			RubyFormatterConstants.LINES_FILE_AFTER_REQUIRE,
			RubyFormatterConstants.LINES_FILE_BETWEEN_MODULE,
			RubyFormatterConstants.LINES_FILE_BETWEEN_CLASS,
			RubyFormatterConstants.LINES_FILE_BETWEEN_METHOD,
			RubyFormatterConstants.LINES_BEFORE_FIRST,
			RubyFormatterConstants.LINES_BEFORE_MODULE,
			RubyFormatterConstants.LINES_BEFORE_CLASS,
			RubyFormatterConstants.LINES_BEFORE_METHOD,
			RubyFormatterConstants.LINES_PRESERVE,
			RubyFormatterConstants.WRAP_COMMENTS,
			RubyFormatterConstants.WRAP_COMMENTS_LENGTH };

	public PreferenceKey[] getPreferenceKeys() {
		final PreferenceKey[] result = new PreferenceKey[KEYS.length];
		for (int i = 0; i < KEYS.length; ++i) {
			final String key = KEYS[i];
			final String qualifier;
			if (RubyFormatterConstants.FORMATTER_TAB_CHAR.equals(key)
					|| RubyFormatterConstants.FORMATTER_INDENTATION_SIZE
							.equals(key)
					|| RubyFormatterConstants.FORMATTER_TAB_SIZE.equals(key)) {
				qualifier = RubyUI.PLUGIN_ID;
			} else {
				qualifier = RubyFormatterPlugin.PLUGIN_ID;
			}
			result[i] = new PreferenceKey(qualifier, key);
		}
		return result;
	}

	public IScriptFormatter createFormatter(String lineDelimiter,
			Map preferences) {
		return new RubyFormatter(lineDelimiter, preferences);
	}

	public URL getPreviewContent() {
		return getClass().getResource("formatterPreview.rb"); //$NON-NLS-1$
	}

	public IFormatterModifyDialog createDialog(
			IFormatterModifyDialogOwner dialogOwner) {
		return new RubyFormatterModifyDialog(dialogOwner, this);
	}

}
