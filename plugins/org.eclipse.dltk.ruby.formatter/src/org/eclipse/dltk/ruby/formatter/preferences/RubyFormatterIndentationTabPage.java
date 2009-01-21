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
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.preferences.FormatterMessages;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public class RubyFormatterIndentationTabPage extends FormatterModifyTabPage {

	/**
	 * @param dialog
	 */
	public RubyFormatterIndentationTabPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	private Combo tabPolicy;
	private Text indentSize;
	private Text tabSize;

	private final String[] tabPolicyItems = new String[] {
			CodeFormatterConstants.SPACE, CodeFormatterConstants.TAB,
			CodeFormatterConstants.MIXED };

	private class TabPolicyListener extends SelectionAdapter implements
			IFormatterControlManager.IInitializeListener {

		private final IFormatterControlManager manager;

		public TabPolicyListener(IFormatterControlManager manager) {
			this.manager = manager;
		}

		public void widgetSelected(SelectionEvent e) {
			int index = tabPolicy.getSelectionIndex();
			if (index >= 0) {
				final boolean tabMode = CodeFormatterConstants.TAB
						.equals(tabPolicyItems[index]);
				manager.enableControl(indentSize, !tabMode);
			}
		}

		public void initialize() {
			final boolean tabMode = CodeFormatterConstants.TAB.equals(manager
					.getString(RubyFormatterConstants.FORMATTER_TAB_CHAR));
			manager.enableControl(indentSize, !tabMode);
		}

	}

	private TabPolicyListener tabPolicyListener;

	protected void createOptions(final IFormatterControlManager manager,
			Composite parent) {
		Group tabPolicyGroup = SWTFactory.createGroup(parent,
				Messages.RubyFormatterIndentationTabPage_generalSettings, 2, 1, GridData.FILL_HORIZONTAL);
		tabPolicy = manager
				.createCombo(
						tabPolicyGroup,
						RubyFormatterConstants.FORMATTER_TAB_CHAR,
						FormatterMessages.IndentationTabPage_general_group_option_tab_policy,
						tabPolicyItems);
		tabPolicyListener = new TabPolicyListener(manager);
		tabPolicy.addSelectionListener(tabPolicyListener);
		manager.addInitializeListener(tabPolicyListener);
		indentSize = manager
				.createNumber(
						tabPolicyGroup,
						RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
						FormatterMessages.IndentationTabPage_general_group_option_indent_size);
		tabSize = manager
				.createNumber(
						tabPolicyGroup,
						RubyFormatterConstants.FORMATTER_TAB_SIZE,
						FormatterMessages.IndentationTabPage_general_group_option_tab_size);
		tabSize.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int index = tabPolicy.getSelectionIndex();
				if (index >= 0) {
					final boolean tabMode = CodeFormatterConstants.TAB
							.equals(tabPolicyItems[index]);
					if (tabMode) {
						indentSize.setText(tabSize.getText());
					}
				}
			}
		});
		//
		Group indentGroup = SWTFactory.createGroup(parent,
				Messages.RubyFormatterIndentationTabPage_indentWithinDefinitions, 1, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_CLASS,
				Messages.RubyFormatterIndentationTabPage_declarationsWithinClassBody);
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_MODULE,
				Messages.RubyFormatterIndentationTabPage_declarationsWithinModuleBody);
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_METHOD,
				Messages.RubyFormatterIndentationTabPage_statementsWithinMethodBody);
		Group indentBlocks = SWTFactory.createGroup(parent,
				Messages.RubyFormatterIndentationTabPage_indentWithinBlocks, 1, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_BLOCKS,
				Messages.RubyFormatterIndentationTabPage_statementsWithinBlockBody);
		manager.createCheckbox(indentBlocks, RubyFormatterConstants.INDENT_IF,
				Messages.RubyFormatterIndentationTabPage_statementsWithinIfBody);
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_CASE,
				Messages.RubyFormatterIndentationTabPage_statementsWithinCaseBody);
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_WHEN,
				Messages.RubyFormatterIndentationTabPage_StatementsWithinWhenBody);
	}

	protected URL getPreviewContent() {
		return getClass().getResource("indentation-preview.rb"); //$NON-NLS-1$
	}
}
