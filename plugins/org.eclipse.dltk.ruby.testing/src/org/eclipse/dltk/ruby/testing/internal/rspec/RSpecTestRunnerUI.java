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
package org.eclipse.dltk.ruby.testing.internal.rspec;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.ruby.testing.internal.AbstractRubyTestingEngine;
import org.eclipse.dltk.testing.AbstractTestRunnerUI;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.ITestElementResolver;
import org.eclipse.dltk.testing.ITestRunnerUI;
import org.eclipse.dltk.testing.TestElementResolution;
import org.eclipse.dltk.testing.model.ITestCaseElement;
import org.eclipse.dltk.testing.model.ITestElement;
import org.eclipse.osgi.util.NLS;

public class RSpecTestRunnerUI extends AbstractTestRunnerUI implements
		ITestRunnerUI, ITestElementResolver {

	private static final char CLASS_BEGIN = '<';

	private final IScriptProject project;
	private final AbstractRubyTestingEngine testingEngine;

	/**
	 * @param testingEngine
	 * @param project
	 */
	public RSpecTestRunnerUI(RspecTestingEngine testingEngine,
			IScriptProject project) {
		this.testingEngine = testingEngine;
		this.project = project;
	}

	/*
	 * @see org.eclipse.dltk.testing.ITestRunnerUI#getDisplayName()
	 */
	public String getDisplayName() {
		return testingEngine.getName();
	}

	/*
	 * @see AbstractTestRunnerUI#getTestCaseLabel(ITestCaseElement, boolean)
	 */
	public String getTestCaseLabel(ITestCaseElement caseElement, boolean full) {
		final String testName = caseElement.getTestName();
		int index = testName.lastIndexOf(CLASS_BEGIN);
		if (index >= 0) {
			if (full) {
				final String template = DLTKTestingMessages.TestSessionLabelProvider_testMethodName_className;
				return NLS.bind(template, testName.substring(index + 1),
						testName.substring(0, index));
			} else {
				return testName.substring(0, index);
			}
		}
		return testName;
	}

	/*
	 * @see AbstractTestRunnerUI#getTestStartedMessage(ITestCaseElement)
	 */
	public String getTestStartedMessage(ITestCaseElement caseElement) {
		final String testName = caseElement.getTestName();
		int index = testName.lastIndexOf(CLASS_BEGIN);
		if (index >= 0) {
			final String template = DLTKTestingMessages.TestRunnerViewPart_message_started;
			return NLS.bind(template, testName.substring(index + 1), testName
					.substring(0, index));
		}
		return testName;
	}

	/*
	 * @see AbstractTestRunnerUI#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (ITestElementResolver.class.equals(adapter)) {
			return this;
		} else {
			return super.getAdapter(adapter);
		}
	}

	/*
	 * @see ITestElementResolver#resolveElement(ITestElement)
	 */
	public TestElementResolution resolveElement(ITestElement element) {
		// TODO Auto-generated method stub
		return null;
	}

}
