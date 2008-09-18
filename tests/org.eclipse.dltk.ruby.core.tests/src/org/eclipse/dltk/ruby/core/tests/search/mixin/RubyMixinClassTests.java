package org.eclipse.dltk.ruby.core.tests.search.mixin;

import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.ruby.core.tests.Activator;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinClass;
import org.eclipse.dltk.ruby.internal.parser.mixin.RubyMixinModel;

public class RubyMixinClassTests extends AbstractModelTests {

	public RubyMixinClassTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Suite suite() {
		return new Suite(RubyMixinClassTests.class);
	}

	private static final String PROJECT_NAME = "mixin-includes";

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpScriptProject(PROJECT_NAME);
		waitUntilIndexesReady();
	}

	public void tearDownSuite() throws Exception {
		deleteProject(PROJECT_NAME);
		super.tearDownSuite();
	}

	public void testIncludeA() {
		RubyMixinModel m = RubyMixinModel.getWorkspaceInstance();
		RubyMixinClass a = (RubyMixinClass) m.createRubyElement("AAA%");
		RubyMixinClass[] includes = a.getIncluded();
		assertEquals(1, includes.length);
		assertEquals("BBB%", includes[0].getKey());
	}

	public void testIncludeB() {
		RubyMixinModel m = RubyMixinModel.getWorkspaceInstance();
		RubyMixinClass b = (RubyMixinClass) m.createRubyElement("BBB%");
		RubyMixinClass[] includes = b.getIncluded();
		assertEquals(1, includes.length);
		assertEquals("AAA%", includes[0].getKey());
	}

}
