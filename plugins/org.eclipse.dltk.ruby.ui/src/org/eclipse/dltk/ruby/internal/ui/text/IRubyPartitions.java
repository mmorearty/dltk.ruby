package org.eclipse.dltk.ruby.internal.ui.text;

import org.eclipse.dltk.ruby.core.RubyConstants;
import org.eclipse.jface.text.IDocument;

public interface IRubyPartitions {
	public static final String RUBY_PARTITIONING = RubyConstants.RUBY_PARTITIONING;

	public static final String RUBY_COMMENT = "__ruby_comment";
	public static final String RUBY_STRING = "__ruby_string";
	public static final String RUBY_DOC = "__ruby_doc";

	public final static String[] RUBY_PARTITION_TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE, IRubyPartitions.RUBY_DOC,
			IRubyPartitions.RUBY_COMMENT, IRubyPartitions.RUBY_STRING };
}
