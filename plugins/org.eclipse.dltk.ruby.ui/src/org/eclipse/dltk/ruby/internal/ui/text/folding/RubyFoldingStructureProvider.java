/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text.folding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.ILog;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.internal.ui.RubyPreferenceConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyUI;
import org.eclipse.dltk.ruby.internal.ui.text.IRubyPartitions;
import org.eclipse.dltk.ruby.internal.ui.text.RubyPartitionScanner;
import org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;

public class RubyFoldingStructureProvider extends
		AbstractASTFoldingStructureProvider {

	/* preferences */
	private boolean fInitCollapseComments;
	private boolean fInitCollapseHeaderComments;
	private boolean fInitCollapseMethods;

	protected void initializePreferences(IPreferenceStore store) {
		super.initializePreferences(store);
		fFoldNewLines = true;
		fCommentsFolding = true;
		fInitCollapseComments = store
				.getBoolean(RubyPreferenceConstants.EDITOR_FOLDING_INIT_COMMENTS);
		fInitCollapseHeaderComments = store
				.getBoolean(RubyPreferenceConstants.EDITOR_FOLDING_INIT_HEADER_COMMENTS);
		fInitCollapseMethods = store
				.getBoolean(RubyPreferenceConstants.EDITOR_FOLDING_INIT_METHODS);
	}

	protected boolean initiallyCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return ctx.allowCollapsing() && s instanceof MethodDeclaration
				&& fInitCollapseMethods;
	}

	protected boolean initiallyCollapseComments(IRegion commentRegion,
			FoldingStructureComputationContext ctx) {
		if (ctx.allowCollapsing()) {
			return isHeaderRegion(commentRegion, ctx) ? fInitCollapseHeaderComments
					: fInitCollapseComments;
		}
		return false;
	}

	protected boolean mayCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return s instanceof MethodDeclaration || s instanceof TypeDeclaration;
	}

	protected String getCommentPartition() {
		return IRubyPartitions.RUBY_COMMENT;
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

	protected static class RubyFoldingASTVisitor extends FoldingASTVisitor {

		static class TypeContainer {
			final List children = new ArrayList();
			final TypeDeclaration type;

			public TypeContainer(TypeDeclaration type) {
				this.type = type;
			}

		}

		final Stack types = new Stack();

		protected RubyFoldingASTVisitor(int offset) {
			super(offset);
			types.push(new TypeContainer(null));
		}

		public boolean visit(TypeDeclaration s) throws Exception {
			final TypeContainer child = new TypeContainer(s);
			((TypeContainer) types.peek()).children.add(child);
			types.push(child);
			return visitGeneral(s);
		}

		public boolean endvisit(TypeDeclaration s) throws Exception {
			types.pop();
			return super.endvisit(s);
		}

		private void processType(TypeContainer container, int level,
				boolean collapsible) {
			if (collapsible) {
				add(container.type);
			}
			for (Iterator i = container.children.iterator(); i.hasNext();) {
				final TypeContainer child = (TypeContainer) i.next();
				processType(child, level + 1, collapsible
						|| (level > 0 && container.children.size() > 1));
			}
		}

		public boolean endvisit(ModuleDeclaration s) throws Exception {
			TypeContainer container = (TypeContainer) types.peek();
			processType(container, 0, false);
			return super.endvisit(s);
		}
	}

	protected FoldingASTVisitor getFoldingVisitor(int offset) {
		return new RubyFoldingASTVisitor(0);
	}

}
