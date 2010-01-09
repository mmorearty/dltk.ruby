/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.editor;

import org.eclipse.dltk.internal.ui.editor.BracketInserter;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ruby.internal.ui.text.IRubyPartitions;
import org.eclipse.dltk.ruby.internal.ui.text.ISymbols;
import org.eclipse.dltk.ruby.internal.ui.text.RubyHeuristicScanner;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.texteditor.ITextEditorExtension3;

public class RubyBracketInserter extends BracketInserter {

	RubyBracketInserter(ScriptEditor scriptEditor) {
		super(scriptEditor);
	}

	public void verifyKey(VerifyEvent event) {
		// early pruning to slow down normal typing as little as possible
		if (!event.doit
				|| editor.getInsertMode() != ITextEditorExtension3.SMART_INSERT)
			return;
		switch (event.character) {
		case '(':
		case '<':
		case '[':
		case '\'':
		case '\"':
			break;
		default:
			return;
		}

		final ISourceViewer sourceViewer = this.editor.getScriptSourceViewer();
		IDocument document = sourceViewer.getDocument();

		final Point selection = sourceViewer.getSelectedRange();
		final int offset = selection.x;
		final int length = selection.y;

		try {
			IRegion startLine = document.getLineInformationOfOffset(offset);
			IRegion endLine = document.getLineInformationOfOffset(offset
					+ length);

			RubyHeuristicScanner scanner = new RubyHeuristicScanner(document);
			int nextToken = scanner.nextToken(offset + length, endLine
					.getOffset()
					+ endLine.getLength());
			String next = nextToken == ISymbols.TokenEOF ? null : document.get(
					offset, scanner.getPosition() - offset).trim();
			int prevToken = scanner.previousToken(offset - 1, startLine
					.getOffset());
			int prevTokenOffset = scanner.getPosition();
			if (prevTokenOffset < 0)
				prevTokenOffset = 0;
			String previous = offset > 1 && prevToken == ISymbols.TokenEOF ? null
					: document.get(prevTokenOffset, offset - prevTokenOffset)
							.trim();
			switch (event.character) {
			case '(':
				if (!fCloseBrackets || nextToken == ISymbols.TokenLPAREN
						|| nextToken == ISymbols.TokenIDENTIFIER
						|| next != null && next.length() > 1)
					return;
				break;

			case '<':
				if (!(fCloseAngularBrackets && fCloseBrackets)
						|| nextToken == ISymbols.TokenLESSTHAN
						|| prevToken != ISymbols.TokenLBRACE
						&& prevToken != ISymbols.TokenRBRACE
						&& prevToken != ISymbols.TokenSEMICOLON
						&& (prevToken != ISymbols.TokenIDENTIFIER || !isAngularIntroducer(previous))
						&& prevToken != ISymbols.TokenEOF)
					return;
				break;

			case '[':
				if (!fCloseBrackets || nextToken == ISymbols.TokenIDENTIFIER
						|| next != null && next.length() > 1)
					return;
				break;

			case '\'':
			case '"':
				if (!fCloseStrings
						|| nextToken == ISymbols.TokenIDENTIFIER
						/* || prevToken == Symbols.TokenIDENT */|| next != null
						&& next.length() > 1
						|| previous != null
						&& (previous.length() > 1 && previous.charAt(0) == event.character))
					return;
				break;

			default:
				return;
			}

			int correctedOffset = (document.getLength() > 0 && document
					.getLength() == offset) ? offset - 1 : offset;
			if (!validatePartitioning(document, correctedOffset,
					IRubyPartitions.RUBY_PARTITIONING)) {
				return;
			}

			if (!this.editor.validateEditorInputState())
				return;

			insertBrackets(document, offset, length, event.character,
					getPeerCharacter(event.character));

			event.doit = false;

		} catch (BadLocationException e) {
			DLTKUIPlugin.log(e);
		} catch (BadPositionCategoryException e) {
			DLTKUIPlugin.log(e);
		}
	}

}
