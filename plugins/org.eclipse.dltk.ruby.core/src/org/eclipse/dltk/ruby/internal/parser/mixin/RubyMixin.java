/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.parser.mixin;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParserConstants;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceModuleInfoCache;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.eclipse.dltk.core.mixin.IMixinParser;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.ruby.core.RubyNature;

public class RubyMixin implements IMixinParser {

	public final static String INSTANCE_SUFFIX = "%"; // suffix for instance
														// //$NON-NLS-1$
	// classes

	public final static String VIRTUAL_SUFFIX = "%v"; // suffix for virtual
														// //$NON-NLS-1$
	// classes

	private IMixinRequestor requestor;

	public void parserSourceModule(boolean signature, ISourceModule module) {
		try {
			ISourceModuleInfoCache sourceModuleInfoCache = ModelManager
					.getModelManager().getSourceModuleInfoCache();
			ISourceModuleInfo sourceModuleInfo = sourceModuleInfoCache
					.get(module);
			ModuleDeclaration moduleDeclaration = SourceParserUtil
					.getModuleFromCache(sourceModuleInfo,
							ISourceParserConstants.DEFAULT);
			if (moduleDeclaration == null) {
				char[] content = module.getSourceAsCharArray();
				moduleDeclaration = SourceParserUtil.getModuleDeclaration(
						module.getPath().toOSString().toCharArray(), content,
						RubyNature.NATURE_ID, null, sourceModuleInfo);
			}
			RubyMixinBuildVisitor visitor = new RubyMixinBuildVisitor(
					moduleDeclaration, module, signature, requestor);
			moduleDeclaration.traverse(visitor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRequirestor(IMixinRequestor requestor) {
		this.requestor = requestor;
	}
}
