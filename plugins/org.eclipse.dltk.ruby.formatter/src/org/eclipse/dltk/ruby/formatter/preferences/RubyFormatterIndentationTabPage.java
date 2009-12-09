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
package org.eclipse.dltk.ruby.formatter.preferences;

import java.net.URL;

import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;
import org.eclipse.dltk.ui.formatter.FormatterIndentationGroup;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RubyFormatterIndentationTabPage extends FormatterModifyTabPage {

	/**
	 * @param dialog
	 */
	public RubyFormatterIndentationTabPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	protected void createOptions(final IFormatterControlManager manager,
			Composite parent) {
		new FormatterIndentationGroup(manager, parent);
		//
		Group indentGroup = SWTFactory
				.createGroup(
						parent,
						Messages.RubyFormatterIndentationTabPage_indentWithinDefinitions,
						1, 1, GridData.FILL_HORIZONTAL);
		manager
				.createCheckbox(
						indentGroup,
						RubyFormatterConstants.INDENT_CLASS,
						Messages.RubyFormatterIndentationTabPage_declarationsWithinClassBody);
		manager
				.createCheckbox(
						indentGroup,
						RubyFormatterConstants.INDENT_MODULE,
						Messages.RubyFormatterIndentationTabPage_declarationsWithinModuleBody);
		manager
				.createCheckbox(
						indentGroup,
						RubyFormatterConstants.INDENT_METHOD,
						Messages.RubyFormatterIndentationTabPage_statementsWithinMethodBody);
		Group indentBlocks = SWTFactory.createGroup(parent,
				Messages.RubyFormatterIndentationTabPage_indentWithinBlocks, 1,
				1, GridData.FILL_HORIZONTAL);
		manager
				.createCheckbox(
						indentBlocks,
						RubyFormatterConstants.INDENT_BLOCKS,
						Messages.RubyFormatterIndentationTabPage_statementsWithinBlockBody);
		manager
				.createCheckbox(
						indentBlocks,
						RubyFormatterConstants.INDENT_IF,
						Messages.RubyFormatterIndentationTabPage_statementsWithinIfBody);
		manager
				.createCheckbox(
						indentBlocks,
						RubyFormatterConstants.INDENT_CASE,
						Messages.RubyFormatterIndentationTabPage_statementsWithinCaseBody);
		manager
				.createCheckbox(
						indentBlocks,
						RubyFormatterConstants.INDENT_WHEN,
						Messages.RubyFormatterIndentationTabPage_StatementsWithinWhenBody);
	}

	protected URL getPreviewContent() {
		return getClass().getResource("indentation-preview.rb"); //$NON-NLS-1$
	}
}
