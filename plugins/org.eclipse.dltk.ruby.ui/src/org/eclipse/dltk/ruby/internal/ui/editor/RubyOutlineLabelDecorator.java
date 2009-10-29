/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.editor;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ruby.core.RubyConstants;
import org.eclipse.dltk.ruby.internal.ui.RubyImages;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.viewsupport.ImageDescriptorRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Image;

public class RubyOutlineLabelDecorator extends BaseLabelProvider implements
		ILabelDecorator {

	public String decorateText(String text, Object element) {
		// if (element instanceof IMethod) {
		// IMethod method = (IMethod) element;
		// try {
		// if (method.exists() && (method.getFlags() &
		// RubyConstants.RubyAliasModifier) != 0) {
		// String oldName = method.getParameters()[0];
		// return method.getElementName();// + " [alias for " + oldName + "]";
		// }
		// } catch (ModelException e) {
		// // Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return text;
	}

	private static ImageDescriptor getTopRight(int flags) {
		if ((flags & Modifiers.AccStatic) != 0) {
			return RubyImages.DESC_OVR_STATIC_FIELD;
		} else if ((flags & Modifiers.AccConstant) != 0) {
			return RubyImages.DESC_OVR_CONST_FIELD;
		} else {
			return null;
		}
	}

	private static ImageDescriptor getTopLeft(int flags) {
		if ((flags & RubyConstants.RubyAttributeModifier) != 0) {
			return DLTKPluginImages.DESC_OVR_ABSTRACT;
		} else if ((flags & RubyConstants.RubyAliasModifier) != 0) {
			return DLTKPluginImages.DESC_OVR_CALLER;
		} else {
			return null;
		}
	}

	public Image decorateImage(Image image, Object obj) {
		try {
			if (obj instanceof IMember) {
				final IMember member = (IMember) obj;
				if (member.exists()) {
					final int flags = member.getFlags();
					final ImageDescriptor left = getTopLeft(flags);
					final ImageDescriptor right = getTopRight(flags);
					if (left != null || right != null) {
						final ImageDescriptor[] decorations = new ImageDescriptor[5];
						decorations[IDecoration.TOP_LEFT] = left;
						decorations[IDecoration.TOP_RIGHT] = right;
						return registry.get(new DecorationOverlayIcon(image,
								decorations));
					}
				}
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return image;
	}

	private final ImageDescriptorRegistry registry = new ImageDescriptorRegistry(
			false);

	@Override
	public void dispose() {
		super.dispose();
		registry.dispose();
	}
}
