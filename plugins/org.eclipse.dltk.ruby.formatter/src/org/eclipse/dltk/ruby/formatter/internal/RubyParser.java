package org.eclipse.dltk.ruby.formatter.internal;

import java.io.Reader;
import java.io.StringReader;

import org.jruby.common.NullWarnings;
import org.jruby.lexer.yacc.LexerSource;
import org.jruby.lexer.yacc.SyntaxException;
import org.jruby.parser.DefaultRubyParser;
import org.jruby.parser.RubyParserConfiguration;
import org.jruby.parser.RubyParserPool;
import org.jruby.parser.RubyParserResult;

/**
 * Serves as a simple facade for all the parsing magic.
 */
public class RubyParser {

	public static RubyParserResult parse(String content) {
		return parse(new StringReader(content));
	}

	public static RubyParserResult parse(Reader content) {
		final RubyParserConfiguration configuration = new RubyParserConfiguration();
		final RubyParserPool parserPool = RubyParserPool.getInstance();
		final DefaultRubyParser parser = parserPool.borrowParser();
		try {
			parser.setWarnings(new NullWarnings());
			final LexerSource source = LexerSource.getSource(FILENAME, content);
			final RubyParserResult result = parser.parse(configuration, source);
			return result;
		} catch (SyntaxException e) {
			return null;
		} finally {
			parserPool.returnParser(parser);
		}
	}

	private static final String FILENAME = "";

}
