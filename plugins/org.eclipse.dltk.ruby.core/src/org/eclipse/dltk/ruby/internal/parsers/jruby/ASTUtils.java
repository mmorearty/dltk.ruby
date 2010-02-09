/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.parsers.jruby;

import java.util.Stack;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.compiler.env.ModuleSource;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.ruby.ast.RubyBlock;
import org.eclipse.dltk.ruby.ast.RubyForStatement2;
import org.eclipse.dltk.ruby.ast.RubyIfStatement;
import org.eclipse.dltk.ruby.ast.RubyUnlessStatement;
import org.eclipse.dltk.ruby.ast.RubyUntilStatement;
import org.eclipse.dltk.ruby.ast.RubyWhileStatement;
import org.eclipse.dltk.ruby.core.RubyNature;

public class ASTUtils {

	private ASTUtils() {
		throw new AssertionError("Cannot instantiate utility class"); //$NON-NLS-1$
	}

	public static void setVisibility(MethodDeclaration methodDeclaration,
			int newVisibility) {
		int modifiers = methodDeclaration.getModifiers();
		modifiers = modifiers
				& ~(Modifiers.AccPublic | Modifiers.AccProtected
						| Modifiers.AccPrivate | Modifiers.AccDefault);
		methodDeclaration.setModifiers(modifiers | newVisibility);
	}

	public static ASTNode[] restoreWayToNode(ModuleDeclaration module,
			final ASTNode nde) {
		final Stack<ASTNode> stack = new Stack<ASTNode>();

		ASTVisitor visitor = new ASTVisitor() {
			boolean found = false;

			@Override
			public boolean visitGeneral(ASTNode node) throws Exception {
				if (!found) {
					stack.push(node);
					if (node.locationMatches(nde)) {
						found = true;
					}
				}
				return super.visitGeneral(node);
			}

			@Override
			public void endvisitGeneral(ASTNode node) throws Exception {
				super.endvisitGeneral(node);
				if (!found) {
					stack.pop();
				}
			}
		};

		try {
			module.traverse(visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stack.toArray(new ASTNode[stack.size()]);
	}

	public static <E extends ASTNode> E getEnclosingElement(Class<E> element,
			ASTNode[] wayToNode, ASTNode node, boolean considerGiven) {
		int pos = -1;
		for (int i = wayToNode.length; --i >= 0;) {
			if (wayToNode[i] == node) {
				pos = i;
				break;
			}
		}
		if (pos != -1) {
			if (!considerGiven)
				pos--;
			for (int i = pos; i >= 0; i--) {
				if (element.isInstance(wayToNode[i])) {
					@SuppressWarnings("unchecked")
					final E result = (E) wayToNode[i];
					return result;
				}
			}
		}

		return null;
	}

	public static TypeDeclaration getEnclosingType(ASTNode[] wayToNode,
			ASTNode node, boolean considerGiven) {
		return getEnclosingElement(TypeDeclaration.class, wayToNode, node,
				considerGiven);
	}

	public static CallExpression getEnclosingCallNode(ASTNode[] wayToNode,
			ASTNode node, boolean considerGiven) {
		return getEnclosingElement(CallExpression.class, wayToNode, node,
				considerGiven);
	}

	public static MethodDeclaration getEnclosingMethod(ASTNode[] wayToNode,
			ASTNode node, boolean considerGiven) {
		return getEnclosingElement(MethodDeclaration.class, wayToNode, node,
				considerGiven);
	}

	/**
	 * Finds minimal ast node, that covers given position
	 * 
	 * @param unit
	 * @param position
	 * @return
	 */
	public static ASTNode findMinimalNode(ModuleDeclaration unit, int start,
			int end) {

		class Visitor extends ASTVisitor {
			ASTNode result = null;
			final int start, end;

			public Visitor(int start, int end) {
				this.start = start;
				this.end = end;
			}

			public ASTNode getResult() {
				return result;
			}

			private int calcLen(ASTNode s) {
				int realStart = s.sourceStart();
				int realEnd = s.sourceEnd();
				if (s instanceof TypeDeclaration) {
					TypeDeclaration declaration = (TypeDeclaration) s;
					realStart = declaration.getNameStart();
					realEnd = declaration.getNameEnd();
				} else if (s instanceof MethodDeclaration) {
					MethodDeclaration declaration = (MethodDeclaration) s;
					realStart = declaration.getNameStart();
					realEnd = declaration.getNameEnd();
				}
				return realEnd - realStart;
			}

			public boolean visitGeneral(ASTNode s) throws Exception {
				int realStart = s.sourceStart();
				int realEnd = s.sourceEnd();
				if (s instanceof Block) {
					realStart = realEnd = -42; // never select on blocks
					// ssanders: BEGIN - Modify narrowing logic
				} else if (s instanceof TypeDeclaration) {
					TypeDeclaration declaration = (TypeDeclaration) s;
					realStart = declaration.sourceStart();
					realEnd = declaration.sourceEnd();
				} else if (s instanceof MethodDeclaration) {
					MethodDeclaration declaration = (MethodDeclaration) s;
					realStart = declaration.sourceStart();
					realEnd = declaration.sourceEnd();
				}
				if (realStart <= start && realEnd >= end) {
					if (result != null) {
						if ((s.sourceStart() >= result.sourceStart())
								&& (s.sourceEnd() <= result.sourceEnd()))
							result = s;
					} else {
						result = s;
					}
					// ssanders: END
					if (DLTKCore.DEBUG_SELECTION)
						System.out.println("Found " + s.getClass().getName()); //$NON-NLS-1$
				}
				return true;
			}

		}

		Visitor visitor = new Visitor(start, end);

		try {
			unit.traverse(visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return visitor.getResult();
	}

	/**
	 * Finds minimal ast node, that covers given position
	 * 
	 * @param unit
	 * @param position
	 * @return
	 */
	public static ASTNode findMaximalNodeEndingAt(ModuleDeclaration unit,
			final int boundaryOffset) {

		class Visitor extends ASTVisitor {
			ASTNode result = null;

			public ASTNode getResult() {
				return result;
			}

			@Override
			public boolean visitGeneral(ASTNode s) throws Exception {
				if (s.sourceStart() < 0 || s.sourceEnd() < 0)
					return true;
				int sourceEnd = s.sourceEnd();
				if (Math.abs(sourceEnd - boundaryOffset) <= 0) { // XXX: was
					// ... <= 1
					result = s;
					if (DLTKCore.DEBUG_SELECTION)
						System.out.println("Found " + s.getClass().getName()); //$NON-NLS-1$
				}
				return true;
			}

		}

		Visitor visitor = new Visitor();

		try {
			unit.traverse(visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return visitor.getResult();
	}

	public static ModuleDeclaration getAST(ISourceModule module) {
		return SourceParserUtil.getModuleDeclaration(module);
	}

	public static ModuleDeclaration getAST(char[] cs) {
		ISourceParser sourceParser = DLTKLanguageManager
				.getSourceParser(RubyNature.NATURE_ID);
		ModuleDeclaration declaration = (ModuleDeclaration) sourceParser.parse(
				new ModuleSource("RawSource" //$NON-NLS-1$
						, cs), null);
		return declaration;
	}

	public static boolean isNodeScoping(ASTNode node) {
		return (node instanceof RubyIfStatement
				|| node instanceof RubyForStatement2
				|| node instanceof RubyWhileStatement
				|| node instanceof RubyBlock
				|| node instanceof RubyUntilStatement
				|| node instanceof RubyUnlessStatement
				|| node instanceof TypeDeclaration || node instanceof MethodDeclaration);
	}

}
