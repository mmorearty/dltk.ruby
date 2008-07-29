package org.eclipse.dltk.ruby.internal.ui.actions;

import org.eclipse.dltk.internal.ui.editor.DLTKEditorMessages;
import org.eclipse.dltk.ruby.internal.ui.editor.RubyEditor;
import org.eclipse.dltk.ui.actions.GenerateActionGroup;
import org.eclipse.dltk.ui.actions.DLTKActionConstants;
import org.eclipse.dltk.ui.actions.IScriptEditorActionDefinitionIds;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.texteditor.TextOperationAction;

public class RubyGenerateActionGroup extends GenerateActionGroup {
	public RubyGenerateActionGroup(RubyEditor editor, String groupName) {
		super(editor, groupName);
		Action action = new TextOperationAction(DLTKEditorMessages
				.getBundleForConstructedKeys(),
				"Format.", editor, ISourceViewer.FORMAT); //$NON-NLS-1$
		action.setActionDefinitionId(IScriptEditorActionDefinitionIds.FORMAT);
		editor.setAction(DLTKActionConstants.FORMAT, action);
		editor.markAsStateDependentAction(DLTKActionConstants.FORMAT, true);
		editor.markAsSelectionDependentAction(DLTKActionConstants.FORMAT, true);
	}

}
