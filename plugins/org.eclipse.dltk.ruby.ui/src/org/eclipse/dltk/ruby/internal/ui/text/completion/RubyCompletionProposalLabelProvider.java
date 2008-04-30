/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.text.completion;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.ruby.core.model.FakeMethod;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.text.completion.CompletionProposalLabelProvider;

public class RubyCompletionProposalLabelProvider extends
		CompletionProposalLabelProvider {

	private static final String SEPARATOR = " - "; //$NON-NLS-1$
	private static final String PACKAGE_SEPARATOR = "::"; //$NON-NLS-1$
	private static final String FOLDER_SEPARATOR = "/"; //$NON-NLS-1$

	protected String createMethodProposalLabel(CompletionProposal methodProposal) {
		StringBuffer nameBuffer = new StringBuffer();

		// method name
		nameBuffer.append(methodProposal.getName());

		// parameters
		nameBuffer.append('(');
		appendUnboundedParameterList(nameBuffer, methodProposal);
		nameBuffer.append(')');

		IMethod method = (IMethod) methodProposal.getModelElement();
		nameBuffer.append(SEPARATOR);
		if (method instanceof FakeMethod
				&& ((FakeMethod) method).getReceiver() != null) {
			nameBuffer.append(((FakeMethod) method).getReceiver());
		} else {
			IModelElement parent = method.getParent();
			if (parent instanceof IType) {
				IType type = (IType) parent;
				nameBuffer.append(type.getTypeQualifiedName(PACKAGE_SEPARATOR));
			} else {
				nameBuffer.append(parent.getElementName());
			}
		}

		return nameBuffer.toString();
	}

	protected String createOverrideMethodProposalLabel(
			CompletionProposal methodProposal) {
		StringBuffer nameBuffer = new StringBuffer();

		// method name
		nameBuffer.append(methodProposal.getName());

		// parameters
		nameBuffer.append('(');
		appendUnboundedParameterList(nameBuffer, methodProposal);
		nameBuffer.append(')');

		IMethod method = (IMethod) methodProposal.getModelElement();
		nameBuffer.append(SEPARATOR);
		if (method instanceof FakeMethod
				&& ((FakeMethod) method).getReceiver() != null) {
			String receiver = ((FakeMethod) method).getReceiver();
			nameBuffer.append(receiver);
		} else {
			IModelElement parent = method.getParent();
			if (parent instanceof IType) {
				IType type = (IType) parent;
				nameBuffer.append(type.getTypeQualifiedName(PACKAGE_SEPARATOR));
			} else {
				nameBuffer.append(parent.getElementName());
			}
		}

		return nameBuffer.toString();
	}

	protected String createTypeProposalLabel(CompletionProposal typeProposal) {
		final StringBuffer nameBuffer = new StringBuffer();
		nameBuffer.append(typeProposal.getName());
		final IType type = (IType) typeProposal.getModelElement();
		final IModelElement parent = type.getParent();
		final ISourceModule parentModule;
		if (parent instanceof IType) {
			nameBuffer.append(SEPARATOR);
			parentModule = appendType((IType) parent, nameBuffer);
		} else if (parent instanceof ISourceModule) {
			parentModule = (ISourceModule) parent;
		} else {
			parentModule = null;
		}
		if (parentModule != null) {
			nameBuffer.append(SEPARATOR);
			appendSourceModule(parentModule, nameBuffer);
		}
		return nameBuffer.toString();
	}

	private ISourceModule appendType(IType type, StringBuffer sb) {
		final IModelElement parent = type.getParent();
		if (parent instanceof IType) {
			final ISourceModule module = appendType((IType) parent, sb);
			sb.append(PACKAGE_SEPARATOR);
			sb.append(type.getElementName());
			return module;
		} else {
			sb.append(type.getElementName());
			if (parent instanceof ISourceModule) {
				return (ISourceModule) parent;
			} else {
				return null;
			}
		}
	}

	private void appendSourceModule(ISourceModule module, StringBuffer sb) {
		final IModelElement parent = module.getParent();
		final IProjectFragment fragment;
		if (parent instanceof IScriptFolder) {
			fragment = appendScriptFolder((IScriptFolder) parent, sb);
		} else {
			fragment = null;
		}
		sb.append(module.getElementName());
		if (fragment != null) {
			if (!fragment.isArchive()) {
				if (fragment.getPath().toString().startsWith(
						IBuildpathEntry.BUILTIN_EXTERNAL_ENTRY_STR)) {
					sb.append(' ');
					sb.append(ScriptElementLabels.BUILTINS_FRAGMENT);
				} else if (fragment.isExternal()) {
					sb.append(SEPARATOR);
					sb.append(EnvironmentPathUtils.getLocalPath(
							fragment.getPath()).toPortableString());
				}
			}
		}
	}

	private IProjectFragment appendScriptFolder(IScriptFolder folder,
			StringBuffer sb) {
		final IModelElement parent = folder.getParent();
		final IProjectFragment fragment;
		if (parent instanceof IScriptFolder) {
			fragment = appendScriptFolder((IScriptFolder) parent, sb);
		} else if (parent instanceof IProjectFragment) {
			fragment = (IProjectFragment) parent;
		} else {
			fragment = null;
		}
		if (!folder.isRootFolder()) {
			sb.append(folder.getElementName());
			sb.append(FOLDER_SEPARATOR);
		}
		return fragment;
	}

}
