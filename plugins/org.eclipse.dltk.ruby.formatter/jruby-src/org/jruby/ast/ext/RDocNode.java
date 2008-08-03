package org.jruby.ast.ext;

import org.jruby.ast.CommentNode;
import org.jruby.lexer.yacc.ISourcePosition;

public class RDocNode extends CommentNode {

	public RDocNode(ISourcePosition position, String content) {
		super(position, content);
	}

}
