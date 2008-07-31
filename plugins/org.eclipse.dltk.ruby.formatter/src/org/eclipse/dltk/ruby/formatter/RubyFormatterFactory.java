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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.ui.formatter.AbstractScriptFormatterFactory;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;

public class RubyFormatterFactory extends AbstractScriptFormatterFactory {

	public Map retrievePreferences(PreferencesLookupDelegate delegate) {
		return new HashMap();
	}

	public IScriptFormatter createFormatter(String lineDelimiter,
			Map preferences) {
		return new RubyFormatter();
	}

}
