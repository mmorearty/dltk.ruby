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
package org.eclipse.dltk.ruby.internal.ui.text;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;

public class RDocRule extends MultiLineRule {

	private static final String BEGIN = "=begin"; //$NON-NLS-1$

	private static final String END = "=end"; //$NON-NLS-1$

	/**
	 * @param token
	 */
	public RDocRule(IToken token) {
		super(BEGIN, END, token);
		setColumnConstraint(0);
	}

	/**
	 * Returns whether the end sequence was detected.
	 * 
	 * @param scanner
	 *            the character scanner to be used
	 * @return <code>true</code> if the end sequence has been detected
	 */
	protected boolean endSequenceDetected(ICharacterScanner scanner) {
		int readCount = 1;
		int c;
		while ((c = scanner.read()) != ICharacterScanner.EOF) {
			if (fEndSequence.length > 0 && c == fEndSequence[0]
					&& scanner.getColumn() == fColumn + 1) {
				// Check if the specified end sequence has been found.
				if (sequenceDetected(scanner, fEndSequence, true))
					return true;
			}
			readCount++;
		}
		for (; readCount > 0; readCount--)
			scanner.unread();
		return false;
	}
}
