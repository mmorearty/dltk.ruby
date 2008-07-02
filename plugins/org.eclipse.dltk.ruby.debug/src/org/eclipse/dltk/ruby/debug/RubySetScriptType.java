package org.eclipse.dltk.ruby.debug;

import org.eclipse.dltk.debug.core.model.CollectionScriptType;

public class RubySetScriptType extends CollectionScriptType {
	
	protected static String SET = "set";
	
	public RubySetScriptType() {
		super(SET);
	}
}
