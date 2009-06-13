/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.templates;

import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.templates.IScriptTemplateIndenter;
import org.eclipse.dltk.ui.templates.ScriptTemplateContext;
import org.eclipse.dltk.ui.templates.TabExpandScriptTemplateIndenter;
import org.eclipse.dltk.ui.text.util.TabStyle;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.TemplateContextType;

public class RubyTemplateContext extends ScriptTemplateContext {

	public RubyTemplateContext(TemplateContextType type, IDocument document,
			int completionOffset, int completionLength,
			ISourceModule sourceModule) {
		super(type, document, completionOffset, completionLength, sourceModule);
	}

	/*
	 * @see org.eclipse.dltk.ui.templates.ScriptTemplateContext#getIndenter()
	 */
	protected IScriptTemplateIndenter getIndenter() {
		IPreferencesLookupDelegate prefs = getPreferences();
		if (TabStyle.SPACES == TabStyle.forName(prefs.getString(
				RubyUI.PLUGIN_ID, CodeFormatterConstants.FORMATTER_TAB_CHAR))) {
			return new TabExpandScriptTemplateIndenter(prefs
					.getInt(RubyUI.PLUGIN_ID,
							CodeFormatterConstants.FORMATTER_TAB_SIZE));
		}
		return super.getIndenter();
	}
}
