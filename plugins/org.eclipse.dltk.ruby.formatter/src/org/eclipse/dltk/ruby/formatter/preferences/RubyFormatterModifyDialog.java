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
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialogOwner;
import org.eclipse.dltk.ui.formatter.IScriptFormatterFactory;

public class RubyFormatterModifyDialog extends FormatterModifyDialog {

	/**
	 * @param parent
	 */
	public RubyFormatterModifyDialog(IFormatterModifyDialogOwner dialogOwner,
			IScriptFormatterFactory formatterFactory) {
		super(dialogOwner, formatterFactory);
	}

	protected void addPages() {
		addTabPage(Messages.RubyFormatterModifyDialog_indentation,
				new RubyFormatterIndentationTabPage(this));
		addTabPage(Messages.RubyFormatterModifyDialog_blankLines,
				new RubyFormatterBlankLinesPage(this));
		addTabPage(Messages.RubyFormatterModifyDialog_comments,
				new RubyFormatterCommentsPage(this));
	}

}
