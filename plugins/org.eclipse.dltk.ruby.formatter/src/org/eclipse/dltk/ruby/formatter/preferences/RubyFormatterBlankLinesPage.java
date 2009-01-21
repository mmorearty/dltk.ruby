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
package org.eclipse.dltk.ruby.formatter.preferences;

import java.net.URL;

import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RubyFormatterBlankLinesPage extends FormatterModifyTabPage {

	/**
	 * @param dialog
	 */
	public RubyFormatterBlankLinesPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		Group emptyLinesGroup = SWTFactory.createGroup(parent,
				Messages.RubyFormatterBlankLinesPage_blankLinesInSourceFile, 2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_FILE_AFTER_REQUIRE,
				Messages.RubyFormatterBlankLinesPage_afterRequireDirectives);
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_FILE_BETWEEN_MODULE,
				Messages.RubyFormatterBlankLinesPage_betweenModules);
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_FILE_BETWEEN_CLASS,
				Messages.RubyFormatterBlankLinesPage_betweenClasses);
		manager.createNumber(emptyLinesGroup,
				RubyFormatterConstants.LINES_FILE_BETWEEN_METHOD,
				Messages.RubyFormatterBlankLinesPage_betweenMethods);
		//
		Group emptyLinesInternalGroup = SWTFactory.createGroup(parent,
				Messages.RubyFormatterBlankLinesPage_blankLinesWithingClassModuleDeclarations, 2, 1,
				GridData.FILL_HORIZONTAL);
		manager.createNumber(emptyLinesInternalGroup,
				RubyFormatterConstants.LINES_BEFORE_FIRST,
				Messages.RubyFormatterBlankLinesPage_befireFirstDeclaration);
		manager.createNumber(emptyLinesInternalGroup,
				RubyFormatterConstants.LINES_BEFORE_MODULE,
				Messages.RubyFormatterBlankLinesPage_beforeNestedModuleDeclarations);
		manager.createNumber(emptyLinesInternalGroup,
				RubyFormatterConstants.LINES_BEFORE_CLASS,
				Messages.RubyFormatterBlankLinesPage_beforeNestedClassDeclarations);
		manager.createNumber(emptyLinesInternalGroup,
				RubyFormatterConstants.LINES_BEFORE_METHOD,
				Messages.RubyFormatterBlankLinesPage_beforeMethodDeclarations);
		//
		Group preserveGroup = SWTFactory.createGroup(parent,
				Messages.RubyFormatterBlankLinesPage_existingBlankLines, 2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(preserveGroup,
				RubyFormatterConstants.LINES_PRESERVE,
				Messages.RubyFormatterBlankLinesPage_numberOfEmptyLinesToPreserve);
	}

	protected URL getPreviewContent() {
		return getClass().getResource("blank-lines-preview.rb"); //$NON-NLS-1$
	}

}
