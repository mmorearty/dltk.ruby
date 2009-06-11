/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.typeinference.goals;

import org.eclipse.dltk.ruby.ast.RubyColonExpression;
import org.eclipse.dltk.ti.IContext;
import org.eclipse.dltk.ti.goals.AbstractTypeGoal;

public class ColonExpressionGoal extends AbstractTypeGoal {

	private final RubyColonExpression expr;

	public ColonExpressionGoal(IContext context, RubyColonExpression expr) {
		super(context);
		this.expr = expr;
	}

	public RubyColonExpression getColonExpression() {
		return expr;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((expr == null) ? 0 : expr.hashCode());
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
		ColonExpressionGoal other = (ColonExpressionGoal) obj;
		if (expr == null) {
			if (other.expr != null)
				return false;
		} else if (!expr.equals(other.expr))
			return false;
		return true;
	}

}
