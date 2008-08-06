package org.eclipse.dltk.ruby.internal.parser.visitors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.BooleanLiteral;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ruby.ast.RubyCallArgument;
import org.eclipse.dltk.ruby.ast.RubySymbolReference;

public class RubyAttributeHandler {
	private static final String ATTR = "attr"; //$NON-NLS-1$
	private static final String ATTR_ACCESSIBLE = "attr_accessible"; //$NON-NLS-1$
	private static final String ATTR_ACCESSOR = "attr_accessor"; //$NON-NLS-1$
	private static final String ATTR_INTERNAL = "attr_internal"; //$NON-NLS-1$
	private static final String ATTR_INTERNAL_ACCESSOR = "attr_internal_accessor"; //$NON-NLS-1$
	private static final String ATTR_INTERNAL_READER = "attr_internal_reader"; //$NON-NLS-1$
	private static final String ATTR_INTERNAL_WRITER = "attr_internal_writer"; //$NON-NLS-1$
	private static final String ATTR_PROTECTED = "attr_protected";
	private static final String ATTR_READER = "attr_reader"; //$NON-NLS-1$
	private static final String ATTR_READONLY = "attr_readonly"; //$NON-NLS-1$
	private static final String ATTR_WRITER = "attr_writer"; //$NON-NLS-1$
	private static final String CATTR_ACCESSOR = "cattr_accessor"; //$NON-NLS-1$
	private static final String CATTR_READER = "cattr_reader"; //$NON-NLS-1$
	private static final String CATTR_WRITER = "cattr_writer"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_ACCESSOR = "class_inheritable_accessor"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_ARRAY = "class_inheritable_array"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_ARRAY_WRITER = "class_inheritable_array_writer"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_HASH = "class_inheritable_hash"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_HASH_WRITER = "class_inheritable_hash_writer"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_READER = "class_inheritable_reader"; //$NON-NLS-1$
	private static final String CLASS_INHERITABLE_WRITER = "class_inheritable_writer"; //$NON-NLS-1$

	private static final Set/*<String>*/ ATTRIBUTE_CREATION_NAMES = new HashSet();
	private static final Set/*<String>*/ ATTRIBUTE_ACCESSOR_NAMES = new HashSet();
	private static final Set/*<String>*/ ATTRIBUTE_READER_NAMES = new HashSet();
	private static final Set/*<String>*/ ATTRIBUTE_WRITER_NAMES = new HashSet();
	private static final Set/*<String>*/ META_ATTRIBUTE_CREATION_NAMES = new HashSet();
	static {
		ATTRIBUTE_CREATION_NAMES.add(ATTR);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_ACCESSOR);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_ACCESSIBLE);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_INTERNAL);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_INTERNAL_ACCESSOR);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_INTERNAL_READER);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_INTERNAL_WRITER);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_PROTECTED);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_READER);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_READONLY);
		ATTRIBUTE_CREATION_NAMES.add(ATTR_WRITER);
		ATTRIBUTE_CREATION_NAMES.add(CATTR_ACCESSOR);
		ATTRIBUTE_CREATION_NAMES.add(CATTR_READER);
		ATTRIBUTE_CREATION_NAMES.add(CATTR_WRITER);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_ACCESSOR);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_ARRAY);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_ARRAY_WRITER);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_HASH);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_HASH_WRITER);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_READER);
		ATTRIBUTE_CREATION_NAMES.add(CLASS_INHERITABLE_WRITER);

		ATTRIBUTE_ACCESSOR_NAMES.add(ATTR_ACCESSOR);
		ATTRIBUTE_ACCESSOR_NAMES.add(ATTR_ACCESSIBLE);
		ATTRIBUTE_ACCESSOR_NAMES.add(ATTR_INTERNAL);
		ATTRIBUTE_ACCESSOR_NAMES.add(ATTR_INTERNAL_ACCESSOR);
		ATTRIBUTE_ACCESSOR_NAMES.add(CATTR_ACCESSOR);
		ATTRIBUTE_ACCESSOR_NAMES.add(CLASS_INHERITABLE_ACCESSOR);
		ATTRIBUTE_ACCESSOR_NAMES.add(CLASS_INHERITABLE_ARRAY);
		ATTRIBUTE_ACCESSOR_NAMES.add(CLASS_INHERITABLE_HASH);

		ATTRIBUTE_READER_NAMES.add(ATTR_INTERNAL_READER);
		ATTRIBUTE_READER_NAMES.add(ATTR_PROTECTED);
		ATTRIBUTE_READER_NAMES.add(ATTR_READER);
		ATTRIBUTE_READER_NAMES.add(ATTR_READONLY);
		ATTRIBUTE_READER_NAMES.add(CATTR_READER);
		ATTRIBUTE_READER_NAMES.add(CLASS_INHERITABLE_READER);

		ATTRIBUTE_WRITER_NAMES.add(ATTR_INTERNAL_WRITER);
		ATTRIBUTE_WRITER_NAMES.add(ATTR_WRITER);
		ATTRIBUTE_WRITER_NAMES.add(CATTR_WRITER);
		ATTRIBUTE_WRITER_NAMES.add(CLASS_INHERITABLE_ARRAY_WRITER);
		ATTRIBUTE_WRITER_NAMES.add(CLASS_INHERITABLE_HASH_WRITER);
		ATTRIBUTE_WRITER_NAMES.add(CLASS_INHERITABLE_WRITER);

		META_ATTRIBUTE_CREATION_NAMES.add(CATTR_ACCESSOR);
		META_ATTRIBUTE_CREATION_NAMES.add(CATTR_READER);
		META_ATTRIBUTE_CREATION_NAMES.add(CATTR_WRITER);
	}

	private final CallExpression call;
	private List readers;
	private List writers;

	public RubyAttributeHandler(CallExpression call) {
		super();
		if (!isAttributeCreationCall(call)) {
			throw new IllegalArgumentException();
		}
		this.call = call;
		readers = new ArrayList();
		writers = new ArrayList();
		init();
	}

	private void init() {
		String name = call.getName();
		CallArgumentsList list = call.getArgs();
		List expr = list.getChilds();
		Iterator it = expr.iterator();
		boolean create_reader = false;
		boolean create_writer = false;
		if (ATTRIBUTE_READER_NAMES.contains(name)) {
			create_reader = true;
		} else if (ATTRIBUTE_WRITER_NAMES.contains(name)) {
			create_writer = true;
		} else if (ATTRIBUTE_ACCESSOR_NAMES.contains(name)) {
			create_reader = true;
			create_writer = true;
		} else if (name.equals(ATTR)) {
			create_reader = true;
			if (expr.size() > 0) {
				ASTNode node = (ASTNode) expr.get(expr.size() - 1);
				if (node instanceof RubyCallArgument) {
					node = ((RubyCallArgument) node).getValue();
				}
				if (node instanceof BooleanLiteral) {
					BooleanLiteral lit = (BooleanLiteral) node;
					create_writer = lit.boolValue();
				}
			}

		}
		while (it.hasNext()) {
			ASTNode sr = (ASTNode) it.next();
			if (!(sr instanceof RubyCallArgument)) {
				continue;
			}
			sr = ((RubyCallArgument) sr).getValue();
			String attr = getText(sr);			
			if (attr == null) {
				continue;
			}
			if (create_reader) {
				readers.add(sr);
			}
			if (create_writer) {
				writers.add(sr);
			}
		}
	}

	public List getReaders() {
		return readers;
	}

	public List getWriters() {
		return writers;
	}

	public static boolean isAttributeCreationCall(CallExpression c) {
		if (c.getReceiver() != null)
			return false;
		String name = c.getName();
		return ATTRIBUTE_CREATION_NAMES.contains(name);
	}

	public static boolean isMetaAttributeCreationCall(CallExpression c) {
		if (c.getReceiver() != null)
			return false;
		String name = c.getName();
		return META_ATTRIBUTE_CREATION_NAMES.contains(name);
	}

	public static String getText(ASTNode sr) {
		if (sr == null)
			return null;
		String attr = null;
		if (sr instanceof RubySymbolReference) {
			attr = ((RubySymbolReference) sr).getName();
		} else if (sr instanceof StringLiteral) {
			attr = ((StringLiteral) sr).getValue();
		}
		return attr;
	}
	
}
