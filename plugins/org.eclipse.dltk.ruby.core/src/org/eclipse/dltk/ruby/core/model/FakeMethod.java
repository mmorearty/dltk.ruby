/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.core.model;

import org.eclipse.dltk.core.IParameter;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.internal.core.SourceMethod;
import org.eclipse.dltk.internal.core.SourceMethodUtils;
import org.eclipse.dltk.internal.core.SourceRange;

public class FakeMethod extends SourceMethod {

	private String receiver;

	private IParameter[] parameters = SourceMethodUtils.NO_PARAMETERS;

	private int flags;

	private int offset;

	private int length;

	private boolean hasSpecialOffsets = false;

	private int nameOffset;

	private int nameLength;

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public FakeMethod(ModelElement parent, String name) {
		super(parent, name);
	}

	public FakeMethod(ModelElement parent, String name, int offset, int length,
			int nameOffset, int nameLength) {
		super(parent, name);
		this.offset = offset;
		this.length = length;
		this.nameOffset = nameOffset;
		this.nameLength = nameLength;
		hasSpecialOffsets = true;
	}

	@Override
	public ISourceRange getNameRange() throws ModelException {
		if (hasSpecialOffsets)
			return new SourceRange(nameOffset, nameLength);
		return super.getNameRange();
	}

	@Override
	public ISourceRange getSourceRange() throws ModelException {
		if (hasSpecialOffsets)
			return new SourceRange(offset, length);
		return super.getSourceRange();
	}

	@Override
	public IScriptProject getScriptProject() {
		return parent.getScriptProject();
	}

	@Override
	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public void setParameters(IParameter[] parameters) {
		this.parameters = parameters;
	}

	@Override
	public IParameter[] getParameters() throws ModelException {
		return parameters;
	}

	@Override
	public String[] getParameterNames() throws ModelException {
		return SourceMethodUtils.getParameterNames(parameters);
	}

}
