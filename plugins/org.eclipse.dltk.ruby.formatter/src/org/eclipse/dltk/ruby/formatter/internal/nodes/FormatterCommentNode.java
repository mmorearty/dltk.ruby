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
		if (getDocument().getBoolean(RubyFormatterConstants.WRAP_COMMENTS)) {
			final boolean savedWrapping = context.isWrapping();
			context.setWrapping(true);
			visitor.write(context, getStartOffset(), getEndOffset());
			context.setWrapping(savedWrapping);
		} else {
			visitor.write(context, getStartOffset(), getEndOffset());
		}
	}

}
