/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.core.tests.search;

import org.eclipse.dltk.core.tests.model.AbstractSingleProjectSearchTests;
import org.eclipse.dltk.core.tests.model.TestSearchResults;
import org.eclipse.dltk.ruby.core.tests.Activator;

public class RubyMethodSearchTests extends AbstractSingleProjectSearchTests {

	public RubyMethodSearchTests(String name) {
		super(Activator.PLUGIN_ID, name, "search-methods");
	}

	public static Suite suite() {
		return new Suite(RubyMethodSearchTests.class);
	}

	private final String methodName = "method001";

	public void testDeclarations() throws Exception {
		final TestSearchResults results = search(methodName, METHOD,
				DECLARATIONS);
		assertEquals(2, results.size());
		// results.assertType(className);
	}

	public void testReferences() throws Exception {
		final TestSearchResults results = search(methodName, METHOD, REFERENCES);
		assertEquals(2, results.size());
		// results.assertSourceModule(fileName);
	}

	public void testAllOccurences() throws Exception {
		final TestSearchResults results = search(methodName, METHOD,
				ALL_OCCURRENCES);
		assertEquals(4, results.size());
		// results.assertSourceModule(fileName);
		// results.assertType(className);
	}

}
