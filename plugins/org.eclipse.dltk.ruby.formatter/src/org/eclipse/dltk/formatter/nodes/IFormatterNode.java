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
package org.eclipse.dltk.formatter.nodes;

public interface IFormatterNode {

	int DEFAULT_OFFSET = 0;

	void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception;

	int getStartOffset();

	int getEndOffset();

	IFormatterDocument getDocument();

}
