/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.templates;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ruby.internal.ui.text.RubyPreferenceInterpreter;
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
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ui.templates.ScriptTemplateContext#getIndenter()
	 */
	protected IScriptTemplateIndenter getIndenter() {
		RubyPreferenceInterpreter pref = RubyPreferenceInterpreter.getDefault();
		if (TabStyle.SPACES == pref.getTabStyle()) {
			return new TabExpandScriptTemplateIndenter(pref.getTabSize());
		}
		return super.getIndenter();
	}
}
