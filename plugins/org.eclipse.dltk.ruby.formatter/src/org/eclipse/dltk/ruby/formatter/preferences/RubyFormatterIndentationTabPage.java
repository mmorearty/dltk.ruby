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
import org.eclipse.dltk.ui.util.SWTFactory;
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

	protected void createOptions(final IFormatterControlManager manager,
			Composite parent) {
		Group tabPolicyGroup = SWTFactory.createGroup(parent,
				"General Settings", 2, 1, GridData.FILL_HORIZONTAL);
		tabPolicy = manager.createCombo(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_TAB_CHAR,
				"Indentation character", tabPolicyItems);
		tabPolicy.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int index = tabPolicy.getSelectionIndex();
				if (index >= 0) {
					boolean indentSizeEnabled = CodeFormatterConstants.TAB
							.equals(tabPolicyItems[index]);
					manager.enableControl(indentSize, !indentSizeEnabled);
				}
			}
		});
		indentSize = manager.createNumber(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
				"Indentation size");
		tabSize = manager.createNumber(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_TAB_SIZE, "Tab size");
		//
		Group indentGroup = SWTFactory.createGroup(parent,
				"Indent within definitions", 1, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_CLASS,
				"Declarations within class body");
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_MODULE,
				"Declarations within module body");
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_METHOD,
				"Statements within method body");
		Group indentBlocks = SWTFactory.createGroup(parent,
				"Indent within blocks", 1, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_BLOCKS,
				"Statements within blocks body");
		manager.createCheckbox(indentBlocks, RubyFormatterConstants.INDENT_IF,
				"Statements within 'if' body");
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_CASE,
				"Statements within 'case' body");
		manager.createCheckbox(indentBlocks,
				RubyFormatterConstants.INDENT_WHEN,
				"Statements within 'when' body");
	}

	protected URL getPreviewContent() {
		return getClass().getResource("indentation-preview.rb");
	}
}
