package org.eclipse.dltk.ruby.debug;

import org.eclipse.dltk.debug.core.model.ArrayScriptType;
import org.eclipse.dltk.debug.core.model.AtomicScriptType;
import org.eclipse.dltk.debug.core.model.ComplexScriptType;
import org.eclipse.dltk.debug.core.model.HashScriptType;
import org.eclipse.dltk.debug.core.model.IScriptType;
import org.eclipse.dltk.debug.core.model.IScriptTypeFactory;
import org.eclipse.dltk.debug.core.model.StringScriptType;

public class RubyTypeFactory implements IScriptTypeFactory {
	private static final String[] atomicTypes = { "Bignum", "FalseClass", //$NON-NLS-1$  //$NON-NLS-2$
			"Fixnum", "Float", "Integer", "NilClass", "Numeric", "Range", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"Regexp", "String", "Symbol", "TrueClass" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	public IScriptType buildType(String type) {
		if (STRING.equals(type)) { //$NON-NLS-1$
			return new StringScriptType(type);
		}

		for (int i = 0; i < atomicTypes.length; ++i) {
			if (atomicTypes[i].equals(type)) {
				return new AtomicScriptType(type);
			}
		}

		if (ARRAY.equals(type)) {
			return new ArrayScriptType();
		}

		if (HASH.equals(type)) {
			return new HashScriptType();
		}

		if (RubySetScriptType.SET.equals(type)) {
			return new RubySetScriptType();
		}

		return new ComplexScriptType(type);
	}
}
