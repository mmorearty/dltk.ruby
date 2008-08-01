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
package org.eclipse.dltk.ruby.formatter.internal;

import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.formatter.nodes.AbstractFormatterNodeBuilder;
import org.eclipse.dltk.formatter.nodes.FormatterRootNode;
import org.eclipse.dltk.formatter.nodes.IFormatterContainerNode;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterTextNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterCaseNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterClassNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterForNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterMethodNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterUntilNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterWhenElseNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterWhenNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterWhileNode;
import org.eclipse.osgi.util.NLS;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.CaseNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.ForNode;
import org.jruby.ast.MethodDefNode;
import org.jruby.ast.ModuleNode;
import org.jruby.ast.Node;
import org.jruby.ast.UntilNode;
import org.jruby.ast.WhenNode;
import org.jruby.ast.WhileNode;
import org.jruby.ast.ext.ElseNode;
import org.jruby.ast.visitor.AbstractVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.ISourcePositionHolder;
import org.jruby.parser.RubyParserResult;

public class RubyFormatterNodeBuilder extends AbstractFormatterNodeBuilder {

	private final static boolean DEBUG = false;
	private int level;

	public IFormatterContainerNode build(RubyParserResult result,
			final IFormatterDocument document) {
		final IFormatterContainerNode root = new FormatterRootNode(document);
		start(root);
		result.getAST().accept(new AbstractVisitor() {

			protected Instruction visitNode(Node visited) {
				visitChildren(visited);
				return null;
			}

			public Instruction visitClassNode(ClassNode visited) {
				FormatterClassNode classNode = new FormatterClassNode(document);
				classNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(classNode);
				visitChildren(visited);
				checkedPop(classNode, visited.getEnd().getPosition()
						.getStartOffset());
				classNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitModuleNode(ModuleNode visited) {
				FormatterClassNode moduleNode = new FormatterClassNode(document);
				moduleNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(moduleNode);
				visitChildren(visited);
				checkedPop(moduleNode, visited.getEnd().getPosition()
						.getStartOffset());
				moduleNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitDefnNode(DefnNode visited) {
				return visitMethodDefNode(visited);
			}

			public Instruction visitDefsNode(DefsNode visited) {
				return visitMethodDefNode(visited);
			}

			private Instruction visitMethodDefNode(MethodDefNode visited) {
				FormatterMethodNode methodNode = new FormatterMethodNode(
						document);
				methodNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(methodNode);
				visitChildren(visited);
				checkedPop(methodNode, visited.getEnd().getPosition()
						.getStartOffset());
				methodNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitWhileNode(WhileNode visited) {
				if (!visited.isBlock())
					return null;
				FormatterWhileNode whileNode = new FormatterWhileNode(document);
				whileNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(whileNode);
				visitChildren(visited);
				checkedPop(whileNode, visited.getEnd().getPosition()
						.getStartOffset());
				whileNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitForNode(ForNode visited) {
				FormatterForNode forNode = new FormatterForNode(document);
				forNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(forNode);
				visitChildren(visited);
				checkedPop(forNode, visited.getEnd().getPosition()
						.getStartOffset());
				forNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitUntilNode(UntilNode visited) {
				if (!visited.isBlock())
					return null;
				FormatterUntilNode untilNode = new FormatterUntilNode(document);
				untilNode.setBegin(createTextNode(document, visited
						.getStartOffset(), visited.getBodyNode()
						.getStartOffset()));
				push(untilNode);
				visitChildren(visited);
				checkedPop(untilNode, visited.getEnd().getPosition()
						.getStartOffset());
				untilNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			public Instruction visitCaseNode(CaseNode visited) {
				FormatterCaseNode caseNode = new FormatterCaseNode(document);
				final int caseEnd = visited.getCaseNode() != null ? visited
						.getCaseNode().getEndOffset() : visited
						.getCaseKeyword().getPosition().getEndOffset();
				caseNode.setBegin(createTextNode(document, visited
						.getStartOffset(), caseEnd));
				push(caseNode);
				Node branch = visited.getFirstWhenNode();
				while (branch != null) {
					if (branch instanceof WhenNode) {
						WhenNode whenBranch = (WhenNode) branch;
						FormatterWhenNode whenNode = new FormatterWhenNode(
								document);
						whenNode.setBegin(createTextNode(document, branch
								.getStartOffset(), whenBranch
								.getExpressionNodes().getEndOffset()));
						push(whenNode);
						whenBranch.getBodyNode().accept(this);
						branch = ((WhenNode) branch).getNextCase();
						checkedPop(whenNode, branch != null ? branch
								.getStartOffset() : visited.getEnd()
								.getPosition().getStartOffset());
					} else if (branch instanceof ElseNode) {
						ElseNode elseBranch = (ElseNode) branch;
						FormatterWhenElseNode whenElseNode = new FormatterWhenElseNode(
								document);
						whenElseNode.setBegin(createTextNode(document,
								elseBranch.getStartOffset(), elseBranch
										.getElseKeyword().getPosition()
										.getEndOffset()));
						push(whenElseNode);
						elseBranch.getStatement().accept(this);
						checkedPop(whenElseNode, visited.getEnd().getPosition()
								.getStartOffset());
						branch = null;
					} else {
						RubyFormatterPlugin.warn(NLS.bind(
								"Unexpected {0} class in case/when expression",
								branch.getClass().getName()),
								new DumpStackOnly());
						break;
					}
				}
				checkedPop(caseNode, visited.getEnd().getPosition()
						.getStartOffset());
				caseNode.setEnd(createTextNode(document, visited.getEnd()));
				return null;
			}

			private void visitChildren(Node visited) {
				if (DEBUG) {
					for (int i = 0; i < level; ++i) {
						System.out.print(' ');
					}
					System.out.println(visited.getClass().getName());
				}
				++level;
				List children = visited.childNodes();
				for (Iterator i = children.iterator(); i.hasNext();) {
					final Node child = (Node) i.next();
					if (isVisitable(child)) {
						child.accept(this);
					}
				}
				--level;
			}

			private boolean isVisitable(Node node) {
				return !(node instanceof ArgumentNode);
			}

		});
		checkedPop(root, document.getLength());
		return root;
	}

	/**
	 * @param holder
	 * @return
	 */
	private IFormatterTextNode createTextNode(IFormatterDocument document,
			ISourcePositionHolder holder) {
		return createTextNode(document, holder.getPosition());
	}

	/**
	 * @param position
	 * @return
	 */
	private IFormatterTextNode createTextNode(IFormatterDocument document,
			ISourcePosition position) {
		return createTextNode(document, position.getStartOffset(), position
				.getEndOffset());
	}

}
