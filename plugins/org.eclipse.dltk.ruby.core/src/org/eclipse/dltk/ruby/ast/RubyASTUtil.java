package org.eclipse.dltk.ruby.ast;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.references.ConstantReference;
import org.eclipse.dltk.ast.references.Reference;
import org.eclipse.dltk.ruby.core.RubyPlugin;

/**
 * @author ssanders
 */
public class RubyASTUtil {

	private RubyASTUtil() {
	}

	public static String resolveClassName(ASTNode node) {
		String className = ""; //$NON-NLS-1$

		if (node instanceof Reference) {
			className = ((Reference) node).getStringRepresentation();
		} else if (node instanceof RubyColonExpression) {
			RubyColonExpression rcExp = (RubyColonExpression) node;

			if (rcExp.getLeft() != null) {
				className = resolveClassName(rcExp.getLeft());

				className += "::"; //$NON-NLS-1$
			}

			className += rcExp.getName();
		}

		return className;
	}

	/**
	 * @param value
	 * @param sb
	 */
	private static boolean collectColonExpression(RubyColonExpression value,
			StringBuffer sb) {
		final ASTNode left = value.getLeft();
		if (left instanceof RubyColonExpression) {
			if (!collectColonExpression((RubyColonExpression) left, sb)) {
				return false;
			}
		} else if (left instanceof ConstantReference) {
			sb.append(((ConstantReference) left).getName());
		} else if (left != null) {
			final String msg = "Unexpected node in colon-expression " + left.getClass().getName(); //$NON-NLS-1$
			RubyPlugin.log(msg);
			return false;
		}
		if (sb.length() != 0) {
			sb.append("::"); //$NON-NLS-1$
		}
		sb.append(value.getName());
		return true;
	}

	/**
	 * Resolves type name reference (Const or ModuleName::Const). Returns the
	 * full resolved type name or <code>null</code>.
	 * 
	 * @param value
	 * @return
	 */
	public static String resolveReference(ASTNode value) {
		if (value instanceof ConstantReference) {
			return ((ConstantReference) value).getName();
		}
		if (value instanceof RubyColonExpression) {
			final StringBuffer sb = new StringBuffer();
			if (collectColonExpression((RubyColonExpression) value, sb)) {
				return sb.toString();
			}
		}
		return null;
	}

	/**
	 * Resolves type name reference (Const or ModuleName::Const). Returns the
	 * simple resolved type name or <code>null</code>.
	 * 
	 * @param value
	 * @return
	 */
	public static String resolveReferenceSimpleName(ASTNode value) {
		if (value instanceof ConstantReference) {
			return ((ConstantReference) value).getName();
		}
		if (value instanceof RubyColonExpression) {
			return ((RubyColonExpression) value).getName();
		}
		return null;
	}

}
