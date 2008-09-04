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

import org.eclipse.dltk.ast.declarations.FakeModuleDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.corext.SourceRange;

public class ResolverUtils {

	public static ISourceRange getSourceRange(final IMember member) {
		try {
			final ISourceRange sourceRange = member.getSourceRange();
			final ISourceRange nameRange = member.getNameRange();
			if (nameRange != null && nameRange.getLength() > 0) {
				return adjustRange(member.getSourceModule().getSource(),
						sourceRange.getOffset(), nameRange.getOffset()
								+ nameRange.getLength());
			} else {
				return sourceRange;
			}
		} catch (ModelException e) {
			return null;
		}
	}

	public static ISourceRange adjustRange(String source, int start, int end) {
		while (start < source.length()
				&& Character.isWhitespace(source.charAt(start))) {
			++start;
		}
		while (end > start && Character.isWhitespace(source.charAt(end - 1))) {
			--end;
		}
		return new SourceRange(start, end - start);
	}

	/**
	 * @param source
	 * @param range
	 * @return
	 */
	public static ISourceRange adjustRange(String source, ISourceRange range) {
		return adjustRange(source, range.getOffset(), range.getOffset()
				+ range.getLength());
	}

	public static ModuleDeclaration parse(ISourceModule module) {
		final ModuleDeclaration declaration = SourceParserUtil
				.getModuleDeclaration(module);
		if (declaration instanceof FakeModuleDeclaration) {
			return null;
		}
		return declaration;
	}

}
