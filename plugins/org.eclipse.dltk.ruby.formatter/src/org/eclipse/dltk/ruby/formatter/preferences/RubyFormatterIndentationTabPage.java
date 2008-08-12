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

	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		Group tabPolicyGroup = SWTFactory.createGroup(parent,
				"General Settings", 2, 1, GridData.FILL_HORIZONTAL);
		manager.createCombo(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_TAB_CHAR,
				"Indentation character", new String[] {
						CodeFormatterConstants.TAB,
						CodeFormatterConstants.SPACE });
		manager.createNumber(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
				"Indentation size");
		//
		Group indentGroup = SWTFactory.createGroup(parent, "Indent", 1, 1,
				GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_CLASS,
				"Declarations within class body");
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_MODULE,
				"Declarations within module body");
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_METHOD,
				"Statements within method body");
		manager.createCheckbox(indentGroup,
				RubyFormatterConstants.INDENT_BLOCKS,
				"Statements within blocks body");
		manager.createCheckbox(indentGroup, RubyFormatterConstants.INDENT_IF,
				"Statements within 'if' body");
		manager.createCheckbox(indentGroup, RubyFormatterConstants.INDENT_CASE,
				"Statements within 'case' body");
		manager.createCheckbox(indentGroup, RubyFormatterConstants.INDENT_WHEN,
				"Statements within 'when' body");
	}

	protected URL getPreviewContent() {
		return getClass().getResource("indentation-preview.rb");
	}
}
