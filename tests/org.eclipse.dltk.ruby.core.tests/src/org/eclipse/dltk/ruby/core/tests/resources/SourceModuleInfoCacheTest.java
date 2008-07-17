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
package org.eclipse.dltk.ruby.core.tests.resources;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceModuleInfoCache;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.ruby.core.tests.Activator;

public class SourceModuleInfoCacheTest extends AbstractModelTests {

	/**
	 * @param name
	 */
	public SourceModuleInfoCacheTest(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Suite suite() {
		return new Suite(SourceModuleInfoCacheTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		setUpScriptProject(PROJECT);
		waitUntilIndexesReady();
	}

	protected void tearDown() throws Exception {
		deleteProject(PROJECT);
		super.tearDown();
	}

	private ISourceModuleInfoCache getSourceModuleInfoCache() {
		return ModelManager.getModelManager().getSourceModuleInfoCache();
	}

	private static final String PROJECT = "resource1";

	/**
	 * Should be the same as {@link SourceParserUtil#AST}
	 */
	private static final String AST = "ast";

	public void testCacheResourceChanged() throws CoreException {
		final IScriptModel model = getScriptModel();
		final String resource1 = "resource001.rb";
		final IScriptProject project = model.getScriptProject(PROJECT);
		final ISourceModule module1 = (ISourceModule) project
				.findElement(new Path(resource1));
		ISourceModuleInfoCache getSourceModuleInfoCache = getSourceModuleInfoCache();
		final ISourceModuleInfoCache cache = getSourceModuleInfoCache;
		SourceParserUtil.getModuleDeclaration(module1);
		ISourceModuleInfoCache.ISourceModuleInfo cacheEntry = cache
				.get(module1);
		assertNotNull(cacheEntry);
		assertNotNull(cacheEntry.get(AST));
		IFile file = project.getProject().getFile(resource1);
		final String source = module1.getSource() + "\n\n" + "#END OF FILE";
		file.setContents(new ByteArrayInputStream(source.getBytes()), true,
				false, null);
		cacheEntry = cache.get(module1);
		assertNotNull(cacheEntry);
		assertNull(cacheEntry.get(AST));
	}

	public void testCacheResourceDeleted() throws CoreException {
		final IScriptModel model = getScriptModel();
		final String resource1 = "resource001.rb";
		final IScriptProject project = model.getScriptProject(PROJECT);
		final ISourceModule module1 = (ISourceModule) project
				.findElement(new Path(resource1));
		final ISourceModuleInfoCache cache = getSourceModuleInfoCache();
		SourceParserUtil.getModuleDeclaration(module1);
		ISourceModuleInfoCache.ISourceModuleInfo cacheEntry = cache
				.get(module1);
		assertNotNull(cacheEntry);
		assertNotNull(cacheEntry.get(AST));
		project.getProject().getFile(resource1).delete(true, null);
		cacheEntry = cache.get(module1);
		assertNotNull(cacheEntry);
		assertNull(cacheEntry.get(AST));
	}

}
