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
package org.eclipse.dltk.ruby.testing.internal.rspec;

public class RSpecUtils {

	public static final String[] CONTEXT_METHODS = { "describe", //$NON-NLS-1$
			"context" //$NON-NLS-1$
	};

	/**
	 * @param name
	 * @return
	 */
	public static boolean isDecribe(String name) {
		for (int i = 0; i < CONTEXT_METHODS.length; ++i) {
			if (CONTEXT_METHODS[i].equals(name)) {
				return true;
			}
		}
		return false;
	}

}
