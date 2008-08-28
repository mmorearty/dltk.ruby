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
package org.eclipse.dltk.ruby.testing.internal;

import java.util.Stack;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.ast.RubyClassDeclaration;

public class AbstractTestingEngineValidateVisitor extends ASTVisitor {

	private static final String REQUIRE = "require"; //$NON-NLS-1$

	private final Stack stack = new Stack();

	/**
	 * @param call
	 * @param methods
	 * @return
	 */
	protected boolean isMethodCall(CallExpression call, String[] methods) {
		if (call.getReceiver() != null) {
			return false;
		}
		final String methodName = call.getName();
		for (int i = 0; i < methods.length; ++i) {
			if (methodName.equals(methods[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param nodeClass
	 * @return
	 */
	protected boolean isNodeOnStack(Class nodeClass) {
		for (int i = stack.size(); --i >= 0;) {
			if (nodeClass.isAssignableFrom(stack.get(i).getClass())) {
				return true;
			}
		}
		return false;
	}

	public boolean visitGeneral(ASTNode node) throws Exception {
		stack.push(node);
		return super.visitGeneral(node);
	}

	public void endvisitGeneral(ASTNode node) throws Exception {
		stack.pop();
		super.endvisitGeneral(node);
	}

	protected boolean isMethodPrefix(final MethodDeclaration method,
			final String prefix) {
		final String methodName = method.getName();
		return methodName.startsWith(prefix)
				&& methodName.length() > prefix.length();
	}

	protected boolean isSuperClassOf(RubyClassDeclaration declaration,
			final String className) {
		return declaration.getSuperClassNames().contains(className);
	}

	private boolean isRequire(CallExpression call) {
		return call.getReceiver() == null && REQUIRE.equals(call.getName())
				&& call.getArgs().getChilds().size() == 1;
	}

	protected boolean isRequire(CallExpression call, String moduleName) {
		if (isRequire(call)) {
			ASTNode argument = (ASTNode) call.getArgs().getChilds().get(0);
			if (argument instanceof RubyCallArgument) {
				RubyCallArgument callArgument = (RubyCallArgument) argument;
				if (callArgument.getValue() instanceof StringLiteral) {
					StringLiteral literal = (StringLiteral) callArgument
							.getValue();
					if (moduleName.equals(literal.getValue())) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
