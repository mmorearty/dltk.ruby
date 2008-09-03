/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.typeinference;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.ti.BasicContext;
import org.eclipse.dltk.ti.IContext;
import org.eclipse.dltk.ti.IInstanceContext;
import org.eclipse.dltk.ti.types.IEvaluatedType;

public class MethodContext extends BasicContext implements IArgumentsContext, IInstanceContext {

	private final String[] argNames;

	private final IEvaluatedType[] argTypes;

	private IEvaluatedType instanceType;

	public MethodContext(IContext parent, ISourceModule sourceModule, ModuleDeclaration rootNode,
			String[] argNames, IEvaluatedType[] argTypes) {
		super(sourceModule, rootNode);

		this.argNames = argNames;
		this.argTypes = argTypes;
		if (parent instanceof IInstanceContext) {
			instanceType = ((IInstanceContext) parent).getInstanceType();
		}
	}

	public IEvaluatedType getArgumentType(String name) {
		for (int i = 0; i < argNames.length; i++) {
			String argName = argNames[i];
			if (name.equals(argName))
				if (i < argTypes.length)
					return argTypes[i];
				else
					return null;
		}
		return null;
	}

	public IEvaluatedType getInstanceType() {
		return instanceType;
	}

}
