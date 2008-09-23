/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.ast;

import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.statements.Block;

public class RubyClassDeclaration extends RubyModuleDeclaration {

	private static final String OBJECT = "Object"; //$NON-NLS-1$
	private static final List OBJECT_SUPER_CLASS = Collections
			.singletonList(OBJECT);

	public RubyClassDeclaration(ASTNode superClass, ASTNode name, Block body,
			int start, int end) {
		super(name, body, start, end);
		this.setNameStart(name.sourceStart());
		this.setNameEnd(name.sourceEnd());
		this.addSuperClass(superClass);
	}

	public void traverse(ASTVisitor visitor) throws Exception {

		if (visitor.visit(this)) {
			if (this.getSuperClasses() != null) {
				this.getSuperClasses().traverse(visitor);
			}
			final ASTNode className = this.getClassName();
			if (className != null) {
				if (visitor instanceof IRubyASTVisitor) {
					((IRubyASTVisitor) visitor).visitTypeName(className);
				} else {
					className.traverse(visitor);
				}
			}
			if (this.getBody() != null) {
				getBody().traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}

	public List/* <String> */getSuperClassNames() {
		List names = super.getSuperClassNames();
		if (names == null || names.isEmpty()) {
			if (!OBJECT.equals(getName()))
				names = OBJECT_SUPER_CLASS;
		}
		return names;
	}

}
