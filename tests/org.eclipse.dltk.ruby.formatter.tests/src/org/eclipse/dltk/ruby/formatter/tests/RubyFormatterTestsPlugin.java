package org.eclipse.dltk.ruby.formatter.tests;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.formatter.tests.ScriptedTest.IScriptedTestContext;
import org.eclipse.dltk.ruby.formatter.RubyFormatter;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class RubyFormatterTestsPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.dltk.ruby.formatter.tests"; //$NON-NLS-1$

	// The shared instance
	private static RubyFormatterTestsPlugin plugin;

	/**
	 * The constructor
	 */
	public RubyFormatterTestsPlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static RubyFormatterTestsPlugin getDefault() {
		return plugin;
	}

	public static void warn(String message) {
		warn(message, null);
	}

	public static void warn(String message, Throwable throwable) {
		log(new Status(IStatus.WARNING, PLUGIN_ID, 0, message, throwable));
	}

	public static void error(String message) {
		error(message, null);
	}

	public static void error(String message, Throwable throwable) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, throwable));
	}

	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}

	public static final IScriptedTestContext CONTEXT = new IScriptedTestContext() {

		public Bundle getResourceBundle() {
			return getDefault().getBundle();
		}

		public String getCharset() {
			return AllTests.CHARSET;
		}

		public IScriptFormatter createFormatter(Map preferences) {
			if (preferences != null) {
				final Map prefs = TestRubyFormatter.createTestingPreferences();
				prefs.putAll(preferences);
				return new TestRubyFormatter(Util.LINE_SEPARATOR, prefs);
			} else {
				return new TestRubyFormatter();
			}
		}

		public String validateOptionName(String name) {
			if (RubyFormatter.isBooleanOption(name)
					|| RubyFormatter.isIntegerOption(name)) {
				return name;
			} else {
				return null;
			}
		}

		public String validateOptionValue(String name, String value) {
			if (RubyFormatter.isBooleanOption(name)) {
				return "false".equals(value) || "true".equals(value) ? value
						: null;
			} else if (RubyFormatter.isIntegerOption(name)) {
				try {
					Integer.parseInt(value);
					return value;
				} catch (NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	};

}
