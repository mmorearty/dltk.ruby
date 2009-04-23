package org.eclipse.dltk.ruby.formatter.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.formatter.FormatterNodeRewriter;
import org.eclipse.dltk.formatter.FormatterUtils;
import org.eclipse.dltk.formatter.IFormatterCommentableNode;
import org.eclipse.dltk.formatter.IFormatterContainerNode;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.formatter.IFormatterNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterCommentNode;
import org.jruby.ast.CommentNode;
import org.jruby.parser.RubyParserResult;

public class RubyFormatterNodeRewriter extends FormatterNodeRewriter {

	public RubyFormatterNodeRewriter(RubyParserResult result) {
		for (Iterator i = result.getCommentNodes().iterator(); i.hasNext();) {
			CommentNode commentNode = (CommentNode) i.next();
			if (!commentNode.isBlock()) {
				addComment(commentNode.getStartOffset(), commentNode
						.getEndOffset(), commentNode);
			}
		}
	}

	public void rewrite(IFormatterContainerNode root) {
		mergeTextNodes(root);
		insertComments(root);
		attachComments(root);
	}

	private void attachComments(IFormatterContainerNode root) {
		final List commentNodes = new ArrayList();
		final List comments = new ArrayList();
		final List body = root.getBody();
		for (Iterator i = body.iterator(); i.hasNext();) {
			IFormatterNode node = (IFormatterNode) i.next();
			if (node instanceof FormatterCommentNode) {
				comments.add(node);
			} else if (FormatterUtils.isNewLine(node)
					&& !comments.isEmpty()
					&& comments.get(comments.size() - 1) instanceof FormatterCommentNode) {
				comments.add(node);
			} else if (!comments.isEmpty()) {
				if (node instanceof IFormatterCommentableNode) {
					((IFormatterCommentableNode) node).insertBefore(comments);
					commentNodes.addAll(comments);
				}
				comments.clear();
			}
		}
		body.removeAll(commentNodes);
		for (Iterator i = body.iterator(); i.hasNext();) {
			final IFormatterNode node = (IFormatterNode) i.next();
			if (node instanceof IFormatterContainerNode) {
				attachComments((IFormatterContainerNode) node);
			}
		}
	}

	protected IFormatterNode createCommentNode(IFormatterDocument document,
			int startOffset, int endOffset, Object object) {
		return new FormatterCommentNode(document, startOffset, endOffset);
	}

}
