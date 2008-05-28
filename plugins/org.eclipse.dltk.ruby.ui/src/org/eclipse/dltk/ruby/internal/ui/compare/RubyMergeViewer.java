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
package org.eclipse.dltk.ruby.internal.ui.compare;

import org.eclipse.compare.CompareConfiguration;
import org.eclipse.dltk.ruby.internal.ui.Messages;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ui.compare.ScriptMergeViewer;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;

public class RubyMergeViewer extends ScriptMergeViewer {

	public RubyMergeViewer(Composite parent, CompareConfiguration configuration) {
		super(parent, configuration);
	}

	protected ScriptTextTools getTextTools() {
		return RubyUI.getDefault().getTextTools();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.ruby.internal.ui.compare.ScriptMergeViewer#
	 * getPreferenceStore()
	 */
	protected IPreferenceStore getPreferenceStore() {
		return RubyUI.getDefault().getPreferenceStore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ui.compare.ScriptMergeViewer#getTitle()
	 */
	public String getTitle() {
		return Messages.CompareTitle;
	}

}
