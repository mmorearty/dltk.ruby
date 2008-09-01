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
package org.eclipse.dltk.ruby.testing.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static final String BUNDLE_ID = "org.eclipse.dltk.ruby.testing.tests"; //$NON-NLS-1$

	public static Test suite() {
		TestSuite suite = new TestSuite(BUNDLE_ID);
		// $JUnit-BEGIN$
		suite.addTest(RubyTestingTestUnitResolverTests.suite());
		// $JUnit-END$
		return suite;
	}

}
