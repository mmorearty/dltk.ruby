package org.eclipse.dltk.ruby.formatter.internal.nodes;

import org.eclipse.dltk.formatter.FormatterTextNode;
import org.eclipse.dltk.formatter.IFormatterContext;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.formatter.IFormatterWriter;
import org.eclipse.dltk.ruby.formatter.RubyFormatterConstants;

public class FormatterCommentNode extends FormatterTextNode {

	public FormatterCommentNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterWriter visitor)
			throws Exception {
		final boolean savedWrapping = context.isWrapping();
		final boolean savedComment = context.isComment();
		final boolean isWrapping = getDocument().getBoolean(
				RubyFormatterConstants.WRAP_COMMENTS);
		if (isWrapping) {
			context.setWrapping(true);
		}
		context.setComment(true);
		visitor.write(context, getStartOffset(), getEndOffset());
		if (isWrapping) {
			context.setWrapping(savedWrapping);
		}
		context.setComment(savedComment);
	}

}
