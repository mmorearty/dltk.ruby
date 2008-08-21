package org.eclipse.dltk.ruby.testing.internal;

import org.eclipse.dltk.core.PriorityClassDLTKExtensionManager;
import org.eclipse.dltk.core.PriorityDLTKExtensionManager.ElementInfo;
import org.eclipse.dltk.ruby.testing.IRubyTestingEngine;

public final class RubyTestingEngineManager {
	private static final PriorityClassDLTKExtensionManager manager = new PriorityClassDLTKExtensionManager(
			Activator.PLUGIN_ID + ".rubyTestEngine", "id"); //$NON-NLS-1$ //$NON-NLS-2$

	public static IRubyTestingEngine[] getEngines() {
		ElementInfo[] elementInfos = manager.getElementInfos();
		IRubyTestingEngine[] engines = new IRubyTestingEngine[elementInfos.length];
		for (int i = 0; i < elementInfos.length; i++) {
			engines[i] = (IRubyTestingEngine) manager
					.getInitObject(elementInfos[i]);
		}
		return engines;
	}
}
