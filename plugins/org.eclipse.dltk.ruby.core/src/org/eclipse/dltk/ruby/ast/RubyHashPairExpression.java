package org.eclipse.dltk.ruby.ast;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;

public class RubyHashPairExpression extends ASTNode {

	private final ASTNode key;
	private final ASTNode value;

	public RubyHashPairExpression(int start, int end, ASTNode key, ASTNode value) {
		super(start, end);
		this.key = key;
		this.value = value;
	}

	public ASTNode getKey() {
		return key;
	}

	public ASTNode getValue() {
		return value;
	}

	public int getKind() {
		return 0;
	}

	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (key != null)
				key.traverse(visitor);
			if (value != null)
				value.traverse(visitor);
			visitor.endvisit(this);
		}
	}

}
