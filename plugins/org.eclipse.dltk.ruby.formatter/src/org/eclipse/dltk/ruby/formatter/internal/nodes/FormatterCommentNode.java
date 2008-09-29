package org.eclipse.dltk.ruby.formatter.internal.nodes;

import org.eclipse.dltk.formatter.nodes.FormatterTextNode;
import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterVisitor;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;

public class FormatterCommentNode extends FormatterTextNode {

	public FormatterCommentNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		if (getDocument().getBoolean(RubyFormatterConstants.WRAP_COMMENTS)) {
			final boolean savedWrapping = context.isWrapping();
			context.setWrapping(true);
			visitor.visit(context, this);
			context.setWrapping(savedWrapping);
		} else {
			visitor.visit(context, this);
		}
	}

}
