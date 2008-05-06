/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *     xored software, Inc. - Search All occurences bugfix, 
 *     						  hilight only class name when class is in search results ( Alex Panchenko <alex@xored.com>)
 *******************************************************************************/

package org.eclipse.dltk.ruby.core.tests.search;

import org.eclipse.dltk.core.tests.model.AbstractSingleProjectSearchTests;
import org.eclipse.dltk.core.tests.model.TestSearchResults;
import org.eclipse.dltk.ruby.core.tests.Activator;

public class RubySearchTests extends AbstractSingleProjectSearchTests {

	public RubySearchTests(String name) {
		super(Activator.PLUGIN_ID, name, "search");
	}

	public static Suite suite() {
		return new Suite(RubySearchTests.class);
	}

	private final String fileName = "Search001.rb";
	private final String className = "Search001";

	public void testDeclarations() throws Exception {
		final TestSearchResults results = search(className, TYPE, DECLARATIONS);
		assertEquals(1, results.size());
		results.assertType(className);
	}

	public void testReferences() throws Exception {
		final TestSearchResults results = search(className, TYPE, REFERENCES);
		assertEquals(1, results.size());
		results.assertSourceModule(fileName);
	}

	public void testAllOccurences() throws Exception {
		final TestSearchResults results = search(className, TYPE,
				ALL_OCCURRENCES);
		assertEquals(2, results.size());
		results.assertSourceModule(fileName);
		results.assertType(className);
	}

}
