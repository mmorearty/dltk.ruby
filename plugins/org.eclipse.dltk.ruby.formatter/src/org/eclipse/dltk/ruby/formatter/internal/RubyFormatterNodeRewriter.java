package org.eclipse.dltk.ruby.formatter.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.formatter.FormatterNodeRewriter;
import org.eclipse.dltk.formatter.FormatterTextNode;
import org.eclipse.dltk.formatter.FormatterUtils;
import org.eclipse.dltk.formatter.IFormatterCommentableNode;
import org.eclipse.dltk.formatter.IFormatterContainerNode;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.formatter.IFormatterNode;
import org.eclipse.dltk.ruby.formatter.internal.nodes.FormatterCommentNode;
import org.jruby.ast.CommentNode;
import org.jruby.parser.RubyParserResult;

public class RubyFormatterNodeRewriter extends FormatterNodeRewriter {

	private final IFormatterDocument document;
	private final List comments = new ArrayList();

	public RubyFormatterNodeRewriter(RubyParserResult result,
			IFormatterDocument document) {
		this.document = document;
		for (Iterator i = result.getCommentNodes().iterator(); i.hasNext();) {
			CommentNode commentNode = (CommentNode) i.next();
			if (!commentNode.isBlock()) {
				comments.add(commentNode);
			}
		}
	}

	public void rewrite(IFormatterContainerNode root) {
		mergeTextNodes(root);
		insertComments(root);
		attachComments(root);
	}

	private void insertComments(IFormatterContainerNode root) {
		final List body = root.getBody();
		final List newBody = new ArrayList();
		boolean changes = false;
		for (Iterator i = body.iterator(); i.hasNext();) {
			final IFormatterNode node = (IFormatterNode) i.next();
			if (isPlainTextNode(node)) {
				if (hasComments(node.getStartOffset(), node.getEndOffset())) {
					selectValidRanges(node.getStartOffset(), node
							.getEndOffset(), newBody);
					changes = true;
				} else {
					newBody.add(node);
				}
			} else {
				newBody.add(node);
			}
		}
		if (changes) {
			body.clear();
			body.addAll(newBody);
		}
		for (Iterator i = body.iterator(); i.hasNext();) {
			final IFormatterNode node = (IFormatterNode) i.next();
			if (node instanceof IFormatterContainerNode) {
				insertComments((IFormatterContainerNode) node);
			}
		}
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

	private boolean hasComments(int startOffset, int endOffset) {
		for (Iterator i = comments.iterator(); i.hasNext();) {
			final CommentNode commentNode = (CommentNode) i.next();
			if (commentNode.getStartOffset() < endOffset
					&& startOffset < commentNode.getEndOffset()) {
				return true;
			}
		}
		return false;
	}

	private void selectValidRanges(int start, int end, List result) {
		for (Iterator i = comments.iterator(); i.hasNext();) {
			final CommentNode comment = (CommentNode) i.next();
			if (start <= comment.getEndOffset()
					&& comment.getStartOffset() <= end) {
				if (start < comment.getStartOffset()) {
					int validEnd = Math.min(end, comment.getStartOffset());
					result
							.add(new FormatterTextNode(document, start,
									validEnd));
					start = comment.getStartOffset();
				}
				result.add(new FormatterCommentNode(document, start, Math.min(
						comment.getEndOffset(), end)));
				start = comment.getEndOffset();
				if (start > end) {
					break;
				}
			}
		}
		if (start < end) {
			result.add(new FormatterTextNode(document, start, end));
		}
	}

}
