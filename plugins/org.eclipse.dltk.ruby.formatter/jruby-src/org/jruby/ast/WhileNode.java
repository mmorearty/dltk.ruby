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
 * Copyright (C) 2001-2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2001-2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
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
 * Represents a while stetement. This could be the both versions:
 * 
 * while &lt;condition&gt; &lt;body&gt; end
 * 
 * and
 * 
 * &lt;body&gt; 'while' &lt;condition&gt;
 * 
 * @author jpetersen
 */
public class WhileNode extends Node {
	static final long serialVersionUID = -5355364190446060873L;

	protected final Node conditionNode;
	protected final Node bodyNode;
	private final boolean evaluateAtStart;
	private final ISourcePositionHolder keyword;
	private final ISourcePositionHolder end;

	/**
	 * @return the end
	 */
	public ISourcePositionHolder getEnd() {
		return end;
	}

	/**
	 * @return the keyword
	 */
	public ISourcePositionHolder getKeyword() {
		return keyword;
	}

	public WhileNode(ISourcePosition position, Node conditionNode,
			Node bodyNode, ISourcePositionHolder keyword,
			ISourcePositionHolder end) {
		this(position, conditionNode, bodyNode, true, keyword, end);
	}

	protected WhileNode(ISourcePosition position, Node conditionNode,
			Node bodyNode, boolean evalAtStart, ISourcePositionHolder keyword,
			ISourcePositionHolder end) {
		super(position, NodeTypes.WHILENODE);
		this.conditionNode = conditionNode;
		this.bodyNode = bodyNode;
		this.evaluateAtStart = evalAtStart;
		this.keyword = keyword;
		this.end = end;
	}

	/**
	 * Accept for the visitor pattern.
	 * 
	 * @param iVisitor
	 *            the visitor
	 **/
	public Instruction accept(NodeVisitor iVisitor) {
		return iVisitor.visitWhileNode(this);
	}

	/**
	 * Gets the bodyNode.
	 * 
	 * @return Returns a Node
	 */
	public Node getBodyNode() {
		return bodyNode;
	}

	/**
	 * Gets the conditionNode.
	 * 
	 * @return Returns a Node
	 */
	public Node getConditionNode() {
		return conditionNode;
	}

	/**
	 * Determine whether this is while or do while
	 * 
	 * @return true if you are a while, false if do while
	 */
	public boolean evaluateAtStart() {
		return evaluateAtStart;
	}

	public List childNodes() {
		return Node.createList(conditionNode, bodyNode);
	}

	public boolean isBlock() {
		return true;
	}

	public static class Inline extends WhileNode {

		/**
		 * @param position
		 * @param conditionNode
		 * @param bodyNode
		 * @param evalAtStart
		 * @param end
		 */
		public Inline(ISourcePosition position, Node conditionNode,
				Node bodyNode, boolean evalAtStart,
				ISourcePositionHolder keyword) {
			super(position, conditionNode, bodyNode, evalAtStart, keyword, null);
		}

		public boolean isBlock() {
			return false;
		}

		public List childNodes() {
			return Node.createList(bodyNode, conditionNode);
		}
	}

}
