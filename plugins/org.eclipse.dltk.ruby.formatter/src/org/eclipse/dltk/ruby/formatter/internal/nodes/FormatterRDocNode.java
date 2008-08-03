package org.eclipse.dltk.ruby.formatter.internal.nodes;

import org.eclipse.dltk.formatter.nodes.FormatterTextNode;
import org.eclipse.dltk.formatter.nodes.IFormatterContext;
import org.eclipse.dltk.formatter.nodes.IFormatterDocument;
import org.eclipse.dltk.formatter.nodes.IFormatterVisitor;

public class FormatterRDocNode extends FormatterTextNode {

	public FormatterRDocNode(IFormatterDocument document, int startOffset,
			int endOffset) {
		super(document, startOffset, endOffset);
	}

	public void accept(IFormatterContext context, IFormatterVisitor visitor)
			throws Exception {
		IFormatterContext commentContext = context.copy();
		commentContext.setIndenting(false);
		visitor.visit(commentContext, this);
	}

}
