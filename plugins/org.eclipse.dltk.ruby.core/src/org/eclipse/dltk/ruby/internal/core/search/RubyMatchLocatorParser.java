/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.core.search;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.expressions.BigNumericLiteral;
import org.eclipse.dltk.ast.expressions.BooleanLiteral;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.FloatNumericLiteral;
import org.eclipse.dltk.ast.expressions.Literal;
import org.eclipse.dltk.ast.expressions.NilLiteral;
import org.eclipse.dltk.ast.expressions.NumericLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.references.ConstantReference;
import org.eclipse.dltk.ast.references.Reference;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.references.TypeReference;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.core.search.matching.MatchLocator;
import org.eclipse.dltk.core.search.matching.MatchLocatorParser;
import org.eclipse.dltk.core.search.matching.PatternLocator;
import org.eclipse.dltk.ruby.ast.IRubyASTVisitor;
import org.eclipse.dltk.ruby.ast.RubyAliasExpression;
import org.eclipse.dltk.ruby.ast.RubyAssignment;
import org.eclipse.dltk.ruby.ast.RubyColonExpression;
import org.eclipse.dltk.ruby.ast.RubyConstantDeclaration;
import org.eclipse.dltk.ruby.ast.RubyRegexpExpression;
import org.eclipse.dltk.ruby.ast.RubySymbolReference;

public class RubyMatchLocatorParser extends MatchLocatorParser {

	public RubyMatchLocatorParser(MatchLocator locator) {
		super(locator);
	}

	private final class TypeReferenceEquals extends TypeReference {
		private TypeReferenceEquals(int start, int end, String name) {
			super(start, end, name);
		}

		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (obj instanceof ASTNode) {
				ASTNode s = (ASTNode) obj;
				if (s.sourceEnd() < 0 || s.sourceStart() < 0) {
					return false;
				}
				return sourceStart() == s.sourceStart()
						&& sourceEnd() == s.sourceEnd();
			}
			return false;
		}

		public int hashCode() {
			return this.sourceEnd() * 1001 + this.sourceEnd();
		}
	}

	protected class RubyMatchVisitor extends MatchVisitor implements
			IRubyASTVisitor {

		public void visitTypeName(ASTNode node) {
			// empty
		}

	}

	protected MatchVisitor getMatchVisitor() {
		return new RubyMatchVisitor();
	}

	private void reportTypeReferenceMatch(ASTNode node, PatternLocator locator) {
		String typeName;
		while (node != null) {
			if (node instanceof RubyColonExpression) {
				typeName = ((RubyColonExpression) node).getName();
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), typeName);
				locator.match(ref, this.getNodeSet());
				node = ((RubyColonExpression) node).getLeft();
			} else if (node instanceof ConstantReference) {
				typeName = ((ConstantReference) node).getName();
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), typeName);
				locator.match(ref, this.getNodeSet());
				node = null;
			} else {
				node = null;
			}
		}
	}

	private void reportSimpleReferenceMatch(SimpleReference simpleRef,
			PatternLocator locator) {
		int pos = simpleRef.sourceStart();
		if (pos < 0) {
			pos = 0;
		}
		locator.match(new SimpleReference(pos, pos
				+ simpleRef.getName().length(), simpleRef.getName()) {
			public boolean equals(Object obj) {
				if (obj == this)
					return true;
				if (obj instanceof ASTNode) {
					ASTNode s = (ASTNode) obj;
					if (s.sourceEnd() < 0 || s.sourceStart() < 0) {
						return false;
					}
					return sourceStart() == s.sourceStart()
							&& sourceEnd() == s.sourceEnd();
				}
				return false;
			}

			public int hashCode() {
				return this.sourceEnd() * 1001 + this.sourceEnd();
			}
		}, this.getNodeSet());
	}

	protected void processStatement(ASTNode node, PatternLocator locator) {
		if (node == null) {
			return;
		}
		if (node instanceof CallExpression) {
			CallExpression call = (CallExpression) node;
			// int start = call.sourceStart();
			// int end = call.sourceEnd();
			// if (start < 0) {
			// start = 0;
			// }
			// if (end < 0) {
			// end = 1;
			// }
			locator.match(call, this.getNodeSet());
			/*
			 * (CallExpression) new CallExpression(start, end,
			 * call.getReceiver(), call.getName(), call.getArgs())
			 */
		} else if (node instanceof RubyAliasExpression) {
			final RubyAliasExpression alias = (RubyAliasExpression) node;
			final MethodDeclaration method = new MethodDeclaration(alias
					.getNewValue(), alias.sourceStart(), alias.sourceEnd(),
					alias.sourceStart(), alias.sourceEnd()) {
				public boolean equals(Object obj) {
					if (obj == this)
						return true;
					if (obj instanceof ASTNode) {
						ASTNode s = (ASTNode) obj;
						if (s.sourceEnd() < 0 || s.sourceStart() < 0) {
							return false;
						}
						return sourceStart() == s.sourceStart()
								&& sourceEnd() == s.sourceEnd();
					}
					return false;
				}

				public int hashCode() {
					return this.sourceEnd() * 1001 + this.sourceEnd();
				}
			};
			locator.match(method, this.getNodeSet());
		} else if (node instanceof RubyAssignment) {
			// Assignment handling (this is static variable assignment.)

			RubyAssignment assignment = (RubyAssignment) node;
			ASTNode left = assignment.getLeft();
			if (left instanceof VariableReference) {
				VariableReference var = (VariableReference) left;
				FieldDeclaration field = new FieldDeclaration(var.getName(),
						var.sourceStart(), var.sourceEnd() - 1, var
								.sourceStart(), var.sourceEnd() - 1) {
					public boolean equals(Object obj) {
						if (obj == this)
							return true;
						if (obj instanceof ASTNode) {
							ASTNode s = (ASTNode) obj;
							if (s.sourceEnd() < 0 || s.sourceStart() < 0) {
								return false;
							}
							return sourceStart() == s.sourceStart()
									&& sourceEnd() == s.sourceEnd();
						}
						return false;
					}

					public int hashCode() {
						return this.sourceEnd() * 1001 + this.sourceEnd();
					}
				};
				locator.match(field, this.getNodeSet());
			}
		} else if (node instanceof Literal) {
			if (node instanceof RubyRegexpExpression) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "Regexp"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof StringLiteral) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "String"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof BooleanLiteral) {
				BooleanLiteral boolLit = (BooleanLiteral) node;
				TypeReference ref;
				if (boolLit.boolValue()) {
					ref = new TypeReferenceEquals(node.sourceStart(), node
							.sourceEnd(), "TrueClass"); //$NON-NLS-1$
				} else {
					ref = new TypeReferenceEquals(node.sourceStart(), node
							.sourceEnd(), "FalseClass"); //$NON-NLS-1$
				}
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof NumericLiteral) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "Fixnum"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof NilLiteral) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "NilClass"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof FloatNumericLiteral) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "Float"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof BigNumericLiteral) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "Bignum"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			}
		} else if (node instanceof Reference) {
			if (node instanceof RubySymbolReference) {
				TypeReference ref = new TypeReferenceEquals(node.sourceStart(),
						node.sourceEnd(), "Symbol"); //$NON-NLS-1$
				locator.match(ref, this.getNodeSet());
			} else if (node instanceof VariableReference) {
				reportSimpleReferenceMatch((SimpleReference) node, locator);
			} else if (node instanceof ConstantReference) {
				reportSimpleReferenceMatch((SimpleReference) node, locator);
				reportTypeReferenceMatch(node, locator);
			}
		} else if (node instanceof RubyColonExpression) {
			reportTypeReferenceMatch(node, locator);
		} else if (node instanceof RubyConstantDeclaration) {
			RubyConstantDeclaration var = (RubyConstantDeclaration) node;
			SimpleReference name = var.getName();
			FieldDeclaration field = new FieldDeclaration(name.getName(), name
					.sourceStart(), name.sourceEnd(), name.sourceStart(), name
					.sourceEnd()) {
				public boolean equals(Object obj) {
					if (obj == this)
						return true;
					if (obj instanceof ASTNode) {
						ASTNode s = (ASTNode) obj;
						if (s.sourceEnd() < 0 || s.sourceStart() < 0) {
							return false;
						}
						return sourceStart() == s.sourceStart()
								&& sourceEnd() == s.sourceEnd();
					}
					return false;
				}

				public int hashCode() {
					return this.sourceEnd() * 1001 + this.sourceEnd();
				}
			};
			locator.match(field, this.getNodeSet());
		}
	}
}
