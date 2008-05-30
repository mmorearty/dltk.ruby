/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - Ñompleted initial version (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.debug.ui.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

/**
 * Provides links for stack traces
 */
public class RubyConsoleTracker implements IPatternMatchListenerDelegate {

	private TextConsole fConsole;

	public void connect(TextConsole console) {
		fConsole = console;
	}

	public void disconnect() {
		fConsole = null;
	}

	protected TextConsole getConsole() {
		return fConsole;
	}

	//	private static final String FROM = "from "; //$NON-NLS-1$
	//	private static final String FROM_E = "from -e"; //$NON-NLS-1$

	public void matchFound(PatternMatchEvent event) {
		try {
			int offset = event.getOffset();
			int length = event.getLength();
			String text = getConsole().getDocument().get(offset, length);
			// if (text.indexOf(FROM_E) != -1) {
			// return;
			// }
			// if (text.startsWith(FROM)) {
			// offset += FROM.length();
			// length -= FROM.length();
			// text = text.substring(FROM.length());
			// }
			if (RubyFileHyperlink.DEBUG) {
				System.out.println("linkText=\"" + text + '"'); //$NON-NLS-1$
			}
			if (isValidLink(text)) {
				final RubyFileHyperlink link = new RubyFileHyperlink(fConsole);
				fConsole.addHyperlink(link, offset, length);
			}
		} catch (BadLocationException e) {
		}
	}

	/**
	 * Validates this hyper link
	 * 
	 * @param offset
	 * @param length
	 * @return
	 */
	private boolean isValidLink(String text) {
		try {
			final String fileName = RubyFileHyperlink.extractFileName(text);
			if (fileName.length() > 1) {
				if (RubyFileHyperlink.DEBUG) {
					System.out.println("fileName='" + fileName + "'"); //$NON-NLS-1$//$NON-NLS-2$
				}
				return RubyFileHyperlink.findSourceModule(fileName) != null;
			}
		} catch (IllegalArgumentException e) {
			if (RubyFileHyperlink.DEBUG) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
