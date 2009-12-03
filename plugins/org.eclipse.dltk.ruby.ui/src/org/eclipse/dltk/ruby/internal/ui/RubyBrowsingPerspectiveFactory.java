/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.ruby.internal.ui.wizards.RubyNewClassWizard;
import org.eclipse.dltk.ruby.internal.ui.wizards.RubyNewFileWizard;
import org.eclipse.dltk.ruby.internal.ui.wizards.RubyNewModuleWizard;
import org.eclipse.dltk.ruby.internal.ui.wizards.RubyNewProjectWizard;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IPlaceholderFolderLayout;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class RubyBrowsingPerspectiveFactory implements IPerspectiveFactory {
	public static final String PERSPECTIVE_ID = "org.eclipse.dltk.ruby.ui.RubyBrowsingPerspective"; //$NON-NLS-1$
	public static final String ID_PROJECTS_VIEW = "org.eclipse.dltk.ruby.ui.Projects"; //$NON-NLS-1$
	public static final String ID_PACKAGES_VIEW = "org.eclipse.dltk.ruby.ui.extbrowsing"; //$NON-NLS-1$
	public static final String ID_MEMBERS_VIEW = "org.eclipse.dltk.ruby.ui.Members"; //$NON-NLS-1$

	public static final String ID_ACTION_SET = "org.eclipse.dltk.ruby.ui.RubyActionSet"; //$NON-NLS-1$
	/*
	 * XXX: This is a workaround for:
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13070
	 */
	static IModelElement fgModelElementFromAction;

	/**
	 * Constructs a new Default layout engine.
	 */
	public RubyBrowsingPerspectiveFactory() {
		super();
	}

	public void createInitialLayout(IPageLayout layout) {
		createHorizontalLayout(layout);

		// views - standard workbench
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_RES_NAV);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
		addActionSets(layout);
		addShowViewShortcuts(layout);
		addNewWizardShortcuts(layout);
		addPerspectiveShotcuts(layout);
	}

	private void createHorizontalLayout(IPageLayout layout) {
		String relativePartId = IPageLayout.ID_EDITOR_AREA;
		int relativePos = IPageLayout.TOP;

		if (shouldShowProjectsView()) {
			layout.addView(ID_PROJECTS_VIEW, IPageLayout.TOP, (float) 0.25,
					IPageLayout.ID_EDITOR_AREA);
			relativePartId = ID_PROJECTS_VIEW;
			relativePos = IPageLayout.RIGHT;
		}
		if (shouldShowPackagesView()) {
			layout.addView(ID_PACKAGES_VIEW, relativePos, (float) 0.25,
					relativePartId);
			relativePartId = ID_PACKAGES_VIEW;
			relativePos = IPageLayout.RIGHT;
		}
		layout.addView(ID_MEMBERS_VIEW, IPageLayout.RIGHT, (float) 0.75,
				ID_PACKAGES_VIEW);

		IFolderLayout placeHolderLeft = layout
				.createFolder(
						"left", IPageLayout.LEFT, (float) 0.25, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
		placeHolderLeft.addPlaceholder(DLTKUIPlugin.ID_SCRIPTEXPLORER);
		placeHolderLeft.addPlaceholder("org.eclipse.dltk.testing.ResultView"); //$NON-NLS-1$
		placeHolderLeft.addPlaceholder(IPageLayout.ID_RES_NAV);

		IFolderLayout folderRight = layout
				.createFolder(
						"right", IPageLayout.RIGHT, (float) 0.75, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
		folderRight.addView(IPageLayout.ID_OUTLINE);

		IPlaceholderFolderLayout placeHolderBottom = layout
				.createPlaceholderFolder(
						"bottom", IPageLayout.BOTTOM, (float) 0.75, IPageLayout.ID_EDITOR_AREA); //$NON-NLS-1$
		placeHolderBottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
		placeHolderBottom.addPlaceholder(IPageLayout.ID_BOOKMARKS);

		placeHolderBottom.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);
	}

	protected void addNewWizardShortcuts(IPageLayout layout) {
		layout.addNewWizardShortcut(RubyNewProjectWizard.WIZARD_ID);
		layout.addNewWizardShortcut(RubyNewClassWizard.WIZARD_ID);
		layout.addNewWizardShortcut(RubyNewModuleWizard.WIZARD_ID);
		layout.addNewWizardShortcut(RubyNewFileWizard.WIZARD_ID);

		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//$NON-NLS-1$
		layout.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//$NON-NLS-1$
		layout
				.addNewWizardShortcut("org.eclipse.ui.editors.wizards.UntitledTextFileWizard");//$NON-NLS-1$
	}

	protected void addPerspectiveShotcuts(IPageLayout layout) {
		layout
				.addPerspectiveShortcut("org.eclipse.dltk.ruby.ui.RubyPerspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective"); //$NON-NLS-1$
		layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //$NON-NLS-1$
		layout
				.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective"); //$NON-NLS-1$
	}

	protected void addShowViewShortcuts(IPageLayout layout) {
		layout.addShowViewShortcut(ID_PROJECTS_VIEW);
		layout.addShowViewShortcut(ID_PACKAGES_VIEW);
		layout.addShowViewShortcut(ID_MEMBERS_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_OUTLINE);
		layout.addShowViewShortcut(IPageLayout.ID_PROBLEM_VIEW);
		layout.addShowViewShortcut(IPageLayout.ID_TASK_LIST);
		layout.addShowViewShortcut(IProgressConstants.PROGRESS_VIEW_ID);
		layout.addShowViewShortcut(DLTKUIPlugin.ID_SCRIPT_EXPLORER);
		layout.addShowViewShortcut(IConsoleConstants.ID_CONSOLE_VIEW);
		layout
				.addShowViewShortcut("org.eclipse.dltk.ruby.ui.RubyDocumentationView"); //$NON-NLS-1$
		layout.addShowViewShortcut(NewSearchUI.SEARCH_VIEW_ID);
		layout.addShowViewShortcut("org.eclipse.dltk.ui.TypeHierarchy"); //$NON-NLS-1$
		layout.addShowViewShortcut("org.eclipse.dltk.callhierarchy.view"); //$NON-NLS-1$
	}

	protected void addActionSets(IPageLayout layout) {
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
		layout.addActionSet(ID_ACTION_SET);
		layout.addActionSet("org.eclipse.debug.ui.launchActionSet"); //$NON-NLS-1$
	}

	private boolean shouldShowProjectsView() {
		return fgModelElementFromAction == null
				|| fgModelElementFromAction.getElementType() == IModelElement.SCRIPT_MODEL;
	}

	private boolean shouldShowPackagesView() {
		if (fgModelElementFromAction == null)
			return true;
		int type = fgModelElementFromAction.getElementType();
		return type == IModelElement.SCRIPT_MODEL
				|| type == IModelElement.SCRIPT_PROJECT
				|| type == IModelElement.PROJECT_FRAGMENT;
	}

	/*
	 * XXX: This is a workaround for:
	 * http://dev.eclipse.org/bugs/show_bug.cgi?id=13070
	 */
	static void setInputFromAction(IAdaptable input) {
		if (input instanceof IModelElement)
			fgModelElementFromAction = (IModelElement) input;
		else
			fgModelElementFromAction = null;
	}

}
