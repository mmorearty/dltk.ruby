package org.eclipse.dltk.ruby.ui.bot.tests.editor;

import junit.framework.Test;

import org.eclipse.dltk.ruby.internal.ui.RubyPerspective;
import org.eclipse.dltk.ruby.ui.bot.tests.AllTests;
import org.eclipse.dltk.uibot.tests.AbstractSWTBotTests;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.test.performance.Dimension;
import org.eclipse.test.performance.Performance;
import org.eclipse.test.performance.PerformanceMeter;
import org.eclipse.ui.IEditorPart;

public class RubyEditorTests extends AbstractSWTBotTests {

	private static final String PROJECT_FOLDER_NAME = "RubyEditor";
	private String fProjectFolderName;

	private IEditorPart edpar;

	public RubyEditorTests(String name) {
		super(AllTests.PLUGIN_ID, name);
		this.fProjectFolderName = PROJECT_FOLDER_NAME;
	}

	public static Test suite() {
		return new Suite(RubyEditorTests.class);
	}

	public void setUp() throws Exception {

		setUpScriptProject(PROJECT_FOLDER_NAME);

		waitForProjectToBuild();

		super.setUp();
	}

	public void tearDown() throws Exception {
		closeActiveEditorPart();
		deleteProject(PROJECT_FOLDER_NAME);
		super.tearDown();
	}

	private void closeActiveEditorPart() {
		closeEditorPart(edpar);
	}

	private IEditorPart openRubyEditorScript(String fileName) throws Exception {

		return openSourceFileInEditor(fProjectFolderName, "src", fileName,
				RubyPerspective.PERSPECTIVE_ID);
	}

	public void testAutoEndKeyword() throws Exception {

		Performance perf = Performance.getDefault();
		String scenarioId = perf.getDefaultScenarioId(this);
		PerformanceMeter performanceMeter = perf
				.createPerformanceMeter(scenarioId);
		perf.tagAsSummary(performanceMeter, "Auto end keyword",
				Dimension.ELAPSED_PROCESS);
		performanceMeter.start();

		edpar = openRubyEditorScript("empty.rb");

		SWTBotEclipseEditor ed = bot.activeEditor();

		int docLength = ed.getText().length();

		ed.selectRange(0, 0, docLength);

		ed.typeText("def f1\n");
		assertEquals("end", ed.getTextOnLine(2));

		closeEditorPart(edpar);
		performanceMeter.stop();
		performanceMeter.commit();
	}

}
