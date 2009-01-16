/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.testing.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.debug.ui.messages.DLTKLaunchConfigurationsMessages;
import org.eclipse.dltk.internal.testing.launcher.DLTKTestingMigrationDelegate;
import org.eclipse.dltk.internal.ui.ModelElementComparator;
import org.eclipse.dltk.internal.ui.StandardModelElementContentProvider;
import org.eclipse.dltk.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.dltk.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.ruby.internal.debug.ui.launchConfigurations.RubyMainLaunchConfigurationTab;
import org.eclipse.dltk.testing.DLTKTestingConstants;
import org.eclipse.dltk.testing.DLTKTestingMessages;
import org.eclipse.dltk.testing.ITestingEngine;
import org.eclipse.dltk.testing.TestingEngineDetectResult;
import org.eclipse.dltk.testing.TestingEngineManager;
import org.eclipse.dltk.ui.ModelElementLabelProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

public class RubyTestingMainLaunchConfigurationTab extends
		RubyMainLaunchConfigurationTab {

	private Button detect;
	private Combo engineType;
	private Map nameToId = new HashMap();
	private Label engineMessageLabel;
	private Label engineMessageImageLabel;

	private Button fTestRadioButton;
	private Button fTestContainerRadioButton;

	public RubyTestingMainLaunchConfigurationTab(String mode) {
		super(mode);
	}

	private Label fProjectLabel;
	private Label fScriptLabel;
	private Text fContainerText;
	private Button fContainerSearchButton;
	private IModelElement fContainerElement;
	private final ILabelProvider fModelElementLabelProvider = new ModelElementLabelProvider();

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);

		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		//
		// one test
		//
		fTestRadioButton = new Button(comp, SWT.RADIO);
		fTestRadioButton
				.setText(DLTKTestingMessages.JUnitLaunchConfigurationTab_label_oneTest);
		fTestRadioButton.setLayoutData(createModeGridData());
		fTestRadioButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fTestRadioButton.getSelection()) {
					testModeChanged();
				}
			}
		});
		comp.setLayout(topLayout);
		createProjectEditor(comp);

		createMainModuleEditor(comp,
				DLTKLaunchConfigurationsMessages.mainTab_mainModule);
		createVerticalSpacer(comp, 1);
		//
		// container tests
		//
		fTestContainerRadioButton = new Button(comp, SWT.RADIO);
		fTestContainerRadioButton
				.setText(DLTKTestingMessages.JUnitLaunchConfigurationTab_label_containerTest);
		fTestContainerRadioButton.setLayoutData(createModeGridData());
		fTestContainerRadioButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (fTestContainerRadioButton.getSelection())
					testModeChanged();
			}
		});
		fContainerText = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 25;
		gd.horizontalSpan = 2;
		fContainerText.setLayoutData(gd);
		fContainerText.addModifyListener(getWidgetListener());

		fContainerSearchButton = createPushButton(comp,
				DLTKTestingMessages.JUnitLaunchConfigurationTab_label_search,
				null);
		fContainerSearchButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleContainerSearchButtonSelected();
			}
		});
		createVerticalSpacer(comp, 1);
		//
		// Testing engine
		//
		createTestEngineEditor(comp, "Ruby Testing engine");
		createVerticalSpacer(comp, 1);

		createDebugOptionsGroup(comp);
		createCustomSections(comp);
		//
		Dialog.applyDialogFont(comp);
	}

	/**
	 * 
	 */
	protected void handleContainerSearchButtonSelected() {
		IModelElement element = chooseContainer(fContainerElement);
		if (element != null) {
			setContainerElement(element);
			setProjectName(element.getScriptProject().getElementName());
		}
	}

	private void setContainerElement(IModelElement javaElement) {
		fContainerElement = javaElement;
		fContainerText.setText(getPresentationName(javaElement));
		validatePage();
		updateLaunchConfigurationDialog();
	}

	protected boolean validate() {
		updateEngineStatus();
		final boolean result;
		if (fTestContainerRadioButton.getSelection()) {
			if (fContainerElement == null) {
				setErrorMessage(DLTKTestingMessages.JUnitLaunchConfigurationTab_error_noContainer);
				return false;
			}
			result = validateProject(fContainerElement.getScriptProject());
		} else {
			result = super.validate();
		}
		return result && validateEngine();
	}

	private String getPresentationName(IModelElement element) {
		return fModelElementLabelProvider.getText(element);
	}

	private IModelElement chooseContainer(IModelElement initElement) {
		Class[] acceptedClasses = new Class[] { IProjectFragment.class,
				IScriptProject.class, IScriptFolder.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(
				acceptedClasses, false) {
			public boolean isSelectedValid(Object element) {
				return true;
			}
		};

		acceptedClasses = new Class[] { IScriptModel.class,
				IProjectFragment.class, IScriptProject.class,
				IScriptFolder.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IScriptProject) {
					if (!getNatureID().equals(
							((IScriptProject) element).getLanguageToolkit()
									.getNatureId())) {
						return false;
					}
				}
				if (element instanceof IProjectFragment) {
					IProjectFragment fragment = (IProjectFragment) element;
					if (fragment.isExternal() || fragment.isArchive()) {
						return false;
					}
				}
				try {
					if (element instanceof IScriptFolder
							&& !((IScriptFolder) element).hasChildren()) {
						return false;
					}
				} catch (ModelException e) {
					return false;
				}
				return super.select(viewer, parent, element);
			}
		};

		StandardModelElementContentProvider provider = new StandardModelElementContentProvider();
		ILabelProvider labelProvider = new ModelElementLabelProvider(
				ModelElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
				getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setComparator(new ModelElementComparator());
		dialog
				.setTitle(DLTKTestingMessages.JUnitLaunchConfigurationTab_folderdialog_title);
		dialog
				.setMessage(DLTKTestingMessages.JUnitLaunchConfigurationTab_folderdialog_message);
		dialog.addFilter(filter);
		dialog.setInput(DLTKCore.create(getWorkspaceRoot()));
		dialog.setInitialSelection(initElement);
		dialog.setAllowMultiple(false);

		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			return (IModelElement) element;
		}
		return null;
	}

	/**
	 * @return
	 */
	private GridData createModeGridData() {
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		return gd;
	}

	protected boolean needGroupForField(String fieldName) {
		return !FIELD_PROJECT.equals(fieldName)
				&& !FIELD_SCRIPT.equals(fieldName);
	}

	protected Label createLabelForField(Composite parent, String fieldName,
			String text) {
		final Label label = super.createLabelForField(parent, fieldName, text);
		if (FIELD_PROJECT.equals(fieldName) || FIELD_SCRIPT.equals(fieldName)) {
			label.setLayoutData(createIndentedGridData());
		}
		if (FIELD_PROJECT.equals(fieldName)) {
			fProjectLabel = label;
		} else if (FIELD_SCRIPT.equals(fieldName)) {
			fScriptLabel = label;
		}
		return label;
	}

	private GridData createIndentedGridData() {
		GridData gd = new GridData();
		gd.horizontalIndent = 25;
		return gd;
	}

	protected final void doCreateControl(Composite composite) {
		// NOP
	}

	private void testModeChanged() {
		boolean isSingleTestMode = fTestRadioButton.getSelection();
		setEnableSingleTestGroup(isSingleTestMode);
		setEnableContainerTestGroup(!isSingleTestMode);
		if (!isSingleTestMode && fContainerText.getText().length() == 0) {
			String projText = getProjectName();
			if (Path.EMPTY.isValidSegment(projText)) {
				IScriptProject project = getScriptModel().getScriptProject(
						projText);
				if (project != null && project.exists()) {
					setContainerElement(project);
				}
			}
		}
		validatePage();
		updateLaunchConfigurationDialog();
	}

	private void setEnableSingleTestGroup(boolean enabled) {
		fProjectLabel.setEnabled(enabled);
		setEnableProjectField(enabled);
		fScriptLabel.setEnabled(enabled);
		setEnableScriptField(enabled);
	}

	protected void projectChanged() {
		testModeChanged();
	}

	private void setEnableContainerTestGroup(boolean enabled) {
		fContainerSearchButton.setEnabled(enabled);
		fContainerText.setEnabled(enabled);
	}

	protected void createTestEngineEditor(Composite parent, String text) {
		Label fTestEngine = new Label(parent, SWT.NONE);
		fTestEngine.setText(text);
		engineType = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		engineType.setLayoutData(gd);
		engineType.addModifyListener(getWidgetListener());
		detect = createPushButton(parent,
				DLTKTestingMessages.MainLaunchConfigurationTab_detectEngine,
				null);

		ITestingEngine[] engines = TestingEngineManager
				.getEngines(getNatureID());
		for (int i = 0; i < engines.length; i++) {
			String name = engines[i].getName();
			this.engineType.add(name);
			nameToId.put(name, engines[i].getId());
		}
		detect.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleDetectButtonSelected();
			}
		});
		new Label(parent, SWT.NONE); // place holder
		Composite engineStatusComposite = new Composite(parent, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1;
		engineStatusComposite.setLayoutData(gd);
		engineStatusComposite.setLayout(new RowLayout());
		engineMessageImageLabel = new Label(engineStatusComposite, SWT.NONE);
		engineMessageLabel = new Label(engineStatusComposite, SWT.NONE);
	}

	private void handleDetectButtonSelected() {
		ISourceModule module = getSourceModule();
		if (module != null && module.exists()) {
			final ITestingEngine[] engines = TestingEngineManager
					.getEngines(getNatureID());
			final TestingEngineDetectResult result = TestingEngineManager
					.detect(engines, module);
			if (result != null) {
				engineType.select(Arrays.asList(engines).indexOf(
						result.getEngine()));
				updateEngineStatus(result.getStatus());
			}
		}
	}

	/**
	 * @param status
	 */
	private void updateEngineStatus(IStatus status) {
		Image newImage = null;
		String newMessage = status.getMessage();
		switch (status.getSeverity()) {
		case IStatus.OK:
			newMessage = EMPTY_STRING;
			break;
		case IStatus.INFO:
			newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_INFO);
			break;
		case IStatus.WARNING:
			newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_WARNING);
			break;
		case IStatus.ERROR:
			newImage = JFaceResources.getImage(Dialog.DLG_IMG_MESSAGE_ERROR);
			break;
		}
		showMessage(newMessage, newImage);
	}

	private String message = EMPTY_STRING;
	private Image messageImage;

	/**
	 * Show the new message and image.
	 * 
	 * @param newMessage
	 * @param newImage
	 */
	private void showMessage(String newMessage, Image newImage) {
		// Any change?
		if (message.equals(newMessage) && messageImage == newImage) {
			return;
		}
		message = newMessage;
		if (message == null)
			message = EMPTY_STRING;
		messageImage = newImage;
		engineMessageImageLabel.setImage(newImage);
		engineMessageLabel.setText(message);
	}

	private void updateEngineStatus() {
		updateEngineStatus(TestingEngineManager.getEngine(getEngineId()));
	}

	private void updateEngineStatus(final ITestingEngine engine) {
		IStatus status = Status.OK_STATUS;
		if (engine != null) {
			if (fTestRadioButton.getSelection()) {
				final ISourceModule module = getSourceModule();
				if (module != null) {
					status = engine.validateSourceModule(module);
				}
			} else {
				// TODO validate container
			}
		}
		if (status != null) {
			updateEngineStatus(status);
		}
	}

	protected ISourceModule getSourceModule() {
		if (!fTestRadioButton.getSelection()) {
			return null;
		}
		return super.getSourceModule();
	}

	private boolean validateEngine() {
		if (TestingEngineManager.getEngine(getEngineId()) == null) {
			setErrorMessage(DLTKTestingMessages.MainLaunchConfigurationTab_ErrorEngineNotSelected);
			return false;
		}
		return true;
	}

	protected void setDefaults(ILaunchConfigurationWorkingCopy configuration,
			IModelElement element) {
		if (element.getElementType() >= IModelElement.SOURCE_MODULE) {
			element = element.getAncestor(IModelElement.SOURCE_MODULE);
			if (element != null) {
				super.setDefaults(configuration, element);
				TestingEngineDetectResult detection = TestingEngineManager
						.detect(TestingEngineManager.getEngines(getNatureID()),
								(ISourceModule) element);
				if (detection != null) {
					configuration.setAttribute(
							DLTKTestingConstants.ATTR_ENGINE_ID, detection
									.getEngine().getId());
				}
			}
		} else if (element != null) {
			configuration.setAttribute(
					ScriptLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					element.getScriptProject().getElementName());
			configuration.setAttribute(
					DLTKTestingConstants.ATTR_TEST_CONTAINER, element
							.getHandleIdentifier());
		}
	}

	protected void doPerformApply(ILaunchConfigurationWorkingCopy config) {
		if (fTestContainerRadioButton.getSelection()
				&& fContainerElement != null) {
			config.setAttribute(
					ScriptLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					fContainerElement.getScriptProject().getElementName());
			config.setAttribute(DLTKTestingConstants.ATTR_TEST_CONTAINER,
					fContainerElement.getHandleIdentifier());
			config.setAttribute(
					ScriptLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME,
					EMPTY_STRING);
		} else {
			config.setAttribute(
					ScriptLaunchConfigurationConstants.ATTR_PROJECT_NAME,
					getProjectName());
			config.setAttribute(
					ScriptLaunchConfigurationConstants.ATTR_MAIN_SCRIPT_NAME,
					getScriptName());
			config.setAttribute(DLTKTestingConstants.ATTR_TEST_CONTAINER,
					EMPTY_STRING);
		}
		performApplyInteractiveConsole(config);
		config.setAttribute(DLTKTestingConstants.ATTR_ENGINE_ID, getEngineId());
	}

	protected void mapResources(ILaunchConfigurationWorkingCopy config)
			throws CoreException {
		DLTKTestingMigrationDelegate.mapResources(config);
	}

	private String getEngineId() {
		return (String) this.nameToId.get(this.engineType.getText());
	}

	protected void doInitializeForm(ILaunchConfiguration config) {
		initializeInteractiveConsoleFrom(config);
		// update test type
		String containerHandle = EMPTY_STRING;
		try {
			containerHandle = config.getAttribute(
					DLTKTestingConstants.ATTR_TEST_CONTAINER, EMPTY_STRING);
		} catch (CoreException ce) {
		}
		if (containerHandle.length() > 0) {
			updateTestContainerFromConfig(config);
		} else {
			updateTestScriptFromConfig(config);
		}
		// update engine
		String id = null;
		try {
			id = config.getAttribute(DLTKTestingConstants.ATTR_ENGINE_ID,
					EMPTY_STRING);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		if (id == null || id.length() == 0) {
			handleDetectButtonSelected();
		} else {
			final ITestingEngine[] engines = TestingEngineManager
					.getEngines(getNatureID());
			for (int i = 0; i < engines.length; i++) {
				final ITestingEngine engine = engines[i];
				if (engine.getId().equals(id)) {
					this.engineType.select(i);
					updateEngineStatus(engine);
					break;
				}
			}
		}
	}

	/**
	 * @param config
	 */
	private void updateTestScriptFromConfig(ILaunchConfiguration config) {
		updateMainModuleFromConfig(config);
		fTestRadioButton.setSelection(true);
		setEnableSingleTestGroup(true);
		setEnableContainerTestGroup(false);
		fTestContainerRadioButton.setSelection(false);
		fContainerText.setText(EMPTY_STRING);
	}

	/**
	 * @param config
	 */
	private void updateTestContainerFromConfig(ILaunchConfiguration config) {
		String containerHandle = EMPTY_STRING;
		IModelElement containerElement = null;
		try {
			containerHandle = config.getAttribute(
					DLTKTestingConstants.ATTR_TEST_CONTAINER, EMPTY_STRING);
			if (containerHandle.length() > 0) {
				containerElement = DLTKCore.create(containerHandle);
			}
		} catch (CoreException ce) {
		}
		if (containerElement != null)
			fContainerElement = containerElement;
		fTestContainerRadioButton.setSelection(true);
		setEnableSingleTestGroup(false);
		setEnableContainerTestGroup(true);
		fTestRadioButton.setSelection(false);
		if (fContainerElement != null)
			fContainerText.setText(getPresentationName(fContainerElement));
		setScriptName(EMPTY_STRING);
	}
}
