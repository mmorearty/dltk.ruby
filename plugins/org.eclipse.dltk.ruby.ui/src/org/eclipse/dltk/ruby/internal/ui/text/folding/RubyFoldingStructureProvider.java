/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text.folding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.ILog;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ruby.internal.ui.text.IRubyPartitions;
import org.eclipse.dltk.ruby.internal.ui.text.RubyPartitionScanner;
import org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class RubyFoldingStructureProvider extends
		AbstractASTFoldingStructureProvider {

	protected String getCommentPartition() {
		return IRubyPartitions.RUBY_COMMENT;
	}

	protected String getDocPartition() {
		return IRubyPartitions.RUBY_DOC;
	}

	protected String getPartition() {
		return IRubyPartitions.RUBY_PARTITIONING;
	}

	protected IPartitionTokenScanner getPartitionScanner() {
		return new RubyPartitionScanner();
	}

	protected String[] getPartitionTypes() {
		return IRubyPartitions.RUBY_PARTITION_TYPES;
	}

	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	protected ILog getLog() {
		return RubyUI.getDefault().getLog();
	}

	protected CodeBlock[] getCodeBlocks(String code, int offset) {
		ISourceParser parser = getSourceParser();
		ModuleDeclaration decl = parser.parse(null, code.toCharArray(), null);
		if (decl instanceof FakeModuleDeclaration) {
			return null;
		}
		return buildCodeBlocks(decl, offset);
	}

	/**
	 * This folding visitor implementation intentionally does not fold top level
	 * classes. This behavior is similar to the JDT.
	 */
	protected static class RubyFoldingASTVisitor extends FoldingASTVisitor {

		static class DeclarationContainer {
			final List children = new ArrayList();
			final Declaration declaration;
			final boolean foldAlways;

			public DeclarationContainer(Declaration declaration,
					boolean foldAlways) {
				this.declaration = declaration;
				this.foldAlways = foldAlways;
			}

			void addChild(DeclarationContainer child) {
				children.add(child);
			}

			int countChildren() {
				return children.size();
			}

			public String toString() {
				return declaration != null ? declaration.toString() : "<TOP>";
			}

		}

		private final Stack declarations = new Stack();

		private DeclarationContainer peekDeclaration() {
			return (DeclarationContainer) declarations.peek();
		}

		private DeclarationContainer popDeclaration() {
			return (DeclarationContainer) declarations.pop();
		}

		protected RubyFoldingASTVisitor(int offset) {
			super(offset);
		}

		public boolean visit(ModuleDeclaration s) throws Exception {
			declarations.push(new DeclarationContainer(null, false));
			return visitGeneral(s);
		}

		public boolean visit(TypeDeclaration s) throws Exception {
			final DeclarationContainer child = new DeclarationContainer(s,
					false);
			peekDeclaration().addChild(child);
			declarations.push(child);
			return visitGeneral(s);
		}

		public boolean endvisit(TypeDeclaration s) throws Exception {
			declarations.pop();
			return super.endvisit(s);
		}

		public boolean visit(MethodDeclaration s) throws Exception {
			final DeclarationContainer child = new DeclarationContainer(s, true);
			peekDeclaration().addChild(child);
			declarations.push(child);
			return visitGeneral(s);
		}

		public boolean endvisit(MethodDeclaration s) throws Exception {
			declarations.pop();
			return super.endvisit(s);
		}

		private void processDeclarations(DeclarationContainer container,
				int level, boolean collapsible) {
			if (container.declaration != null
					&& (collapsible || container.foldAlways)) {
				add(container.declaration);
			}
			for (Iterator i = container.children.iterator(); i.hasNext();) {
				final DeclarationContainer child = (DeclarationContainer) i
						.next();
				processDeclarations(child, level + 1, collapsible
						|| (level > 0 && container.countChildren() > 1));
			}
		}

		public boolean endvisit(ModuleDeclaration s) throws Exception {
			final DeclarationContainer container = popDeclaration();
			processDeclarations(container, 0, container.countChildren() > 1);
			return super.endvisit(s);
		}
	}

	protected FoldingASTVisitor getFoldingVisitor(int offset) {
		return new RubyFoldingASTVisitor(offset);
	}
}
