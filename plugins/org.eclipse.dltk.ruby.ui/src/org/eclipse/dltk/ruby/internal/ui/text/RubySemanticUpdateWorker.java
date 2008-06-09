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
package org.eclipse.dltk.ruby.internal.ui.text;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.semantic.highlighting.SemanticUpdateWorker;
import org.eclipse.dltk.ruby.ast.RubyEvaluatableStringExpression;
import org.eclipse.dltk.ruby.ast.RubyRegexpExpression;
import org.eclipse.dltk.ruby.ast.RubySymbolReference;
import org.eclipse.dltk.ruby.core.utils.RubySyntaxUtils;

public class RubySemanticUpdateWorker extends SemanticUpdateWorker {

	private static final int HL_REGEXP = 0;
	private static final int HL_STRING = 1;
	private static final int HL_SYMBOL = 2;
	private static final int HL_VARIABLE = 3;
	private static final int HL_EVAL_EXPR = 4;

	private final char[] content;

	/**
	 * @param presenter
	 * @param highlightings
	 * @param sourceModule
	 * @throws ModelException
	 */
	public RubySemanticUpdateWorker(ISourceModule sourceModule)
			throws ModelException {
		this.content = sourceModule.getSourceAsCharArray();
	}

	public boolean visitGeneral(ASTNode node) throws Exception {
		if (node instanceof RubyRegexpExpression) {
			handleRegexp(node);
		} else if (node instanceof RubySymbolReference) {
			addHighlightedPosition(node.sourceStart(), node.sourceEnd(),
					HL_SYMBOL);
		} else if (node instanceof VariableReference) {
			addHighlightedPosition(node.sourceStart(), node.sourceEnd(),
					HL_VARIABLE);
		} else if (node instanceof StringLiteral) {
			addHighlightedPosition(node.sourceStart(), node.sourceEnd(),
					HL_STRING);
		} else if (node instanceof RubyEvaluatableStringExpression) {
			handleEvaluatableExpression(node);
		}
		return true;
	}

	private void handleEvaluatableExpression(ASTNode node) {
		int start = node.sourceStart();
		int end = node.sourceEnd();
		if (content[start] == '#' && content[start + 1] == '{') {
			if (content[end - 1] == '\r') {
				// FIXME JRuby bug
				--end;
			}
			if (content[end - 1] == '}') {
				addHighlightedPosition(start, start + 2, HL_EVAL_EXPR);
				addHighlightedPosition(end - 1, end - 0, HL_EVAL_EXPR);
			}
		}
	}

	private void handleRegexp(ASTNode node) {
		int start = node.sourceStart();
		int end = node.sourceEnd();
		if (start >= 1 && content[start - 1] == '/' && end < content.length
				&& content[end] == '/') {
			--start;
			++end;
			while (end < content.length
					&& RubySyntaxUtils.isValidRegexpModifier(content[end])) {
				++end;
			}
		} else if (start >= 3 && content[start - 3] == '%'
				&& content[start - 2] == 'r') {
			char terminator = RubySyntaxUtils
					.getPercentStringTerminator(content[start - 1]);
			if (terminator != 0 && end < content.length
					&& content[end] == terminator) {
				start -= 3;
				++end;
				while (end < content.length
						&& RubySyntaxUtils.isValidRegexpModifier(content[end])) {
					++end;
				}
			}
		}
		addHighlightedPosition(start, end, HL_REGEXP);
	}

}
