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
package org.eclipse.dltk.ruby.formatter;

import org.eclipse.dltk.ui.formatter.AbstractScriptFormatter;
import org.eclipse.text.edits.TextEdit;

public class RubyFormatter extends AbstractScriptFormatter {

	public TextEdit format(String source, int offset, int length, int indent) {
		return null;// new ReplaceEdit(offset, length, input);
	}

}
