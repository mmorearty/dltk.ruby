package org.eclipse.dltk.ruby.formatter.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.ruby.formatter.preferences.messages"; //$NON-NLS-1$
	public static String RubyFormatterBlankLinesPage_afterRequireDirectives;
	public static String RubyFormatterBlankLinesPage_befireFirstDeclaration;
	public static String RubyFormatterBlankLinesPage_beforeMethodDeclarations;
	public static String RubyFormatterBlankLinesPage_beforeNestedClassDeclarations;
	public static String RubyFormatterBlankLinesPage_beforeNestedModuleDeclarations;
	public static String RubyFormatterBlankLinesPage_betweenClasses;
	public static String RubyFormatterBlankLinesPage_betweenMethods;
	public static String RubyFormatterBlankLinesPage_betweenModules;
	public static String RubyFormatterBlankLinesPage_blankLinesInSourceFile;
	public static String RubyFormatterBlankLinesPage_blankLinesWithingClassModuleDeclarations;
	public static String RubyFormatterBlankLinesPage_existingBlankLines;
	public static String RubyFormatterBlankLinesPage_numberOfEmptyLinesToPreserve;
	public static String RubyFormatterCommentsPage_commentFormatting;
	public static String RubyFormatterCommentsPage_enableCommentWrapping;
	public static String RubyFormatterCommentsPage_maximumLineWidthForComments;
	public static String RubyFormatterIndentationTabPage_declarationsWithinClassBody;
	public static String RubyFormatterIndentationTabPage_declarationsWithinModuleBody;
	public static String RubyFormatterIndentationTabPage_generalSettings;
	public static String RubyFormatterIndentationTabPage_indentationCharacter;
	public static String RubyFormatterIndentationTabPage_indentationSize;
	public static String RubyFormatterIndentationTabPage_indentWithinBlocks;
	public static String RubyFormatterIndentationTabPage_indentWithinDefinitions;
	public static String RubyFormatterIndentationTabPage_statementsWithinBlockBody;
	public static String RubyFormatterIndentationTabPage_statementsWithinCaseBody;
	public static String RubyFormatterIndentationTabPage_statementsWithinIfBody;
	public static String RubyFormatterIndentationTabPage_statementsWithinMethodBody;
	public static String RubyFormatterIndentationTabPage_StatementsWithinWhenBody;
	public static String RubyFormatterIndentationTabPage_tabSize;
	public static String RubyFormatterModifyDialog_blankLines;
	public static String RubyFormatterModifyDialog_comments;
	public static String RubyFormatterModifyDialog_indentation;
	public static String RubyFormatterModifyDialog_rubyFormatter;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
