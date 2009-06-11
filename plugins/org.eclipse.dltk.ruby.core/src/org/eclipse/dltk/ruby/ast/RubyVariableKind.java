/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.ast;

import org.eclipse.dltk.ast.references.VariableKind;

public interface RubyVariableKind extends VariableKind {

	public class RubyImplementation extends Implementation implements
			RubyVariableKind {

		public RubyImplementation(VariableKind kind) {
			super(kind.getId());
		}
	}

	public static final RubyVariableKind LOCAL = new RubyImplementation(
			VariableKind.LOCAL);

	public static final RubyVariableKind GLOBAL = new RubyImplementation(
			VariableKind.GLOBAL);

	public static final RubyVariableKind INSTANCE = new RubyImplementation(
			VariableKind.INSTANCE);

	public static final RubyVariableKind CLASS = new RubyImplementation(
			VariableKind.CLASS);

	public static final RubyVariableKind CONSTANT = new RubyImplementation(
			VariableKind.GLOBAL);

}
