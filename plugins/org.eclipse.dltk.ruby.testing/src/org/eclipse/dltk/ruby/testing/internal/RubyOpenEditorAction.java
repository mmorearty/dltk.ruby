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
package org.eclipse.dltk.ruby.testing.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.jface.action.Action;

public class RubyOpenEditorAction extends Action {

	private final Object element;
	private int lineNumber;

	/**
	 * @param element
	 * @param lineNumber
	 */
	public RubyOpenEditorAction(Object element, int lineNumber) {
		this.element = element;
		this.lineNumber = lineNumber;
	}

	/*
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		try {
			RubyFileHyperlink.openInEditor(element, lineNumber);
		} catch (CoreException e) {
			RubyTestingPlugin.error(Messages.openEditorError, e);
		}
	}

}
