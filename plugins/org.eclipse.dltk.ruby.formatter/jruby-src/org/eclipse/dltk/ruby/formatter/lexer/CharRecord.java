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

public class CharRecord {

	int ch;
	int line;
	int column;
	int offset;

	/**
	 * @param ch
	 * @param column
	 * @param line
	 * @param offset
	 */
	public CharRecord(int ch, int column, int line, int offset) {
		this.ch = ch;
		this.column = column;
		this.line = line;
		this.offset = offset;
	}

	/**
	 * @return the ch
	 */
	public int getCh() {
		return ch;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	public String toString() {
		return "'" + (char) ch + "' offset=" + offset;
	}

}
