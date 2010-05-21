package org.eclipse.dltk.ruby.internal.ui.actions;

import org.eclipse.dltk.ui.actions.ScriptOpenSearchPageAction;

public class OpenRubySearchPageAction extends ScriptOpenSearchPageAction {

	private static final String RUBY_SEARCH_PAGE_ID = "org.eclipse.dltk.ui.RubySearchPage"; //$NON-NLS-1$

	@Override
	protected String getSearchPageId() {
		return RUBY_SEARCH_PAGE_ID;
	}
}