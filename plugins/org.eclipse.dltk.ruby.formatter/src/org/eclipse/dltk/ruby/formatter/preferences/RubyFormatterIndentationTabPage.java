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

import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RubyFormatterIndentationTabPage extends FormatterModifyTabPage {

	/**
	 * @param dialogOwner
	 */
	public RubyFormatterIndentationTabPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	protected void createOptions(Composite parent) {
		Group tabPolicyGroup = SWTFactory.createGroup(parent,
				"General Settings", 2, 1, GridData.FILL_HORIZONTAL);
		createCombo(tabPolicyGroup, RubyFormatterConstants.FORMATTER_TAB_CHAR,
				"Indentation character", new String[] {
						CodeFormatterConstants.TAB,
						CodeFormatterConstants.SPACE });
		createNumber(tabPolicyGroup,
				RubyFormatterConstants.FORMATTER_INDENTATION_SIZE,
				"Indentation size");
		//
		Group indentGroup = SWTFactory.createGroup(parent, "Indent", 1, 1,
				GridData.FILL_HORIZONTAL);
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_CLASS,
				"Declarations within class body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_MODULE,
				"Declarations within module body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_METHOD,
				"Statements within method body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_BLOCKS,
				"Statements within blocks body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_IF,
				"Statements within 'if' body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_CASE,
				"Statements within 'case' body");
		createCheckbox(indentGroup, RubyFormatterConstants.INDENT_WHEN,
				"Statements within 'when' body");
	}
}
