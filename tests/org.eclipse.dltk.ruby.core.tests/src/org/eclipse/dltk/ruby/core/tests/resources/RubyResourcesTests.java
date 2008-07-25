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

package org.eclipse.dltk.ruby.core.tests.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.ruby.core.tests.Activator;

public class RubyResourcesTests extends AbstractModelTests {

	private static final String PROJECT1 = "resource1";

	private static final String PROJECT2 = "resource2";

	public RubyResourcesTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Suite suite() {
		return new Suite(RubyResourcesTests.class);
	}

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpScriptProject(PROJECT1);
		setUpScriptProject(PROJECT2);
		waitUntilIndexesReady();
	}

	public void tearDownSuite() throws Exception {
		deleteProject(PROJECT1);
		deleteProject(PROJECT2);
		super.tearDownSuite();
	}

	public void testModelElementEquals() throws ModelException {
		final IScriptModel model = getScriptModel();
		final IScriptProject project1 = model.getScriptProject(PROJECT1);
		assertNotNull(project1);
		final IScriptProject project2 = model.getScriptProject(PROJECT2);
		assertNotNull(project2);
		assertFalse(project1.equals(project2));
		assertFalse(project2.equals(project1));
		//
		final IScriptProject project1a = model.getScriptProject(PROJECT1);
		assertNotNull(project1a);
		assertTrue(project1.equals(project1a));
		assertTrue(project1a.equals(project1));
		//
		final IScriptProject project2a = model.getScriptProject(PROJECT2);
		assertNotNull(project2a);
		assertTrue(project2.equals(project2a));
		assertTrue(project2a.equals(project2));
		//
		final ISourceModule module1 = (ISourceModule) project1
				.findElement(new Path("resource001.rb"));
		assertNotNull(module1);
		final ISourceModule module1a = (ISourceModule) project1a
				.findElement(new Path("resource001.rb"));
		assertNotNull(module1a);
		assertTrue(module1.equals(module1a));
		assertTrue(module1a.equals(module1));
	}

	public void testTypeEquals() {
		final IProject project = getProject(PROJECT1);
		final ISourceModule module1 = (ISourceModule) DLTKCore.create(project
				.getFile("class1.rb"));
		final ISourceModule module2 = (ISourceModule) DLTKCore.create(project
				.getFile("class2.rb"));
		final IType type1 = module1.getType("A001");
		final IType type2 = module2.getType("A001");
		assertFalse(type1.equals(type2));
	}

	public void testSourceModuleEquals() {
		final IProject project = getProject(PROJECT1);
		final ISourceModule module1 = (ISourceModule) DLTKCore.create(project
				.getFile("folder1/class3.rb"));
		final ISourceModule module2 = (ISourceModule) DLTKCore.create(project
				.getFile("folder2/class3.rb"));
		assertFalse(module1.equals(module2));
	}

}
