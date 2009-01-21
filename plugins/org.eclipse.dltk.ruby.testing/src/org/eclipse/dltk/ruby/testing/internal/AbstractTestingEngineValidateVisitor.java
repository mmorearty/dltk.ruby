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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.search.indexing.IIndexConstants;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.ast.RubyClassDeclaration;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinClass;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinModel;
import org.eclipse.dltk.ruby.typeinference.RubyClassType;

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

	protected boolean isSuperClassOf(ISourceModule module,
			RubyClassDeclaration declaration, final String className) {
		if (declaration.getSuperClassNames().contains(className))
			return true;
		else {
			String classKey = className.replaceAll("::", //$NON-NLS-1$
					String.valueOf(IIndexConstants.SEPARATOR));
			Set processedKeys = new HashSet();
			for (Iterator iter = declaration.getSuperClassNames().iterator(); iter
					.hasNext();) {
				String superClass = (String) iter.next();
				RubyMixinModel model = RubyMixinModel.getInstance(module
						.getScriptProject());
				RubyMixinClass mixinClass = model
						.createRubyClass(new RubyClassType(superClass
								.replaceAll("::", String //$NON-NLS-1$
										.valueOf(IIndexConstants.SEPARATOR))));
				if (mixinClass != null)
					// ssanders - Already know that it's indirect, because of
					// contains() above
					mixinClass = mixinClass.getSuperclass();
				while (mixinClass != null) {
				  if (processedKeys.add(mixinClass.getKey()) == true) {
					  if (mixinClass.getKey().equals(classKey))
						  return true;
					  mixinClass = mixinClass.getSuperclass();
				  }
				  else {
					  mixinClass = null;
				  }
				}
			}
		}
		return false;
	}

	private boolean isRequire(CallExpression call) {
		return call.getReceiver() == null && REQUIRE.equals(call.getName())
				&& call.getArgs().getChilds().size() == 1;
	}

	protected boolean isRequire(CallExpression call, String moduleName) {
		if (isRequire(call)) {
			final ASTNode argument = (ASTNode) call.getArgs().getChilds()
					.get(0);
			return isStringLiteralArgument(argument, moduleName);
		}
		return false;
	}

	protected boolean isStringLiteralArgument(ASTNode argument, String value) {
		if (argument instanceof RubyCallArgument) {
			RubyCallArgument callArg = (RubyCallArgument) argument;
			if (callArg.getValue() instanceof StringLiteral) {
				StringLiteral literal = (StringLiteral) callArg.getValue();
				if (value.equals(literal.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

}
