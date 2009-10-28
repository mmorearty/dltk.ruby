/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.wizards;

import org.eclipse.dltk.ruby.core.RubyNature;
import org.eclipse.dltk.ruby.internal.ui.RubyImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.ProjectWizard;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;

public class RubyNewProjectWizard extends ProjectWizard {
	public static final String WIZARD_ID = "org.eclipse.dltk.ruby.wizards.newproject"; //$NON-NLS-1$

	private ProjectWizardFirstPage fFirstPage;
	private ProjectWizardSecondPage fSecondPage;

	public RubyNewProjectWizard() {
		setDefaultPageImageDescriptor(RubyImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(RubyWizardMessages.NewProjectWizard_title);
	}

	@Override
	public String getScriptNature() {
		return RubyNature.NATURE_ID;
	}

	@Override
	public void addPages() {
		super.addPages();
		fFirstPage = new ProjectWizardFirstPage() {

			@Override
			protected boolean interpeterRequired() {
				/*
				 * It is not allowed to create Ruby project without interpreter
				 * to minimize further user's problems.
				 */
				return true;
			}
		};

		// First page
		fFirstPage.setTitle(RubyWizardMessages.NewProjectFirstPage_title);
		fFirstPage
				.setDescription(RubyWizardMessages.NewProjectFirstPage_description);
		addPage(fFirstPage);

		// Second page
		fSecondPage = new ProjectWizardSecondPage(fFirstPage);
		addPage(fSecondPage);
	}

}
