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
package org.eclipse.dltk.ruby.formatter.lexer;

import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.RubyYaccLexer;
import org.jruby.lexer.yacc.Token;

public class HeredocToken extends Token {

	private final int func;

	/**
	 * @param value
	 * @param position
	 * @param func
	 */
	public HeredocToken(Object value, ISourcePosition position, int func) {
		super(value, position);
		this.func = func;
	}

	public boolean isIndent() {
		return (func & RubyYaccLexer.STR_FUNC_INDENT) != 0;
	}

}
