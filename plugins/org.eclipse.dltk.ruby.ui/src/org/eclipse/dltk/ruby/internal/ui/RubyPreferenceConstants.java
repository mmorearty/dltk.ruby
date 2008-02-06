/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui;

import org.eclipse.dltk.ruby.internal.ui.text.IRubyColorConstants;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.dltk.ui.preferences.NewScriptProjectPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

public class RubyPreferenceConstants extends PreferenceConstants
{

	/*
	 * Single line comment
	 */
	/**
	 * A named preference that holds the color used to render single line comments.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_COLOR = IRubyColorConstants.RUBY_SINGLE_LINE_COMMENT;

	/**
	 * A named preference that controls whether single line comments are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in bold. If <code>false</code> the are
	 * rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_BOLD = IRubyColorConstants.RUBY_SINGLE_LINE_COMMENT + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in italic. If <code>false</code> the
	 * are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_ITALIC = IRubyColorConstants.RUBY_SINGLE_LINE_COMMENT + EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in strikethrough. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_STRIKETHROUGH = IRubyColorConstants.RUBY_SINGLE_LINE_COMMENT + EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in underline. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 * 
	 *
	 */
	public final static String EDITOR_SINGLE_LINE_COMMENT_UNDERLINE = IRubyColorConstants.RUBY_SINGLE_LINE_COMMENT + EDITOR_UNDERLINE_SUFFIX;

	/*
	 * Key worlds
	 */
	/**
	 * A named preference that holds the color used to render keyword.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_COLOR = IRubyColorConstants.RUBY_KEYWORD;

	/**
	 * A named preference that controls whether kwyword are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in bold. If <code>false</code> the are
	 * rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_BOLD = IRubyColorConstants.RUBY_KEYWORD + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in italic. If <code>false</code> the
	 * are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_ITALIC = IRubyColorConstants.RUBY_KEYWORD + EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in strikethrough. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_STRIKETHROUGH = IRubyColorConstants.RUBY_KEYWORD + EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in underline. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 * 
	 *
	 */
	public final static String EDITOR_KEYWORD_UNDERLINE = IRubyColorConstants.RUBY_KEYWORD + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_DOC_COLOR = IRubyColorConstants.RUBY_DOC;
	public final static String EDITOR_DOC_BOLD = IRubyColorConstants.RUBY_DOC + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_DOC_ITALIC = IRubyColorConstants.RUBY_DOC + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_DOC_STRIKETHROUGH = IRubyColorConstants.RUBY_DOC + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_DOC_UNDERLINE = IRubyColorConstants.RUBY_DOC + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_DOC_TOPIC_COLOR = IRubyColorConstants.RUBY_DOC_TOPIC;
	public final static String EDITOR_DOC_TOPIC_BOLD = IRubyColorConstants.RUBY_DOC_TOPIC + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_DOC_TOPIC_ITALIC = IRubyColorConstants.RUBY_DOC_TOPIC + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_DOC_TOPIC_STRIKETHROUGH = IRubyColorConstants.RUBY_DOC_TOPIC + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_DOC_TOPIC_UNDERLINE = IRubyColorConstants.RUBY_DOC_TOPIC + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_VARIABLE_COLOR = IRubyColorConstants.RUBY_VARIABLE;
	public final static String EDITOR_VARIABLE_BOLD = IRubyColorConstants.RUBY_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_VARIABLE_ITALIC = IRubyColorConstants.RUBY_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_CLASS_VARIABLE_COLOR = IRubyColorConstants.RUBY_CLASS_VARIABLE;
	public final static String EDITOR_CLASS_VARIABLE_BOLD = IRubyColorConstants.RUBY_CLASS_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_CLASS_VARIABLE_ITALIC = IRubyColorConstants.RUBY_CLASS_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_CLASS_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_CLASS_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_CLASS_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_CLASS_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_GLOBAL_VARIABLE_COLOR = IRubyColorConstants.RUBY_GLOBAL_VARIABLE;
	public final static String EDITOR_GLOBAL_VARIABLE_BOLD = IRubyColorConstants.RUBY_GLOBAL_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_GLOBAL_VARIABLE_ITALIC = IRubyColorConstants.RUBY_GLOBAL_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_GLOBAL_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_GLOBAL_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_GLOBAL_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_GLOBAL_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_CONSTANT_VARIABLE_COLOR = IRubyColorConstants.RUBY_CONSTANT_VARIABLE;
	public final static String EDITOR_CONSTANT_VARIABLE_BOLD = IRubyColorConstants.RUBY_CONSTANT_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_CONSTANT_VARIABLE_ITALIC = IRubyColorConstants.RUBY_CONSTANT_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_CONSTANT_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_CONSTANT_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_CONSTANT_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_CONSTANT_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_INSTANCE_VARIABLE_COLOR = IRubyColorConstants.RUBY_INSTANCE_VARIABLE;
	public final static String EDITOR_INSTANCE_VARIABLE_BOLD = IRubyColorConstants.RUBY_INSTANCE_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_INSTANCE_VARIABLE_ITALIC = IRubyColorConstants.RUBY_INSTANCE_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_INSTANCE_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_INSTANCE_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_INSTANCE_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_INSTANCE_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_PSEUDO_VARIABLE_COLOR = IRubyColorConstants.RUBY_PSEUDO_VARIABLE;
	public final static String EDITOR_PSEUDO_VARIABLE_BOLD = IRubyColorConstants.RUBY_PSEUDO_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_PSEUDO_VARIABLE_ITALIC = IRubyColorConstants.RUBY_PSEUDO_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_PSEUDO_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_PSEUDO_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_PSEUDO_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_PSEUDO_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_PREDEFINED_VARIABLE_COLOR = IRubyColorConstants.RUBY_PREDEFINED_VARIABLE;
	public final static String EDITOR_PREDEFINED_VARIABLE_BOLD = IRubyColorConstants.RUBY_PREDEFINED_VARIABLE + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_PREDEFINED_VARIABLE_ITALIC = IRubyColorConstants.RUBY_PREDEFINED_VARIABLE + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_PREDEFINED_VARIABLE_STRIKETHROUGH = IRubyColorConstants.RUBY_PREDEFINED_VARIABLE + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_PREDEFINED_VARIABLE_UNDERLINE = IRubyColorConstants.RUBY_PREDEFINED_VARIABLE + EDITOR_UNDERLINE_SUFFIX;
	
	public final static String EDITOR_SYMBOLS_COLOR = IRubyColorConstants.RUBY_SYMBOLS;
	public final static String EDITOR_SYMBOLS_BOLD = IRubyColorConstants.RUBY_SYMBOLS + EDITOR_BOLD_SUFFIX;
	public final static String EDITOR_SYMBOLS_ITALIC = IRubyColorConstants.RUBY_SYMBOLS + EDITOR_ITALIC_SUFFIX;
	public final static String EDITOR_SYMBOLS_STRIKETHROUGH = IRubyColorConstants.RUBY_SYMBOLS + EDITOR_STRIKETHROUGH_SUFFIX;
	public final static String EDITOR_SYMBOLS_UNDERLINE = IRubyColorConstants.RUBY_SYMBOLS + EDITOR_UNDERLINE_SUFFIX;	
	/*
	 * keyword return color
	 */
	/**
	 * A named preference that holds the color used to render keyword.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_KEYWORD_RETURN_COLOR = IRubyColorConstants.RUBY_KEYWORD_RETURN;

	/**
	 * A named preference that controls whether kwyword are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in bold. If <code>false</code> the are
	 * rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_BOLD = IRubyColorConstants.RUBY_KEYWORD_RETURN + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in italic. If <code>false</code> the
	 * are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_ITALIC = IRubyColorConstants.RUBY_KEYWORD_RETURN + EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in strikethrough. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_KEYWORD_RETURN_STRIKETHROUGH = IRubyColorConstants.RUBY_KEYWORD_RETURN + EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether keyword are rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in underline. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 * 
	 *
	 */
	public final static String EDITOR_KEYWORD_RETURN_UNDERLINE = IRubyColorConstants.RUBY_KEYWORD_RETURN + EDITOR_UNDERLINE_SUFFIX;

	/*
	 * Numbers
	 */
	/**
	 * A named preference that holds the color used to render NUMBER.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_NUMBER_COLOR = IRubyColorConstants.RUBY_NUMBER;

	/**
	 * A named preference that controls whether number are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in bold. If <code>false</code> the are
	 * rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_BOLD = IRubyColorConstants.RUBY_NUMBER + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether NUMBER are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in italic. If <code>false</code> the
	 * are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_ITALIC = IRubyColorConstants.RUBY_NUMBER + EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in strikethrough. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_NUMBER_STRIKETHROUGH = IRubyColorConstants.RUBY_NUMBER + EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether NUMBER are rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in underline. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 * 
	 *
	 */

	public final static String EDITOR_NUMBER_UNDERLINE = IRubyColorConstants.RUBY_NUMBER + EDITOR_UNDERLINE_SUFFIX;		
	
	/*
	 * Strings
	 */
	/**
	 * A named preference that holds the color used to render STRING.
	 * <p>
	 * Value is of type <code>String</code>. A RGB color value encoded as a string using class <code>PreferenceConverter</code>
	 * </p>
	 * 
	 * @see org.eclipse.jface.resource.StringConverter
	 * @see org.eclipse.jface.preference.PreferenceConverter
	 */
	public final static String EDITOR_STRING_COLOR = IRubyColorConstants.RUBY_STRING;

	/**
	 * A named preference that controls whether STRING are rendered in bold.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in bold. If <code>false</code> the are
	 * rendered using no font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_BOLD = IRubyColorConstants.RUBY_STRING + EDITOR_BOLD_SUFFIX;

	/**
	 * A named preference that controls whether STRING are rendered in italic.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in italic. If <code>false</code> the
	 * are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_ITALIC = IRubyColorConstants.RUBY_STRING + EDITOR_ITALIC_SUFFIX;

	/**
	 * A named preference that controls whether single line comments are rendered in strikethrough.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in strikethrough. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 */
	public final static String EDITOR_STRING_STRIKETHROUGH = IRubyColorConstants.RUBY_STRING + EDITOR_STRIKETHROUGH_SUFFIX;

	/**
	 * A named preference that controls whether STRING are rendered in underline.
	 * <p>
	 * Value is of type <code>Boolean</code>. If <code>true</code> single line comments are rendered in underline. If <code>false</code>
	 * the are rendered using no italic font style attribute.
	 * </p>
	 * 
	 *
	 */

	public final static String EDITOR_STRING_UNDERLINE = IRubyColorConstants.RUBY_STRING + EDITOR_UNDERLINE_SUFFIX;
	
	
	public static void initializeDefaultValues( IPreferenceStore store ) {
		PreferenceConstants.initializeDefaultValues(store);
		
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_COLOR, new RGB( 63, 127, 95 ) );		
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_DOC_COLOR, new RGB( 63, 127, 95 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_DOC_TOPIC_COLOR, new RGB( 63, 127, 95 ) );
		
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_KEYWORD_COLOR, new RGB( 127, 0, 85 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_KEYWORD_RETURN_COLOR, new RGB( 127, 0, 85 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_STRING_COLOR, new RGB( 42, 0, 255 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_SYMBOLS_COLOR, new RGB( 42, 0, 255 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_NUMBER_COLOR, new RGB( 0, 0, 0 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_PSEUDO_VARIABLE_COLOR, new RGB( 127, 0, 85 ) );
		
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_VARIABLE_COLOR, new RGB( 200, 0, 0 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_CLASS_VARIABLE_COLOR, new RGB( 200, 0, 0 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_INSTANCE_VARIABLE_COLOR, new RGB( 200, 0, 0 ) );
		PreferenceConverter.setDefault( store, RubyPreferenceConstants.EDITOR_GLOBAL_VARIABLE_COLOR, new RGB( 200, 0, 0 ) );

		store.setDefault( RubyPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_BOLD, false );
		store.setDefault( RubyPreferenceConstants.EDITOR_SINGLE_LINE_COMMENT_ITALIC, true );				
		
		store.setDefault( RubyPreferenceConstants.EDITOR_DOC_ITALIC, true );
		
		store.setDefault( RubyPreferenceConstants.EDITOR_DOC_TOPIC_BOLD, true );
		store.setDefault( RubyPreferenceConstants.EDITOR_DOC_TOPIC_ITALIC, true );
		
		store.setDefault( RubyPreferenceConstants.EDITOR_SYMBOLS_ITALIC, true );

		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_BOLD, true );
		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_ITALIC, false );
		
		store.setDefault( RubyPreferenceConstants.EDITOR_PSEUDO_VARIABLE_BOLD, true );
		store.setDefault( RubyPreferenceConstants.EDITOR_PSEUDO_VARIABLE_ITALIC, true );
		
		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_RETURN_BOLD, true );
		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_RETURN_ITALIC, false );
		
		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_RETURN_BOLD, true );
		store.setDefault( RubyPreferenceConstants.EDITOR_KEYWORD_RETURN_ITALIC, false );
		
		store.setDefault( PreferenceConstants.EDITOR_SMART_INDENT, true);
		store.setDefault( PreferenceConstants.EDITOR_TAB_ALWAYS_INDENT, false);
		store.setDefault( PreferenceConstants.EDITOR_CLOSE_STRINGS, true);
		store.setDefault( PreferenceConstants.EDITOR_CLOSE_BRACKETS, true);
		store.setDefault( PreferenceConstants.EDITOR_CLOSE_BRACES, true);
		store.setDefault( PreferenceConstants.EDITOR_SMART_TAB, true);
		store.setDefault( PreferenceConstants.EDITOR_SMART_PASTE, true);
		store.setDefault( PreferenceConstants.EDITOR_SMART_HOME_END, true);
		store.setDefault( PreferenceConstants.EDITOR_SUB_WORD_NAVIGATION, true);		
		store.setDefault( PreferenceConstants.EDITOR_TAB_WIDTH, 2);
		store.setDefault( PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, true);
		
		// folding
		store.setDefault(PreferenceConstants.EDITOR_FOLDING_ENABLED, true);
		store.setDefault(PreferenceConstants.EDITOR_COMMENTS_FOLDING_ENABLED, true);		
//		store.setDefault(PreferenceConstants.EDITOR_FOLDING_INNERTYPES, false);
//		store.setDefault(PreferenceConstants.EDITOR_FOLDING_METHODS, false);
//		store.setDefault(PreferenceConstants.EDITOR_FOLDING_IMPORTS, true);
		
		store.setDefault (CodeFormatterConstants.FORMATTER_TAB_CHAR, CodeFormatterConstants.TAB);
		store.setDefault (CodeFormatterConstants.FORMATTER_TAB_SIZE, "2");
		store.setDefault (CodeFormatterConstants.FORMATTER_INDENTATION_SIZE, "2");
		
		NewScriptProjectPreferencePage.initDefaults(store);
		
		store.setDefault(PreferenceConstants.APPEARANCE_COMPRESS_PACKAGE_NAMES, false);
		store.setDefault(PreferenceConstants.APPEARANCE_METHOD_RETURNTYPE, false);
		store.setDefault(PreferenceConstants.APPEARANCE_METHOD_TYPEPARAMETERS, true);
		store.setDefault(PreferenceConstants.APPEARANCE_PKG_NAME_PATTERN_FOR_PKG_VIEW, ""); //$NON-NLS-1$
		
		store.setDefault(PreferenceConstants.SHOW_SOURCE_MODULE_CHILDREN, true);
		
		store.setDefault(PreferenceConstants.CODEASSIST_AUTOACTIVATION_TRIGGERS, ".:$@");
	}
}
