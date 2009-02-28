/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.ui.tests.folding;

import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ruby.internal.ui.text.folding.RubyFoldingStructureProvider;
import org.eclipse.dltk.ruby.ui.tests.internal.RubyUITestsPlugin;
import org.eclipse.dltk.ruby.ui.tests.internal.TestUtils;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;

public class RubyFoldingTest extends TestCase {

	private class MyRubyASTFoldingStructureProvider extends
			RubyFoldingStructureProvider {
		public boolean codeFolding = true;
		public boolean commentFolding = true;

		protected FoldingStructureComputationContext createInitialContext() {
			initializePreferences(fStore);
			fCommentsFolding = commentFolding;
			return createContext(true);
		}

		protected FoldingStructureComputationContext createContext(
				boolean allowCollapse) {
			ProjectionAnnotationModel model = new ProjectionAnnotationModel();

			IDocument doc = getDocument();
			if (doc == null)
				return null;

			return new FoldingStructureComputationContext(doc, model,
					allowCollapse);
		}

		Document fDocument;

		public void setDocument(Document doc) {
			fDocument = doc;
		}

		protected IDocument getDocument() {
			return fDocument;
		}

		public Map testComputeFoldingStructure(String contents,
				FoldingStructureComputationContext ctx) {
			super.computeFoldingStructure(contents, ctx);
			return ctx.getMap();
		}

		protected boolean mayCollapse(ASTNode s,
				FoldingStructureComputationContext ctx) {
			return codeFolding && super.mayCollapse(s, ctx);
		}

	};

	IPreferenceStore fStore;
	MyRubyASTFoldingStructureProvider provider;

	protected void setUp() throws Exception {
		super.setUp();
		fStore = RubyUITestsPlugin.getDefault().getPreferenceStore();
		RubyPreferenceConstants.initializeDefaultValues(fStore);
		provider = new MyRubyASTFoldingStructureProvider();
	}

	public void test0() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		String content = "#ab\n#dc\n";
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(1, result.size());
	}

	public void test177924a() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		String content = TestUtils.getData("resources/folding/b177924.rb");
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(3 + 3, result.size());
	}

	public void test177924b() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 4);
		String content = TestUtils.getData("resources/folding/b177924.rb");
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(2 + 3, result.size());
	}

	public void test193174a() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		String content = TestUtils.getData("resources/folding/b193174.rb");
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.codeFolding = false;
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(27, result.size());
	}

	public void test193174b() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		String content = TestUtils.getData("resources/folding/b193174.rb");
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.commentFolding = false;
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(73 + 13 + 1, result.size());
	}

	public void test193174c() throws Exception {
		fStore.setValue(PreferenceConstants.EDITOR_FOLDING_LINES_LIMIT, 2);
		String content = TestUtils.getData("resources/folding/b193174.rb");
		Document document = new Document(content);
		TestUtils.installStuff(document);
		provider.setDocument(document);
		Map result = provider.testComputeFoldingStructure(content, provider
				.createInitialContext());
		assertEquals(100 + 13 + 1, result.size());
	}
}
