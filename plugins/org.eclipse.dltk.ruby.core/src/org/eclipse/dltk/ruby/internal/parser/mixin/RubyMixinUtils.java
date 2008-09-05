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
package org.eclipse.dltk.ruby.internal.parser.mixin;

public class RubyMixinUtils {

	public static boolean isObjectOrKernel(String key) {
		return OBJECT.equals(key) || OBJECT_INSTANCE.equals(key)
				|| KERNEL.equals(key) || KERNEL_INSTANCE.equals(key);
	}

	public static final String OBJECT = "Object"; //$NON-NLS-1$

	public static final String OBJECT_INSTANCE = OBJECT
			+ RubyMixin.INSTANCE_SUFFIX;

	public static final String KERNEL = "Kernel"; //$NON-NLS-1$

	public static final String KERNEL_INSTANCE = KERNEL
			+ RubyMixin.INSTANCE_SUFFIX;

}
