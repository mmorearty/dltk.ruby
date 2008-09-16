package org.eclipse.dltk.ruby.ui.tests.folding;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.ruby.ui.tests.internal.RubyUITestsPlugin;
import org.eclipse.dltk.ui.text.folding.DefaultElementCommentResolver;
import org.eclipse.dltk.ui.text.folding.IElementCommentResolver;

public class RubyCommentElementTests extends AbstractModelTests {

	private final IElementCommentResolver resolver = new DefaultElementCommentResolver();
	private static final String PROJECT_NAME = "comments";

	public static Suite suite() {
		return new Suite(RubyCommentElementTests.class);
	}

	public RubyCommentElementTests(String name) {
		super(RubyUITestsPlugin.PLUGIN_ID, name);
	}

	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpScriptProject(PROJECT_NAME);
	}

	public void tearDownSuite() throws Exception {
		deleteProject(PROJECT_NAME);
		super.tearDownSuite();
	}

	public void testCommentPositioning1() throws ModelException {
		ISourceModule module = getSourceModule(PROJECT_NAME, "src",
				"comments1.rb");

		assertEquals("proc1", getElementByComment(module, "proc1_before"));
		assertEquals("proc1", getElementByComment(module, "proc1_inside"));

		assertEquals("Class1", getElementByComment(module, "Class1_before"));
		assertEquals("Class1", getElementByComment(module, "Class1_inside"));

		assertEquals("method1", getElementByComment(module, "method1_inside"));
		assertEquals("method1", getElementByComment(module, "method1_before"));

		assertEquals("proc2", getElementByComment(module, "proc2_before"));

		assertNull(getElementByComment(module, "eof_comment"));

	}

	/**
	 * 
	 * Gets a string pattern, searches for the comment that contains this
	 * pattern and returns IModelElement to which the comment corresponds
	 * 
	 */
	protected String getElementByComment(ISourceModule module,
			String commentPattern) throws ModelException {
		IModelElement el = resolver.getElementByCommentPosition(module, module
				.getSource().indexOf(commentPattern), 0);
		if (el != null)
			return el.getElementName();
		else
			return null;
	}
}
