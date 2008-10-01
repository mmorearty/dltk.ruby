package org.eclipse.dltk.ruby.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

public class RubyContentDescriber extends ScriptContentDescriber {
	protected static Pattern[] header_patterns = { Pattern.compile(
			"^#!.*ruby.*", Pattern.MULTILINE) }; //$NON-NLS-1$

	protected Pattern[] getHeaderPatterns() {
		return header_patterns;
	}
}
