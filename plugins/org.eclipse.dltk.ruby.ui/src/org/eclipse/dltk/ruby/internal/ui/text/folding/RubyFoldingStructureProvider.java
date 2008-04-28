/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text.folding;

import org.eclipse.core.runtime.ILog;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
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

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#initiallyCollapse(org.eclipse.dltk.ast.statements.Statement,
	 *      org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider.FoldingStructureComputationContext)
	 */
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

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#mayCollapse(org.eclipse.dltk.ast.statements.Statement,
	 *      org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider.FoldingStructureComputationContext)
	 */
	protected boolean mayCollapse(ASTNode s,
			FoldingStructureComputationContext ctx) {
		return s instanceof MethodDeclaration;
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getCommentPartitionType()
	 */
	protected String getCommentPartition() {
		return IRubyPartitions.RUBY_COMMENT;
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getPartition()
	 */
	protected String getPartition() {
		return IRubyPartitions.RUBY_PARTITIONING;
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getPartitionScanner()
	 */
	protected IPartitionTokenScanner getPartitionScanner() {
		return new RubyPartitionScanner();
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getPartitionTypes()
	 */
	protected String[] getPartitionTypes() {
		return IRubyPartitions.RUBY_PARTITION_TYPES;
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getNatureId()
	 */
	protected String getNatureId() {
		return RubyNature.NATURE_ID;
	}

	/*
	 * @see org.eclipse.dltk.ui.text.folding.AbstractASTFoldingStructureProvider#getLog()
	 */
	protected ILog getLog() {
		return RubyUI.getDefault().getLog();
	}

}
