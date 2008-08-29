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
package org.eclipse.dltk.ruby.testing.internal.testunit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.ruby.debug.RubyFilenameLinenumberResolver;
import org.eclipse.dltk.ruby.internal.debug.ui.console.RubyFileHyperlink;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.ruby.testing.internal.RubyOpenEditorAction;
import org.eclipse.dltk.testing.AbstractTestRunnerUI;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.jface.action.IAction;

public class TestUnitTestRunnerUI extends AbstractTestRunnerUI implements
		ITestRunnerUI {

	private final AbstractRubyTestingEngine testingEngine;

	/**
	 * @param testingEngine
	 */
	public TestUnitTestRunnerUI(AbstractRubyTestingEngine testingEngine) {
		this.testingEngine = testingEngine;
	}

	public String filterStackTrace(String trace) {
		// TODO implement filtering
		return trace;
	}

	private static final Pattern STACK_FRAME_IN_PATTERN = Pattern
			.compile("(.+):(\\d+):in `(.+)'"); //$NON-NLS-1$

	private static final Pattern STACK_FRAME_PATTERN = RubyFilenameLinenumberResolver
			.createPattern();

	public boolean isStackFrame(String line) {
		return STACK_FRAME_IN_PATTERN.matcher(line).matches()
				|| STACK_FRAME_PATTERN.matcher(line).matches();
	}

	public IAction createOpenEditorAction(String line) {
		Matcher matcher = STACK_FRAME_IN_PATTERN.matcher(line);
		if (!matcher.matches()) {
			matcher = STACK_FRAME_PATTERN.matcher(line);
		}
		if (matcher.matches()) {
			Object element = RubyFileHyperlink.findSourceModule(matcher
					.group(1));
			if (element != null) {
				final int lineNumber;
				try {
					lineNumber = Integer.parseInt(matcher.group(2));
				} catch (NumberFormatException e) {
					return null;
				}
				return new RubyOpenEditorAction(element, lineNumber);
			}
		}
		return null;
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestRunnerUI#getDisplayName()
	 */
	public String getDisplayName() {
		return testingEngine.getName();
	}
}
