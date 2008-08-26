package org.eclipse.dltk.ruby.testing.internal;

import org.eclipse.dltk.core.PriorityClassDLTKExtensionManager;
import org.eclipse.dltk.core.PriorityDLTKExtensionManager.ElementInfo;
import org.eclipse.dltk.ruby.testing.IRubyTestingEngine;

public final class RubyTestingEngineManager {
	private static final PriorityClassDLTKExtensionManager manager = new PriorityClassDLTKExtensionManager(
			Activator.PLUGIN_ID + ".engine", "id"); //$NON-NLS-1$ //$NON-NLS-2$

	public static IRubyTestingEngine[] getEngines() {
		ElementInfo[] elementInfos = manager.getElementInfos();
		IRubyTestingEngine[] engines = new IRubyTestingEngine[elementInfos.length];
		int engineCount = 0;
		for (int i = 0; i < elementInfos.length; i++) {
			final IRubyTestingEngine engine = (IRubyTestingEngine) manager
					.getInitObject(elementInfos[i]);
			if (engine != null) {
				engines[engineCount++] = engine;
			}
		}
		if (engineCount != elementInfos.length) {
			System.arraycopy(engines, 0,
					engines = new IRubyTestingEngine[engineCount], 0,
					engineCount);
		}
		return engines;
	}
}
