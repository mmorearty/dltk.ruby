/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *     xored software, Inc. - RubyDocumentation display improvements (Alex Panchenko <alex@xored.com>)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.core.codeassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.references.ConstantReference;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.codeassist.IAssistParser;
import org.eclipse.dltk.codeassist.ScriptSelectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.ScriptModelUtil;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.search.IDLTKSearchConstants;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.core.search.SearchMatch;
import org.eclipse.dltk.core.search.SearchParticipant;
import org.eclipse.dltk.core.search.SearchPattern;
import org.eclipse.dltk.core.search.SearchRequestor;
import org.eclipse.dltk.core.search.TypeNameMatch;
import org.eclipse.dltk.core.search.TypeNameMatchRequestor;
import org.eclipse.dltk.ruby.ast.RubyAssignment;
import org.eclipse.dltk.ruby.ast.RubyColonExpression;
import org.eclipse.dltk.ruby.ast.RubyForStatement2;
import org.eclipse.dltk.ruby.ast.RubyMethodArgument;
import org.eclipse.dltk.ruby.ast.RubySuperExpression;
import org.eclipse.dltk.ruby.core.RubyPlugin;
import org.eclipse.dltk.ruby.core.model.FakeField;
import org.eclipse.dltk.ruby.core.utils.RubySyntaxUtils;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinClass;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinElementInfo;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinMethod;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinModel;
import org.eclipse.dltk.ruby.internal.parsers.jruby.ASTUtils;
import org.eclipse.dltk.ruby.typeinference.RubyClassType;
import org.eclipse.dltk.ruby.typeinference.RubyModelUtils;
import org.eclipse.dltk.ruby.typeinference.RubyTypeInferencingUtils;
import org.eclipse.dltk.ruby.typeinference.evaluators.ColonExpressionEvaluator;
import org.eclipse.dltk.ruby.typeinference.evaluators.ConstantReferenceEvaluator;
import org.eclipse.dltk.ruby.typeinference.goals.NonTypeConstantTypeGoal;
import org.eclipse.dltk.ti.BasicContext;
import org.eclipse.dltk.ti.DLTKTypeInferenceEngine;
import org.eclipse.dltk.ti.GoalState;
import org.eclipse.dltk.ti.goals.AbstractTypeGoal;
import org.eclipse.dltk.ti.goals.ExpressionTypeGoal;
import org.eclipse.dltk.ti.goals.IGoal;
import org.eclipse.dltk.ti.types.IEvaluatedType;

public class RubySelectionEngine extends ScriptSelectionEngine {
	public static final boolean DEBUG = DLTKCore.DEBUG_SELECTION;

	protected int actualSelectionStart;

	protected int actualSelectionEnd;

	private Set selectionElements = new HashSet();

	private RubySelectionParser parser = new RubySelectionParser();

	private ISourceModule sourceModule;

	private ASTNode[] wayToNode;

	private DLTKTypeInferenceEngine inferencer;

	private TypeDeclaration getEnclosingType(ASTNode node) {
		return ASTUtils.getEnclosingType(wayToNode, node, true);
	}

	private CallExpression getEnclosingCallNode(ASTNode node) {
		return ASTUtils.getEnclosingCallNode(wayToNode, node, true);
	}

	public RubySelectionEngine() {
		inferencer = new DLTKTypeInferenceEngine();
	}

	public IAssistParser getParser() {
		return null;
	}

	private RubyMixinModel mixinModel;

	public IModelElement[] select(
			org.eclipse.dltk.compiler.env.ISourceModule sourceUnit,
			int selectionSourceStart, int selectionSourceEnd) {
		sourceModule = (ISourceModule) sourceUnit.getModelElement();
		mixinModel = RubyMixinModel
				.getInstance(sourceModule.getScriptProject());
		String source = sourceUnit.getSourceContents();
		if (DEBUG) {
			System.out.print("SELECTION IN "); //$NON-NLS-1$
			System.out.print(sourceUnit.getFileName());
			System.out.print(" FROM "); //$NON-NLS-1$
			System.out.print(selectionSourceStart);
			System.out.print(" TO "); //$NON-NLS-1$
			System.out.println(selectionSourceEnd);
			System.out.println("SELECTION - Source :"); //$NON-NLS-1$
			System.out.println(source);
		}
		if (!checkSelection(source, selectionSourceStart, selectionSourceEnd)) {
			return new IModelElement[0];
		}
		actualSelectionEnd--; // inclusion fix
		if (DEBUG) {
			System.out.print("SELECTION - Checked : \""); //$NON-NLS-1$
			System.out.print(source.substring(actualSelectionStart,
					actualSelectionEnd + 1));
			System.out.println('"');
		}

		try {
			ModuleDeclaration parsedUnit = this.parser.parse(sourceUnit);

			if (parsedUnit != null) {
				if (DEBUG) {
					System.out.println("SELECTION - AST :"); //$NON-NLS-1$
					System.out.println(parsedUnit.toString());
				}

				ASTNode node = ASTUtils.findMinimalNode(parsedUnit,
						actualSelectionStart, actualSelectionEnd);

				if (node == null)
					return new IModelElement[0];

				this.wayToNode = ASTUtils.restoreWayToNode(parsedUnit, node);

				if (node instanceof TypeDeclaration) {
					selectionOnTypeDeclaration(parsedUnit,
							(TypeDeclaration) node);
				} else if (node instanceof MethodDeclaration) {
					selectionOnMethodDeclaration(parsedUnit,
							(MethodDeclaration) node);
				} else if (node instanceof ConstantReference
						|| node instanceof RubyColonExpression) {
					selectTypes(parsedUnit, node);
				} else if (node instanceof VariableReference) {
					selectionOnVariable(parsedUnit, (VariableReference) node);
				} else if (node instanceof RubyMethodArgument) {
					selectOnMethodArgument(parsedUnit,
							(RubyMethodArgument) node);
				} else if (node instanceof RubySuperExpression) {
					selectOnSuper(parsedUnit, (RubySuperExpression) node);
				} else {
					CallExpression parentCall = this.getEnclosingCallNode(node);
					if (parentCall != null) {
						selectOnMethod(parsedUnit, parentCall);
					} else { // parentCall == null
					}
				}
			}
		} catch (IndexOutOfBoundsException e) { // work-around internal failure
			RubyPlugin.log(e);
		}

		return (IModelElement[]) selectionElements
				.toArray(new IModelElement[selectionElements.size()]);
	}

	private void selectOnSuper(ModuleDeclaration parsedUnit,
			RubySuperExpression superExpr) {
		RubyClassType selfClass = RubyTypeInferencingUtils.determineSelfClass(
				mixinModel, sourceModule, parsedUnit, superExpr.sourceStart());
		MethodDeclaration enclosingMethod = ASTUtils.getEnclosingMethod(
				wayToNode, superExpr, false);
		if (enclosingMethod != null) {
			String name = enclosingMethod.getName();
			RubyMixinClass rubyClass = mixinModel.createRubyClass(selfClass);
			RubyMixinClass superclass = rubyClass.getSuperclass();
			RubyMixinMethod method = superclass.getMethod(name);
			if (method != null) {
				IMethod[] sourceMethods = method.getSourceMethods();
				addArrayToCollection(sourceMethods, selectionElements);
			}
		}
	}

	private void selectOnColonExpression(ModuleDeclaration parsedUnit,
			RubyColonExpression node) {
		BasicContext basicContext = new BasicContext(sourceModule, parsedUnit);
		ColonExpressionEvaluator evaluator = new ColonExpressionEvaluator(
				new ExpressionTypeGoal(basicContext, node));
		IGoal[] init = evaluator.init();
		if (init == null || init.length == 0) {
			// should never be here
			// System.err.println("Why did ColonExpressionEvaluator evaluated so
			// fast?");
		} else {
			IEvaluatedType leftType = inferencer.evaluateType(
					(AbstractTypeGoal) init[0], -1);
			IGoal[] goals = evaluator.subGoalDone(init[0], leftType,
					GoalState.DONE);
			if (goals == null || goals.length == 0) { // good, we have
				// type-constant
				Object evaluatedType = evaluator.produceResult();
				if (evaluatedType instanceof RubyClassType) {
					RubyMixinClass mixinClass = mixinModel
							.createRubyClass((RubyClassType) evaluatedType);
					if (mixinClass != null)
						addArrayToCollection(mixinClass.getSourceTypes(),
								selectionElements);
				}
			} else {
				if (goals[0] instanceof NonTypeConstantTypeGoal) {
					processNonTypeConstant((NonTypeConstantTypeGoal) (goals[0]));
				}
			}
		}
	}

	/**
	 * Uses goal info for selection on non-type goal
	 * 
	 * @param ngoal
	 */
	private void processNonTypeConstant(NonTypeConstantTypeGoal ngoal) {
		IMixinElement element = ngoal.getElement();
		if (element != null) {
			Object[] eObjects = element.getAllObjects();
			for (int i = 0; i < eObjects.length; i++) {
				if (eObjects[i] instanceof RubyMixinElementInfo) {
					RubyMixinElementInfo info = (RubyMixinElementInfo) eObjects[i];
					Object obj = info.getObject();
					if (obj instanceof IModelElement) {
						this.selectionElements.add(obj);
					}
				}
			}
		}
	}

	private void selectOnConstant(ModuleDeclaration parsedUnit,
			ConstantReference node) {
		BasicContext basicContext = new BasicContext(sourceModule, parsedUnit);
		ConstantReferenceEvaluator evaluator = new ConstantReferenceEvaluator(
				new ExpressionTypeGoal(basicContext, node));
		IGoal[] init = evaluator.init();
		if (init == null || init.length == 0) {
			Object evaluatedType = evaluator.produceResult();
			if (evaluatedType instanceof RubyClassType) {
				RubyMixinClass mixinClass = mixinModel
						.createRubyClass((RubyClassType) evaluatedType);
				if (mixinClass != null)
					addArrayToCollection(mixinClass.getSourceTypes(),
							selectionElements);
			}
		} else if (init[0] instanceof NonTypeConstantTypeGoal) {
			// it'a non-type constant
			processNonTypeConstant((NonTypeConstantTypeGoal) init[0]);
		}
	}

	/**
	 * Checks, whether giver selection is correct selection, or can be expanded
	 * to correct selection region. As result will set
	 * this.actualSelection(Start|End) properly. In case of incorrect selection,
	 * will return false.
	 * 
	 * @param source
	 * @param start
	 * @param end
	 * @return
	 */
	protected boolean checkSelection(String source, int start, int end) {
		if (start > end) {
			int x = start;
			start = end;
			end = x;
		}
		if (start + 1 == end) {
			ISourceRange range = RubySyntaxUtils.getEnclosingName(source, end);
			if (range != null) {
				this.actualSelectionStart = range.getOffset();
				this.actualSelectionEnd = this.actualSelectionStart
						+ range.getLength();
				// return true;
			}
			ISourceRange range2 = RubySyntaxUtils.insideMethodOperator(source,
					end);
			if (range != null
					&& (range2 == null || range2.getLength() < range
							.getLength()))
				return true;
			if (range2 != null) {
				this.actualSelectionStart = range2.getOffset();
				this.actualSelectionEnd = this.actualSelectionStart
						+ range2.getLength();
				return true;
			}
		} else {
			if (start >= 0 && end < source.length()) {
				String str = source.substring(start, end + 1);
				if (RubySyntaxUtils.isRubyName(str)) {
					this.actualSelectionStart = start;
					this.actualSelectionEnd = end + 1;
					return true;
				}
			}
		}

		return false;
	}

	private void selectTypes(ModuleDeclaration parsedUnit, ASTNode node) {
		if (node instanceof ConstantReference) {
			selectOnConstant(parsedUnit, (ConstantReference) node);
		} else if (node instanceof RubyColonExpression) {
			selectOnColonExpression(parsedUnit, (RubyColonExpression) node);
		}

		if (selectionElements.isEmpty()) {
			TypeNameMatchRequestor requestor = new TypeNameMatchRequestor() {
				public void acceptTypeNameMatch(TypeNameMatch match) {
					selectionElements.add(match.getType());
				}
			};
			String unqualifiedName = null;
			if (node instanceof RubyColonExpression) {
				RubyColonExpression expr = (RubyColonExpression) node;
				unqualifiedName = expr.getName();
			} else if (node instanceof ConstantReference) {
				ConstantReference expr = (ConstantReference) node;
				unqualifiedName = expr.getName();
			}
			if (unqualifiedName != null) {
				ScriptModelUtil.searchTypeDeclarations(sourceModule
						.getScriptProject(), unqualifiedName, requestor);
			}
		}
	}

	private void selectOnMethodArgument(ModuleDeclaration parsedUnit,
			RubyMethodArgument arg) {
		selectionElements.add(createLocalVariable(arg.getName(), arg
				.sourceStart(), arg.sourceEnd()));
	}

	private void selectionOnVariable(ModuleDeclaration parsedUnit,
			VariableReference e) {
		String name = e.getName();
		if (name.startsWith("@")) { //$NON-NLS-1$
			IField[] fields = RubyModelUtils.findFields(mixinModel,
					sourceModule, parsedUnit, name, e.sourceStart());
			addArrayToCollection(fields, selectionElements);
		} else {
			/*
			 * local vars (legacy, saved for speed reasons: we don't need to use
			 * mixin model for local vars)
			 */
			ASTNode parentScope = null;
			for (int i = wayToNode.length; --i >= 0;) {
				final ASTNode node = wayToNode[i];
				if (node instanceof MethodDeclaration
						|| node instanceof TypeDeclaration
						|| node instanceof ModuleDeclaration
						|| node instanceof RubyForStatement2) {
					parentScope = node;
					break;
				}
			}
			if (parentScope != null) {
				RubyAssignment[] assignments = RubyTypeInferencingUtils
						.findLocalVariableAssignments(parentScope, e, name);
				if (assignments.length > 0) {
					final ASTNode left = assignments[0].getLeft();
					selectionElements.add(createLocalVariable(name, left
							.sourceStart(), left.sourceEnd()));
				} else {
					selectionElements.add(createLocalVariable(name, e
							.sourceStart(), e.sourceEnd()));
				}
			}
		}
	}

	private IField createLocalVariable(String name, int nameStart, int nameEnd) {
		return new FakeField(sourceModule, name, nameStart,
				nameEnd - nameStart);
	}

	private IType[] getSourceTypesForClass(ModuleDeclaration parsedUnit,
			ASTNode statement) {
		ExpressionTypeGoal typeGoal = new ExpressionTypeGoal(new BasicContext(
				sourceModule, parsedUnit), statement);
		IEvaluatedType evaluatedType = this.inferencer.evaluateType(typeGoal,
				5000);
		if (evaluatedType instanceof RubyClassType) {
			RubyMixinClass mixinClass = mixinModel
					.createRubyClass((RubyClassType) evaluatedType);
			if (mixinClass != null)
				return mixinClass.getSourceTypes();
		}
		return new IType[0];
	}

	private void selectionOnTypeDeclaration(ModuleDeclaration parsedUnit,
			TypeDeclaration typeDeclaration) {
		// if (typeDeclaration instanceof RubyClassDeclaration) {
		// RubyClassDeclaration rcd = (RubyClassDeclaration) typeDeclaration;
		// IType[] types = getSourceTypesForClass(parsedUnit, rcd
		// .getClassName());
		// selectionElements.addAll(Arrays.asList(types));
		// }
		IModelElement elementAt = null;
		try {
			elementAt = sourceModule
					.getElementAt(typeDeclaration.sourceStart() + 1);
		} catch (ModelException e) {
			RubyPlugin.log(e);
		}
		if (elementAt != null)
			selectionElements.add(elementAt);
	}

	private void selectionOnMethodDeclaration(ModuleDeclaration parsedUnit,
			MethodDeclaration methodDeclaration) {
		IModelElement elementAt = null;
		try {
			elementAt = sourceModule.getElementAt(methodDeclaration
					.sourceStart() + 1);
		} catch (ModelException e) {
			RubyPlugin.log(e);
		}
		if (elementAt != null)
			selectionElements.add(elementAt);
	}

	private void selectOnMethod(ModuleDeclaration parsedUnit,
			CallExpression parentCall) {
		String methodName = parentCall.getName();
		ASTNode receiver = parentCall.getReceiver();

		final List availableMethods = new ArrayList();

		if (receiver == null) {
			IEvaluatedType type = RubyTypeInferencingUtils.determineSelfClass(
					mixinModel, sourceModule, parsedUnit, parentCall
							.sourceStart());
			if ((type != null) && "Object".equals(type.getTypeName())) { //$NON-NLS-1$
				ExpressionTypeGoal goal = new ExpressionTypeGoal(
						new BasicContext(sourceModule, parsedUnit), parsedUnit);
				IEvaluatedType type2 = inferencer.evaluateType(goal, 2000);
				if (type2 != null) {
					type = type2;
				}
			}
			IMethod[] m = RubyModelUtils.searchClassMethodsExact(mixinModel,
					sourceModule, parsedUnit, type, methodName);
			addArrayToCollection(m, availableMethods);
		} else {
			ExpressionTypeGoal goal = new ExpressionTypeGoal(new BasicContext(
					sourceModule, parsedUnit), receiver);
			IEvaluatedType type = inferencer.evaluateType(goal, 5000);
			IMethod[] m = RubyModelUtils.searchClassMethodsExact(mixinModel,
					sourceModule, parsedUnit, type, methodName);
			addArrayToCollection(m, availableMethods);
			if (receiver instanceof VariableReference) {
				IMethod[] availableMethods2 = RubyModelUtils
						.getSingletonMethods(mixinModel,
								(VariableReference) receiver, parsedUnit,
								sourceModule, methodName);
				addArrayToCollection(availableMethods2, availableMethods);
			}
		}

		if (availableMethods.isEmpty()) {
			searchMethodDeclarations(sourceModule.getScriptProject(),
					methodName, availableMethods);
		}

		if (!availableMethods.isEmpty()) {
			for (int i = 0, size = availableMethods.size(); i < size; ++i) {
				final IMethod m = (IMethod) availableMethods.get(i);
				if (methodName.equals(methodName)) {
					selectionElements.add(m);
				}
			}
		}
	}

	private static void searchMethodDeclarations(IScriptProject project,
			String methodName, final List availableMethods) {
		final SearchRequestor requestor = new SearchRequestor() {

			public void acceptSearchMatch(SearchMatch match)
					throws CoreException {
				IModelElement modelElement = (IModelElement) match.getElement();
				ISourceModule sm = (ISourceModule) modelElement
						.getAncestor(IModelElement.SOURCE_MODULE);
				IModelElement elementAt = sm.getElementAt(match.getOffset());
				if (elementAt.getElementType() == IModelElement.METHOD) {
					availableMethods.add(elementAt);
				}
			}

		};
		final IDLTKSearchScope scope = SearchEngine.createSearchScope(project);
		try {
			final SearchEngine engine = new SearchEngine();
			final SearchPattern pattern = SearchPattern.createPattern(
					methodName, IDLTKSearchConstants.METHOD,
					IDLTKSearchConstants.DECLARATIONS,
					SearchPattern.R_EXACT_MATCH
							| SearchPattern.R_CASE_SENSITIVE,
					DLTKLanguageManager.getLanguageToolkit(project));
			final SearchParticipant[] participants = new SearchParticipant[] { SearchEngine
					.getDefaultSearchParticipant() };
			engine.search(pattern, participants, scope, requestor, null);
		} catch (CoreException e) {
			RubyPlugin.log(e);
		}
	}

	/**
	 * @param src
	 * @param dest
	 */
	private static void addArrayToCollection(IMember[] src, Collection dest) {
		if (src != null) {
			for (int i = 0, size = src.length; i < size; ++i) {
				dest.add(src[i]);
			}
		}
	}

}
