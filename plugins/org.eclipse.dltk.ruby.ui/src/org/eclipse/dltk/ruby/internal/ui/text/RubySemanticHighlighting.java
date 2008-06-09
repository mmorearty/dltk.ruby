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

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.internal.ui.editor.semantic.highlighting.SemanticHighlighting;

public class RubySemanticHighlighting extends SemanticHighlighting {

	private final String preferenceKey;

	public RubySemanticHighlighting(String preferenceKey) {
		Assert.isNotNull(preferenceKey);
		this.preferenceKey = preferenceKey;
	}

	public String getPreferenceKey() {
		return preferenceKey;
	}

	public int hashCode() {
		return preferenceKey.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof RubySemanticHighlighting) {
			final RubySemanticHighlighting other = (RubySemanticHighlighting) obj;
			return preferenceKey.equals(other.preferenceKey);
		}
		return false;
	}

}
