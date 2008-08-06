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

public class CharHistory {

	private final CharPositionPool pool;
	private final List history = new ArrayList();

	/**
	 * @param pool
	 */
	public CharHistory(CharPositionPool pool) {
		this.pool = pool;
	}

	public boolean isEmpty() {
		return history.isEmpty();
	}

	public void addHead(int ch, int column, int offset) {
		history.add(0, pool.create(ch, column, offset));
	}

	public CharRecord getHead() {
		return (CharRecord) history.get(0);
	}

	public void removeHead() {
		CharRecord position = (CharRecord) history.remove(0);
		pool.release(position);
	}

	public void addTail(int ch, int column, int offset) {
		history.add(pool.create(ch, column, offset));
	}

	public CharRecord getTail() {
		return (CharRecord) history.get(history.size() - 1);
	}

	public void removeTail() {
		CharRecord position = (CharRecord) history
				.remove(history.size() - 1);
		pool.release(position);
	}

}
