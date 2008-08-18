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
package org.eclipse.dltk.ruby.core.tests.search.mixin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelStatus;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.tests.model.AbstractDLTKSearchTests;
import org.eclipse.dltk.internal.core.BuildpathEntry;
import org.eclipse.dltk.ruby.core.tests.Activator;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixin;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinModel;

public class MixinProjectIsolationTests extends AbstractDLTKSearchTests
		implements IDLTKSearchConstants {

	private static final String PROJECT1_NAME = "mixins1";
	private static final String PROJECT2_NAME = "mixins2";

	private static final String METHOD1_NAME = "testProjectIsolation1";
	private static final String METHOD2_NAME = "testProjectIsolation2";

	public MixinProjectIsolationTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Suite suite() {
		return new Suite(MixinProjectIsolationTests.class);
	}

	private IScriptProject project1;
	private IScriptProject project2;

	protected void setUp() throws Exception {
		super.setUp();
		project1 = setUpScriptProject(PROJECT1_NAME);
		project2 = setUpScriptProject(PROJECT2_NAME);
		waitUntilIndexesReady();
	}

	protected void tearDown() throws Exception {
		deleteProject(PROJECT1_NAME);
		deleteProject(PROJECT2_NAME);
		super.tearDown();
	}

	private static final String TEST_CLASS_NAME = "TestClass20080818";

	public void testSimple() {
		checkModel(RubyMixinModel.getInstance(project1),
				new String[] { METHOD1_NAME });
		checkModel(RubyMixinModel.getInstance(project2),
				new String[] { METHOD2_NAME });
		checkModel(RubyMixinModel.getWorkspaceInstance(), new String[] {
				METHOD1_NAME, METHOD2_NAME });
	}

	public void testComplex2to1() throws ModelException {
		testSimple();
		addBuildpathEntry(project1, DLTKCore.newProjectEntry(new Path("/"
				+ PROJECT2_NAME)));
		waitUntilIndexesReady();
		//
		checkModel(RubyMixinModel.getInstance(project1), new String[] {
				METHOD1_NAME, METHOD2_NAME });
		checkModel(RubyMixinModel.getInstance(project2),
				new String[] { METHOD2_NAME });
		checkModel(RubyMixinModel.getWorkspaceInstance(), new String[] {
				METHOD1_NAME, METHOD2_NAME });
		//
		removeBuildpathEntry(project1, DLTKCore.newProjectEntry(new Path("/"
				+ PROJECT2_NAME)));
		waitUntilIndexesReady();
		//
		testSimple();
	}

	public void testComplex1to2() throws ModelException {
		testSimple();
		addBuildpathEntry(project2, DLTKCore.newProjectEntry(new Path("/"
				+ PROJECT1_NAME)));
		waitUntilIndexesReady();
		//
		checkModel(RubyMixinModel.getInstance(project1),
				new String[] { METHOD1_NAME });
		checkModel(RubyMixinModel.getInstance(project2), new String[] {
				METHOD1_NAME, METHOD2_NAME });
		checkModel(RubyMixinModel.getWorkspaceInstance(), new String[] {
				METHOD1_NAME, METHOD2_NAME });
		//
		removeBuildpathEntry(project2, DLTKCore.newProjectEntry(new Path("/"
				+ PROJECT1_NAME)));
		waitUntilIndexesReady();
		//
		testSimple();
	}

	private void addBuildpathEntry(final IScriptProject project,
			final IBuildpathEntry newEntry) throws ModelException {
		IBuildpathEntry[] originalCP = project.getRawBuildpath();
		IBuildpathEntry[] newCP = new IBuildpathEntry[originalCP.length + 1];
		System.arraycopy(originalCP, 0, newCP, 0, originalCP.length);
		newCP[originalCP.length] = newEntry;
		IModelStatus status = BuildpathEntry.validateBuildpath(project, newCP);
		assertEquals(IStatus.OK, status.getSeverity());
		project.setRawBuildpath(newCP, null);
	}

	private void removeBuildpathEntry(final IScriptProject project,
			final IBuildpathEntry entry) throws ModelException {
		final List cp = new ArrayList();
		cp.addAll(Arrays.asList(project.getRawBuildpath()));
		assertTrue(cp.remove(entry));
		IBuildpathEntry[] newCP = (IBuildpathEntry[]) cp
				.toArray(new IBuildpathEntry[cp.size()]);
		IModelStatus status = BuildpathEntry.validateBuildpath(project, newCP);
		assertEquals(IStatus.OK, status.getSeverity());
		project.setRawBuildpath(newCP, null);
	}

	private void checkModel(RubyMixinModel model, String[] childrenNames) {
		IMixinElement element = model.getRawModel().get(
				TEST_CLASS_NAME + RubyMixin.INSTANCE_SUFFIX);
		assertNotNull(element);
		IMixinElement[] children = element.getChildren();
		assertNotNull(children);
		assertEquals(childrenNames.length, children.length);
		final Set names = new HashSet();
		for (int i = 0; i < children.length; ++i) {
			names.add(children[i].getLastKeySegment());
		}
		for (int i = 0; i < childrenNames.length; ++i) {
			assertTrue(childrenNames[i] + " is not found", names
					.contains(childrenNames[i]));
		}
	}

}
