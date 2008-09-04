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

	public static final String[] TEST_METHODS = { "it", //$NON-NLS-1$
			"specify" //$NON-NLS-1$
	};

	public static final String[] TEST_SHARED = { "it_should_behave_like" //$NON-NLS-1$
	};

	public static final String[] SHARED_GROUP = { "shared_examples_for", //$NON-NLS-1$
			"share_examples_for", //$NON-NLS-1$
			"share_as" //$NON-NLS-1$
	};

}
