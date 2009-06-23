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

import org.eclipse.dltk.ruby.internal.ui.text.rules.BeginOfLineRule;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptCommentScanner;
import org.eclipse.dltk.ui.text.TodoTaskPreferencesOnPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;

public class RubyDocScanner extends ScriptCommentScanner {
	private static final String[] fgTokenProperties = new String[] {
			IRubyColorConstants.RUBY_DOC, IRubyColorConstants.RUBY_DOC_TOPIC,
			IRubyColorConstants.RUBY_TODO_COMMENT };

	public RubyDocScanner(IColorManager manager, IPreferenceStore store) {
		super(manager, store, IRubyColorConstants.RUBY_DOC,
				IRubyColorConstants.RUBY_TODO_COMMENT,
				new TodoTaskPreferencesOnPreferenceStore(store), false);
		initialize();
	}

	@Override
	protected String[] getTokenProperties() {
		return fgTokenProperties;
	}

	@Override
	protected List<IRule> createRules() {
		final List<IRule> rules = new ArrayList<IRule>();

		final IToken topic = getToken(IRubyColorConstants.RUBY_DOC_TOPIC);
		final IToken other = getToken(IRubyColorConstants.RUBY_DOC);

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new RubyWhitespaceDetector()));
		rules.add(new BeginOfLineRule(topic, '='));
		rules.add(createTodoRule());

		setDefaultReturnToken(other);
		return rules;
	}

	@Override
	protected int skipCommentChars() {
		return 0;
	}

	/**
	 * FIXME Standard implementation copied (Alex)
	 */
	@Override
	public IToken nextToken() {
		fTokenOffset = fOffset;
		fColumn = UNDEFINED;
		if (fRules != null) {
			for (int i = 0; i < fRules.length; i++) {
				IToken token = (fRules[i].evaluate(this));
				if (!token.isUndefined())
					return token;
			}
		}
		if (read() == EOF)
			return Token.EOF;
		return fDefaultReturnToken;
	}

}
