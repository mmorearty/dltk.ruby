/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.ruby.internal.ui.text.rules.BeginOfLineRule;
import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.TodoTagRule;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class RubyDocScanner extends AbstractScriptScanner {
	private static final String[] fgTokenProperties = new String[] {
			IRubyColorConstants.RUBY_DOC, IRubyColorConstants.RUBY_DOC_TOPIC,
			IRubyColorConstants.RUBY_TODO_COMMENT };

	public RubyDocScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store);

		initialize();
	}

	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	protected List createRules() {
		final List/* <IRule> */rules = new ArrayList/* <IRule> */();

		final IToken topic = getToken(IRubyColorConstants.RUBY_DOC_TOPIC);
		final IToken other = getToken(IRubyColorConstants.RUBY_DOC);
		final IToken todo = getToken(IRubyColorConstants.RUBY_TODO_COMMENT);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new RubyWhitespaceDetector()));
		rules.add(new BeginOfLineRule(topic, '='));
		rules.add(createTodoTagRule(todo));

		setDefaultReturnToken(other);
		return rules;
	}

	private TodoTagRule createTodoTagRule(final IToken token) {
		final ITodoTaskPreferences prefs = new TodoTaskPreferences(RubyPlugin
				.getDefault().getPluginPreferences());
		return new TodoTagRule(token, prefs.getTagNames(), prefs
				.isCaseSensitive());
	}

}
