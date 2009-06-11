/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.typeinference;

import org.eclipse.dltk.ast.references.VariableKind;
import org.eclipse.dltk.ti.IContext;
import org.eclipse.dltk.ti.goals.AbstractTypeGoal;

/**
 * Type of a class/instance/global/constant variables
 * 
 */
public class VariableTypeGoal extends AbstractTypeGoal {

	private final String name;
	private final String parentKey;
	private final VariableKind kind;

	public VariableTypeGoal(IContext context, String name, String parent,
			VariableKind kind) {
		super(context);
		this.name = name;
		parentKey = parent;
		this.kind = kind;
	}

	public VariableKind getKind() {
		return kind;
	}

	public String getName() {
		return name;
	}

	public String getParentKey() {
		return parentKey;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((parentKey == null) ? 0 : parentKey.hashCode());
		return result;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		VariableTypeGoal other = (VariableTypeGoal) obj;
		if (kind != other.kind)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parentKey == null) {
			if (other.parentKey != null)
				return false;
		} else if (!parentKey.equals(other.parentKey))
			return false;
		return true;
	}

	public String toString() {
		return getClassName() + "[" + name + "," + parentKey + "," + kind + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$
	}

}
