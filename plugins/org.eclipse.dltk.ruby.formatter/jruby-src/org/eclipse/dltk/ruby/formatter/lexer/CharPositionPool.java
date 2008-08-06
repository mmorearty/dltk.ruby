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

import java.util.ArrayList;
import java.util.List;

public class CharPositionPool {

	private final List unused = new ArrayList();

	public CharRecord create(int ch, int line, int column, int offset) {
		if (unused.isEmpty()) {
			return new CharRecord(ch, line, column, offset);
		} else {
			final CharRecord result = (CharRecord) unused.remove(unused
					.size() - 1);
			result.ch = ch;
			result.line = line;
			result.column = column;
			result.offset = offset;
			return result;
		}
	}

	public void release(CharRecord position) {
		position.ch = -1;
		position.line = -1;
		position.column = -1;
		position.offset = -1;
		unused.add(position);
	}

}
