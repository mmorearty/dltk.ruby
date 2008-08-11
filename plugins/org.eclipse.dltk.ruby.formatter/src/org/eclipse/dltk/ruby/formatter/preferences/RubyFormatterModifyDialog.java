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

import org.eclipse.dltk.ui.formatter.FormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IFormatterDialogOwner;

public class RubyFormatterModifyDialog extends FormatterModifyDialog {

	/**
	 * @param parent
	 */
	public RubyFormatterModifyDialog(IFormatterDialogOwner dialogOwner) {
		super(dialogOwner);
		setTitle("Ruby Formatter");
	}

	protected void addPages() {
		addTabPage("Indentation", new RubyFormatterIndentationTabPage(this));
		addTabPage("Blank Lines", new RubyFormatterBlankLinesPage(this));
	}

}
