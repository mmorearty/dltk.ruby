package org.eclipse.dltk.ruby.internal.parser.mixin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.internal.core.ModelElement;
import org.eclipse.dltk.ruby.ast.RubyAliasExpression;
import org.eclipse.dltk.ruby.core.model.FakeMethod;

public class AliasedRubyMixinMethod extends RubyMixinMethod {

	private final RubyMixinAlias alias;

	public AliasedRubyMixinMethod(RubyMixinModel model, RubyMixinAlias alias) {
		super(model, alias.getKey());
		this.alias = alias;
		final RubyAliasExpression node = alias.getAlias();
		final int length = node.sourceEnd() - node.sourceStart();
		final IMethod sourceMethod = findSourceMethod(model, alias);
		final ModelElement fakeMethodParent;
		if (sourceMethod != null
				&& sourceMethod.getParent() instanceof ModelElement) {
			fakeMethodParent = (ModelElement) sourceMethod.getParent();
		} else {
			fakeMethodParent = (ModelElement) alias.getSourceModule();
		}
		FakeMethod fakeMethod = new FakeMethod(fakeMethodParent, node
				.getNewValue(), node.sourceStart(), length, node.sourceStart(),
				length);
		if (sourceMethod != null) {
			try {
				fakeMethod.setFlags(sourceMethod.getFlags());
				fakeMethod.setParameters(sourceMethod.getParameters());
			} catch (ModelException e) {
			}
		}
		this.setSourceMethods(new IMethod[] { fakeMethod });
	}

	private static IMethod findSourceMethod(RubyMixinModel model,
			RubyMixinAlias alias) {
		final IMethod[] sourceMethods = RubyMixinMethod.getSourceMethods(model,
				alias.getOldKey());
		if (sourceMethods.length == 1 && sourceMethods[0] != null) {
			return sourceMethods[0];
		} else {
			return null;
		}
	}

	public String getName() {
		return alias.getNewName();
	}

	public RubyMixinVariable[] getVariables() {
		List result = new ArrayList();
		IMixinElement mixinElement = model.getRawModel().get(alias.getOldKey());
		IMixinElement[] children = mixinElement.getChildren();
		for (int i = 0; i < children.length; i++) {
			IRubyMixinElement element = model.createRubyElement(children[i]);
			if (element instanceof RubyMixinVariable)
				result.add(element);
		}
		return (RubyMixinVariable[]) result
				.toArray(new RubyMixinVariable[result.size()]);
	}

}
