/***** BEGIN LICENSE BLOCK *****
 * Version: CPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2001 Chad Fowler <chadfowler@chadfowler.com>
 * Copyright (C) 2001-2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2001-2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the CPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the CPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.ast;

import java.util.List;

import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.evaluator.Instruction;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.ISourcePositionHolder;

/**
 * an 'if' statement.
 * 
 * @author jpetersen
 */
public class IfNode extends Node {
	static final long serialVersionUID = -163780144332979551L;

	private final Node condition;
	private final Node thenBody;
	private final Node elseBody;
	private final ISourcePositionHolder thenKeyword;
	private final ISourcePositionHolder endKeyword;

	public IfNode(ISourcePosition position, Node condition, Node thenBody,
			Node elseBody, ISourcePositionHolder thenKeyword,
			ISourcePositionHolder endKeyword) {
		super(position, NodeTypes.IFNODE);
		this.condition = condition;
		this.thenBody = thenBody;
		this.elseBody = elseBody;
		this.thenKeyword = thenKeyword;
		this.endKeyword = endKeyword;
	}

	/**
	 * Accept for the visitor pattern.
	 * 
	 * @param iVisitor
	 *            the visitor
	 **/
	public Instruction accept(NodeVisitor iVisitor) {
		return iVisitor.visitIfNode(this);
	}

	/**
	 * Gets the condition.
	 * 
	 * @return Returns a Node
	 */
	public Node getCondition() {
		return condition;
	}

	/**
	 * Gets the elseBody.
	 * 
	 * @return Returns a Node
	 */
	public Node getElseBody() {
		return elseBody;
	}

	/**
	 * Gets the thenBody.
	 * 
	 * @return Returns a Node
	 */
	public Node getThenBody() {
		return thenBody;
	}

	public List childNodes() {
		return Node.createList(condition, thenBody, elseBody);
	}

	/**
	 * @return the thenKeyword
	 */
	public ISourcePositionHolder getThenKeyword() {
		return thenKeyword;
	}

	/**
	 * @return the endKeyword
	 */
	public ISourcePositionHolder getEndKeyword() {
		return endKeyword;
	}

	public Node getFirstBody() {
		return getThenBody();
	}

	public Node getSecondBody() {
		return getElseBody();
	}

	public boolean isInline() {
		return false;
	}

	public static class ElseIf extends IfNode {

		/**
		 * @param position
		 * @param condition
		 * @param thenBody
		 * @param elseBody
		 */
		public ElseIf(ISourcePosition position, Node condition, Node thenBody,
				Node elseBody, ISourcePositionHolder thenKeyword) {
			super(position, condition, thenBody, elseBody, thenKeyword, null);
		}

	}

	public static class Unless extends IfNode {

		/**
		 * @param position
		 * @param condition
		 * @param thenBody
		 * @param elseBody
		 */
		public Unless(ISourcePosition position, Node condition, Node thenBody,
				Node elseBody, ISourcePositionHolder thenKeyword,
				ISourcePositionHolder endKeyword) {
			super(position, condition, thenBody, elseBody, thenKeyword,
					endKeyword);
		}

		public Node getFirstBody() {
			return getElseBody();
		}

		public Node getSecondBody() {
			return getThenBody();
		}
	}

	public static class Inline extends IfNode {

		/**
		 * @param position
		 * @param condition
		 * @param thenBody
		 * @param elseBody
		 */
		public Inline(ISourcePosition position, Node condition, Node thenBody,
				Node elseBody) {
			super(position, condition, thenBody, elseBody, null, null);
		}

		public boolean isInline() {
			return true;
		}

	}

	public static class InlineUnless extends Inline {

		/**
		 * @param position
		 * @param condition
		 * @param thenBody
		 * @param elseBody
		 */
		public InlineUnless(ISourcePosition position, Node condition,
				Node thenBody, Node elseBody) {
			super(position, condition, thenBody, elseBody);
		}

	}

}
