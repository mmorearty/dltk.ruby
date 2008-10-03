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
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.internal.core.ISourceCodeCache;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.ruby.core.tests.Activator;

public class SourceCacheTests extends AbstractModelTests {

	/**
	 * @param name
	 */
	public SourceCacheTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Suite suite() {
		return new Suite(SourceCacheTests.class);
	}

	private static final String PROJECT_NAME = "resource2";

	public void setUp() throws Exception {
		super.setUp();
		setUpScriptProject(PROJECT_NAME);
	}

	public void tearDown() throws Exception {
		deleteProject(PROJECT_NAME);
		super.tearDown();
	}

	public void testResourceChange() throws Exception {
		final ISourceCodeCache cache = ModelManager.getModelManager()
				.getSourceCodeCache();
		cache.beginOperation();
		try {
			final String fileName = "resource002.rb";
			ISourceModule module = getSourceModule(PROJECT_NAME,
					Util.EMPTY_STRING, fileName);
			final String className = "Resource001";
			final String source = module.getSource();
			assertTrue(source.indexOf(className) >= 0);
			// validate AST
			ModuleDeclaration declaration = SourceParserUtil
					.getModuleDeclaration(module);
			assertEquals(1, declaration.getTypes().length);
			assertEquals(className, declaration.getTypes()[0].getName());
			// validate model
			assertEquals(1, module.getAllTypes().length);
			assertEquals(className, module.getAllTypes()[0].getElementName());
			// getFile
			final IFile file = getWorkspaceRoot().getProject(PROJECT_NAME)
					.getFile(fileName);
			assertNotNull(cache.getContentsIfCached(file));
			// change content
			final String otherClassName = "Class001";
			final String newSource = source.replaceAll(className,
					otherClassName);
			assertFalse(source.equals(newSource));
			assertTrue(newSource.indexOf(otherClassName) >= 0);
			assertFalse(newSource.indexOf(className) >= 0);
			file.setContents(new ByteArrayInputStream(newSource.getBytes()),
					true, false, null);
			// test source cache reset
			assertNull(cache.getContentsIfCached(file));
			// validate AST is updated
			declaration = SourceParserUtil.getModuleDeclaration(module);
			assertEquals(1, declaration.getTypes().length);
			assertEquals(otherClassName, declaration.getTypes()[0].getName());
			// validate model is updated
			assertEquals(1, module.getAllTypes().length);
			assertEquals(otherClassName, module.getAllTypes()[0]
					.getElementName());
			// test source
			assertEquals(newSource, module.getSource());
		} finally {
			cache.endOperation();
		}
	}

	public void testResourceDelete() throws Exception {
		final ISourceCodeCache cache = ModelManager.getModelManager()
				.getSourceCodeCache();
		cache.beginOperation();
		try {
			final String fileName = "resource002.rb";
			ISourceModule module = getSourceModule(PROJECT_NAME,
					Util.EMPTY_STRING, fileName);
			final String className = "Resource001";
			final String source = module.getSource();
			assertTrue(source.indexOf(className) >= 0);
			final IFile file = getWorkspaceRoot().getProject(PROJECT_NAME)
					.getFile(fileName);
			assertNotNull(cache.getContentsIfCached(file));
			file.delete(true, null);
			assertNull(cache.getContentsIfCached(file));
		} finally {
			cache.endOperation();
		}
	}
}
