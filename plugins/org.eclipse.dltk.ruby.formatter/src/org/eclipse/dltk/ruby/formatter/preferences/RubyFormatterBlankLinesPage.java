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
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RubyFormatterBlankLinesPage extends FormatterModifyTabPage {

	/**
	 * @param dialog
	 */
	public RubyFormatterBlankLinesPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		Group emptyLinesGroup = SWTFactory.createGroup(parent, "Blank Lines",
				2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_BEFORE_CLASS,
				"Before class/module");
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_AFTER_CLASS, "After class/module");
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_BEFORE_METHOD, "Before method");
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_AFTER_METHOD, "After method");
		Group preserveGroup = SWTFactory.createGroup(parent,
				"Existing blank lines", 2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(preserveGroup,
				RubyFormatterConstants.LINES_PRESERVE,
				"Number of empty lines to preserve");
	}

	protected URL getPreviewContent() {
		return getClass().getResource("blank-lines-preview.rb");
	}

}
