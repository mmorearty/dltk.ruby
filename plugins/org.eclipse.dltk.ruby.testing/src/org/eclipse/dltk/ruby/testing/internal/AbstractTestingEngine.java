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
package org.eclipse.dltk.ruby.testing.internal;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.core.DLTKContributedExtension;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.testing.ITestingProcessor;

public abstract class AbstractTestingEngine extends DLTKContributedExtension {

	public ITestingProcessor getProcessor(ILaunch launch) {
		return null;
	}

	public void correctLaunchConfiguration(InterpreterConfig config,
			ILaunchConfiguration configuration, ILaunch launch) {
		// empty
	}

}
