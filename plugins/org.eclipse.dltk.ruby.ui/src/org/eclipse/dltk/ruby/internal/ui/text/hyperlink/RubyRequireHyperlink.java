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
package org.eclipse.dltk.ruby.internal.ui.text.hyperlink;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.ruby.internal.ui.RubyLabelProvider;
import org.eclipse.dltk.ui.actions.OpenAction;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;

public class RubyRequireHyperlink implements IHyperlink {

	private final String path;
	private final IRegion region;
	private final ITextEditor editor;

	public RubyRequireHyperlink(String path, IRegion region, ITextEditor editor) {
		this.path = path;
		this.region = region;
		this.editor = editor;
	}

	public IRegion getHyperlinkRegion() {
		return region;
	}

	public String getHyperlinkText() {
		return NLS.bind(RubyHyperlinkMessages.RequireHyperlink_text, path);
	}

	public String getTypeLabel() {
		return RubyHyperlinkMessages.RequireHyperlink_label;
	}

	public void open() {
		try {
			doOpen();
		} catch (Exception e) {
			RubyPlugin.log(e);
			showMessage(e.getMessage());
		}
	}

	/**
	 * Default extension for ruby scripts
	 */
	private static final String DEFAULT_EXTENSION = ".rb"; //$NON-NLS-1$

	private void doOpen() throws ModelException {
		final IModelElement input = EditorUtility.getEditorInputModelElement(
				editor, false);
		if (input != null) {
			final OpenAction openAction = (OpenAction) editor
					.getAction("OpenEditor"); //$NON-NLS-1$
			if (openAction != null) {
				final IScriptProject project = input.getScriptProject();
				final ISourceModule[] modules = SourceModuleLookup.lookup(
						project, path, DEFAULT_EXTENSION);
				if (modules.length > 0) {
					if (modules.length == 1) {
						openAction.run(modules);
					} else {
						ISourceModule[] selection = selectModule(modules);
						if (selection != null && selection.length > 0) {
							openAction.run(selection);
						}
					}
				} else {
					showMessage(RubyHyperlinkMessages.RequireHyperlink_BadSelection);
				}
			}
		}
	}

	/**
	 * Shows a dialog for resolving an ambiguous script element.
	 */
	private ISourceModule[] selectModule(ISourceModule[] modules) {
		Assert.isLegal(modules.length > 1);
		final ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				getShell(), new RubyLabelProvider());
		dialog.setTitle(RubyHyperlinkMessages.RequireHyperlink_title);
		dialog.setMessage(RubyHyperlinkMessages.RequireHyperlink_message);
		dialog.setElements(modules);
		if (dialog.open() == Window.OK) {
			final Object[] selection = dialog.getResult();
			if (selection != null && selection.length > 0) {
				final List result = new ArrayList();
				for (int i = 0, size = selection.length; i < size; i++) {
					final Object current = selection[i];
					if (current instanceof ISourceModule) {
						result.add(current);
					}
				}
				if (!result.isEmpty()) {
					return (ISourceModule[]) result
							.toArray(new ISourceModule[result.size()]);
				}
			}
		}
		return null;
	}

	private void showMessage(String message) {
		final IEditorStatusLine statusLine = (IEditorStatusLine) editor
				.getAdapter(IEditorStatusLine.class);
		if (statusLine != null) {
			statusLine.setMessage(true, message, null);
		}
		getShell().getDisplay().beep();
	}

	private Shell getShell() {
		return editor.getEditorSite().getShell();
	}

}
